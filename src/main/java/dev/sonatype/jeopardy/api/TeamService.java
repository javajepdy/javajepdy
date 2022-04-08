package dev.sonatype.jeopardy.api;

import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.Team;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/api/team")

public class TeamService {

    @Inject
    TeamStore store;


    @GET
    @Path("count")
    public long count() {
        return store.count();
    }

    @GET
    @Path("all")

    public List<Team> all() {

        return store.listAll();
    }


    @GET
    @Path("icon")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] icon(@QueryParam("id") long id) {
        return store.findById(id).picture;
    }


    @DELETE
    @Path("delete")
    @Transactional
    public Response delete(@QueryParam("id") long id) {

        Team mt= store.findById(id);
        if(mt==null) return Response.status(Response.Status.NOT_FOUND).build();
        else {
            store.delete(mt);
        }
        return Response.ok().build();
    }


}
