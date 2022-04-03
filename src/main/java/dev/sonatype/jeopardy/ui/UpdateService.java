package dev.sonatype.jeopardy.ui;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.UUID;

@ApplicationScoped
@Path("/ui/update")

public class UpdateService {
    private static final Logger log = Logger.getLogger(UpdateService.class);

    private SseBroadcaster sseBroadcaster;


    @Context
    private Sse sse;


    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink es, @QueryParam("game") String game) {

        log.infof("Registering update listener for game %s",game);
        if(sseBroadcaster==null) {
            sseBroadcaster = sse.newBroadcaster();
            sseBroadcaster.onClose(eventSink -> log.infof("On close EventSink: %s", eventSink));
            sseBroadcaster.onError(
                    (eventSink, throwable) -> log.infof("On Error EventSink: %s, Throwable: %s", eventSink, throwable));

        }

        sseBroadcaster.register(es);

    }


    @POST
    public void broadcast(String message) {
        if(sse!=null) {
            OutboundSseEvent event = sse.newEventBuilder().name(message).data(message).reconnectDelay(10000).build();
            log.infof("broadcasting %s",event.getName());
            sseBroadcaster.broadcast(event);
        } else {
            log.info("unable to broadcast. No sse");
        }
    }

}
