package dev.sonatype.jeopardy.api;

import dev.sonatype.jeopardy.GameGenerator;
import dev.sonatype.jeopardy.GameStore;
import dev.sonatype.jeopardy.model.*;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.Form;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@ApplicationScoped

@Path("/api/event")

public class GameService {

    private static final Logger log = Logger.getLogger(GameService.class);


    @Inject
    GameGenerator generator;

    @Inject
    GameStore store;

    @POST
    @Path("/endgame")
    public Response endGame(@QueryParam("uuid")String uuid) {
        Game g=store.endGame(uuid);
        if(g==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(g.status.name()).build();
    }


    @POST
    @Path("/startgame")
    public Response startGame(@QueryParam("uuid")String uuid) {
        Game g=store.startGame(uuid);
        if(g==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(g.status.name()).build();
    }


    @POST
    @Path("/updategame")
    public Response updateGame(@QueryParam("uuid")String uuid) {
        Game g=store.broadcast(uuid);

        if(g==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(g.status.name()).build();
    }



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("add")
    @Produces(MediaType.TEXT_HTML)

    public Response add(@Form NewEventForm f) {


        String err=f.check();
        log.infof("add event requested (check=%s)",err);
        if(err!=null) return Response.serverError().build();

        // create the game
       Game e= generator.generate(f);
        if(e==null) throw new RuntimeException("event generation failed");

      String uuid=store.addEvent(e);

        log.infof("added event : shortkey = %s",e.shortCode);
        log.infof("added event : uuid     = %s",uuid);

        return Response.temporaryRedirect(URI.create("/ui/added?uuid="+uuid)).build();

    }


    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Game> getEvents() {
        return store.getActiveEventsByShortCode();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game getEvent(@PathParam("id") String id) {
        log.infof("get event requested (id=%s)",id);
        return store.getPublicEvent(id);
    }



    @POST
    @Path("revealClue")
    public Response revealClue(@QueryParam("uuid") String uuid,
                               @QueryParam("round") int round,
                               @QueryParam("row") int row,
                               @QueryParam("cell") int cell) {

        log.infof("reveal clue %s",uuid);
        Game g=store.getHostEvent(uuid);
        if(g==null) return Response.status(Response.Status.NOT_FOUND).build();
        Round r=g.rounds.get(round);
        Row w=r.rows.get(row-1);
        Cell c=w.cells.get(cell-1);

        store.revealClue(g,c);

        return Response.ok().build();
    }

    @POST
    @Path("revealAnswer")
    public Response revealAnswer(@QueryParam("uuid") String uuid,
                               @QueryParam("round") int round,
                               @QueryParam("row") int row,
                               @QueryParam("cell") int cell) {

        log.infof("reveal answeer %s",uuid);
        Game g=store.getHostEvent(uuid);
        if(g==null) return Response.status(Response.Status.NOT_FOUND).build();
        Round r=g.rounds.get(round);
        Row w=r.rows.get(row-1);
        Cell c=w.cells.get(cell-1);

        store.revealAnswer(g,c);

        return Response.ok().build();
    }

    @POST
    @Path("score")
    public Response score(@Form ScoreForm f){

        log.infof("score %s (w=%d,l=%d",f.uuid,f.winner,f.loser);
        Game g=store.getHostEvent(f.uuid);
        if(g==null) return Response.status(Response.Status.NOT_FOUND).build();
        Round r=g.rounds.get(f.round);
        Row w=r.rows.get(f.row-1);
        Cell c=w.cells.get(f.cell-1);
        store.score(g,c,f.winner-1,f.loser-1);

        return Response.temporaryRedirect(URI.create("/ui/host?uuid="+f.uuid)).build();
    }


}
