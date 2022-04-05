package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.GameStore;
import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.MyTeam;
import dev.sonatype.jeopardy.model.NewEventForm;
import dev.sonatype.jeopardy.model.NewTeamForm;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ApplicationScoped

@Path("/ui/team")
public class TeamService {

    private static final Logger log = LogManager.getLogger(TeamService.class);

    @Inject
    HTMLTemplates t;

    @Inject
    TeamStore store;

    @GET
    @Path("count")
    @Transactional
    public long count() {
        MyTeam mt=new MyTeam();
        mt.name= ""+new Date();
        mt.picture="hello".getBytes();
        store.persist(mt);
        return store.count();
    }

    @GET
    @Path("all")

    public List<MyTeam> all() {

        return store.listAll();
    }
    @GET
    @Path("icon")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] icon(@QueryParam("id") long id) {
        return store.findById(id).picture;
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
