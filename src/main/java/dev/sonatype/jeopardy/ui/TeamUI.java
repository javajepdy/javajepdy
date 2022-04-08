package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.Team;
import dev.sonatype.jeopardy.model.forms.TeamForm;
import io.quarkus.qute.TemplateInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped

@Path("/ui/team")
public class TeamUI {

    private static final Logger log = LogManager.getLogger(TeamUI.class);

    @Inject
    HTMLTemplates t;

    @Inject
    TeamStore store;



    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("addteam")
    public TemplateInstance addTeam() {

        log.info("add team request for teams");

        return t.new_team.data("e",new HashMap(),"f",new TeamForm());

    }



    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("list")
    public TemplateInstance list() {

        log.info("list request for teams");
        return t.teams.data("teams",store.listAll());
    }



    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("edit/{id}")
    public TemplateInstance edit(@PathParam("id") Long id) {

        log.info("edit request for {}",id);
        Team team=store.findById(id);
        if(team==null) {
            log.error("resource {} not found",id);
            return null;
        }
        TeamForm f= TeamForm.newForm(team);

        return t.new_team.data("f",f,"e",new HashMap<>());

    }



    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("add")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance add(@MultipartForm TeamForm f,@Context HttpHeaders headers) {

        Long id=null;

        if(headers.getRequestHeaders().containsKey("x-team-update")) {
            try {
                id = Long.parseLong(headers.getHeaderString("x-team-update"));
            } catch(NumberFormatException nfe) {
                ;
            }
        }

        log.info("new team add request [{}] [{}] //{}",f.name,f.description,id);

        Map<String, String> errors = f.isValid();
        if (errors.isEmpty()) {

            Team mt = f.newTeam(store,id);
            store.persist(mt);
            return t.team_added.data("t", mt);
        } else {
            return t.team_form.data("f", f, "e", errors);
        }
    }
    }
