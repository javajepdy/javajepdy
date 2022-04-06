package dev.sonatype.jeopardy.model.forms;


import javax.ws.rs.FormParam;
import java.util.Set;
import java.util.TreeSet;

public class ScoreForm {

    @FormParam("uuid")
    public String uuid;

    @FormParam("round")
    public int round;

    @FormParam("row")
    public int row;

    @FormParam("cell")
    public int cell;

    @FormParam("winner")
    public int winner;

    @FormParam("loser")
    public int loser;

}
