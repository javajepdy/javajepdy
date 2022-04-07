package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.ClueStore;
import dev.sonatype.jeopardy.GameGenerator;
import dev.sonatype.jeopardy.GameStore;
import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.*;
import dev.sonatype.jeopardy.model.forms.NewGameForm;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.Template;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.util.HashMap;
import java.util.Map;


@Path("/ui/game")
public class GameUI {

    private static final Logger log = Logger.getLogger(GameUI.class);


    @Inject
    GameGenerator generator;


    @Inject
    HTMLTemplates t;


    @Inject
    Template game;



    @Inject
    Template nogame;




    @Inject
    Template viewstate;



    @Inject
    GameStore store;
    @Inject
    ClueStore clues;
    @Inject
    TeamStore teams;

    @GET
    @Path("main")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        log.info("main requested");

        return t.main.data("store",store,"clues",clues);
    }


    @GET
    @Path("view")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance view(@QueryParam("id") String id) {
        log.infof("view %s requested",id);
        Game g=store.getPublicEvent(id);
        if(g==null) return nogame.data(null);
        return t.view.data("id",id,"game",g);
    }

    @GET
    @Path("viewstate")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance viewstate(@QueryParam("game") String id) {
        log.infof("viewstate %s requested",id);
        Game g=store.getPublicEvent(id);
        if(g==null) return nogame.data(null);
        return viewstate.data("game",g);
    }




    @GET
    @Path("find")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance find(@QueryParam("host") boolean host) {
        log.info("find requested");
        store.getGamesByDateReversed();

        return t.find.data("games",store,"host",host);
    }


    /**
     * Initial page for a new game.
     * Shows the host controller page
     */
    @POST
    @GET
    @Path("host")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance host(@QueryParam("uuid") String uuid) {

        log.infof("game %s requested",uuid);
        Game g=store.getHostEvent(uuid);
        if(g==null)  return nogame.data(null);
        return t.host.data("game",g,"uuid",uuid);
    }


    /**
     * Added sucessfully page
     * Shows the host controller page
     */
    @POST
    @Path("added")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance added(@QueryParam("uuid") String uuid) {

        log.infof("game %s requested",uuid);
        Game g=store.getHostEvent(uuid);
        return t.added.data("game",g,"uuid",uuid);

    }

    @POST
    @Path("start")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance start(@QueryParam("uuid") String uuid) {

        log.infof("game %s start requested",uuid);

        Game g=store.startGame(uuid);
        return t.gamestate.data("game",g,"uuid",uuid);

    }


    @POST
    @Path("endgame")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance endGame(@QueryParam("uuid") String uuid) {

        log.infof("game %s end requested",uuid);

        Game g=store.endGame(uuid);
        return t.gamestate.data("game",g,"uuid",uuid);

    }


    @POST
    @Path("clue_modal")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance clueModal(@QueryParam("uuid") String uuid,
                                      @QueryParam("row") int row,
                                      @QueryParam("round") int round,
                                      @QueryParam("cell") int cell) {

        log.infof("game modal requested uuid=%s,round=%d,row=%d,cell=%d",uuid,round,row,cell);
        Game g=store.getHostEvent(uuid);
        Round r=g.rounds.get(round);
        Row w=r.rows.get(row-1);
        Cell c=w.cells.get(cell-1);
        return t.clue_modal.data("game",g,"round",round,"row",row,"cell",cell,"c",c);

    }


    @POST
    @Path("nextRound")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance nextRound(@QueryParam("uuid") String uuid) {

        Game g=store.nextRound(uuid);

        return t.host_grid.data("game",g,"uuid",uuid);
    }


    @POST
    @Path("previousRound")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance previousRound(@QueryParam("uuid") String uuid) {

        Game g=store.previousRound(uuid);

        return t.host_grid.data("game",g,"uuid",uuid);
    }




    @GET
    @Path("new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance newGame() {

        return t.newgame
                .data("e",new HashMap())
                .data("f",new NewGameForm())
                .data("teams",teams.listAll());
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("add")
    @Produces(MediaType.TEXT_HTML)

    public TemplateInstance add(@MultipartForm NewGameForm f) {


        Map<String, String> errors = f.isValid(teams);
        if (errors.isEmpty()) {

            Game g = generator.generate(f);
            store.addEvent(g);
            return t.added.data("g", g,"uuid",g.uuid());
        } else {
            return t.game_form.data("f", f, "e", errors,"teams",teams.listAll());
        }
    }
}
