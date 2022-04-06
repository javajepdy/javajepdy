package dev.sonatype.jeopardy.api;

import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.MyTeam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

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

    public List<MyTeam> all() {

        return store.listAll();
    }


    @GET
    @Path("icon")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] icon(@QueryParam("id") long id) {
        return store.findById(id).picture;
    }


}
