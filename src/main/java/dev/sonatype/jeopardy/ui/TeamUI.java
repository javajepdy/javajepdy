package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.MyTeam;
import dev.sonatype.jeopardy.model.forms.NewTeamForm;
import io.quarkus.qute.TemplateInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
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
        return t.new_team.data("e",new HashMap(),"f",new NewTeamForm());
    }




    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("add")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance add(@MultipartForm NewTeamForm f) {

        log.info("new team add request [{}] [{}] [{}]",f.name,f.description,f.image.getAbsolutePath());

        Map<String, String> errors = f.isValid();
        if (errors.isEmpty()) {

            MyTeam mt = f.newTeam();
            store.persist(mt);
            return t.team_added.data("t", mt);
        } else {
            return t.team_form.data("f", f, "e", errors);
        }
    }
    }
