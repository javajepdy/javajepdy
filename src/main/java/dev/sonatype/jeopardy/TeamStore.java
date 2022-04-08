package dev.sonatype.jeopardy;

import dev.sonatype.jeopardy.model.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class TeamStore implements PanacheRepository<Team> {

}
