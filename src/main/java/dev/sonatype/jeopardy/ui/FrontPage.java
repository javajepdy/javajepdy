package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.model.Game;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/")
public class FrontPage {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index() {
        return Response.temporaryRedirect(URI.create("/ui/game/main")).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response game(@PathParam("id") String id) {

        return Response.temporaryRedirect(URI.create("/ui/view?id="+id)).build();
    }


}
