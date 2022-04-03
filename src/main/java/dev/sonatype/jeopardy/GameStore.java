package dev.sonatype.jeopardy;

import com.mongodb.client.model.Indexes;
import dev.sonatype.jeopardy.model.Cell;
import dev.sonatype.jeopardy.model.Game;
import dev.sonatype.jeopardy.model.GameState;
import dev.sonatype.jeopardy.model.Team;
import dev.sonatype.jeopardy.ui.UpdateService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import org.bson.types.ObjectId;
import org.hashids.Hashids;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class GameStore implements PanacheMongoRepository<Game> {

    @Inject
    UpdateService publisher;

    private static final Logger log = Logger.getLogger(GameStore.class);

    private final Hashids hashids = new Hashids("this is my salt");

    public GameStore() {

        log.warn("--> game store created");
        mongoCollection().createIndex(Indexes.text("shortCode"));


    }

    public synchronized String addEvent(Game e) {

        String hash = hashids.encode(Integer.MAX_VALUE- (count()+1) );
        e.shortCode=hash;

        persist(e);

        return e.id.toString();
    }

    public Game getPublicEvent(String shortKey) {
        log.infof("get public event %s",shortKey);
        Game g=null;
        try {
             g = find("shortCode", shortKey).firstResult();
        } catch( IllegalArgumentException e) {
            ;
        }

        if(g==null) log.infof("not found");
        return g;
    }
    public Game getHostEvent(String uuid) {
        try {
            ObjectId gameId = new ObjectId(uuid);
           return findById(gameId);
        } catch( IllegalArgumentException e) {
            ;
        }
        return null;

    }


    public Collection<Game> getActiveEventsByShortCode() {
        return list("status", GameState.started);
    }

    public Game startGame(String uuid) {
        Game g=getHostEvent(uuid);
        g.status=GameState.started;
        persistOrUpdate(g);
        publisher.broadcast(g.shortCode);

        return g;
    }

    public Collection<Game> getGamesByDateReversed() {
        return listAll(Sort.by("created").descending());
    }

    public Game endGame(String uuid) {
        Game g=getHostEvent(uuid);
        g.status=GameState.finished;
        persistOrUpdate(g);
        publisher.broadcast(g.shortCode);
        return g;

    }

    public Game broadcast(String uuid) {
        Game g=getHostEvent(uuid);
        if(g!=null) {
            publisher.broadcast(g.shortCode);
        }
        return g;
    }

    public void revealClue(Game g, Cell c) {

        g.currentCell=c;
        g.status=GameState.showClue;
        persistOrUpdate(g);
        publisher.broadcast(g.shortCode);

    }


    public void revealAnswer(Game g, Cell c) {

        g.currentCell=c;
        g.status=GameState.showAnswer;
        persistOrUpdate(g);
        publisher.broadcast(g.shortCode);

    }

    public void score(Game g, Cell c, int winner, int loser) {
        c.used=true;

        if(winner>=0) {
            Team w= g.teams.get(winner);
            w.score.points+=c.value;
            w.score.correctAnswers++;
        }

        if(loser>=0) {
            Team l= g.teams.get(loser);
            l.score.points-=c.value;
            l.score.wrongAnswers++;
        }

        g.currentCell=null;
        g.status=GameState.started;
        persistOrUpdate(g);
        publisher.broadcast(g.shortCode);


    }

    public Game nextRound(String uuid) {

        Game g=getHostEvent(uuid);
        int max=g.rounds.size();
        int next=g.currentRound+1;
        log.infof("next round requested %s %d -> %d / %d",uuid,g.currentRound,next,max);
        if(next<max) {
            g.currentRound=next;
            persistOrUpdate(g);
            publisher.broadcast(g.shortCode);
        }

        return g;
    }


    public Game previousRound(String uuid) {

        Game g=getHostEvent(uuid);
        int next=g.currentRound-1;

        log.infof("previous round requested %s %d -> %d",uuid,g.currentRound,next);
        if(next>=0) {
            g.currentRound=next;
            persistOrUpdate(g);
            publisher.broadcast(g.shortCode);
        }

        return g;
    }
}

