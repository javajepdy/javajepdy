package dev.sonatype.jeopardy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.URI;

@Entity
public class Team {

    public String name;

    public String description;

    public byte[] picture;

    @Id
    @GeneratedValue
    public Long id;

}
