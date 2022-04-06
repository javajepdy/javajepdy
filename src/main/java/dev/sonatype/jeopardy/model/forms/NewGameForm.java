package dev.sonatype.jeopardy.model.forms;


import dev.sonatype.jeopardy.GameStore;
import dev.sonatype.jeopardy.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.FormParam;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NewGameForm {

    private static final Logger log = LogManager.getLogger(NewGameForm.class);

    @FormParam("name")
    public String name;

    @FormParam("rounds")
    public int rounds;

    @FormParam("categories")
    public int categories;

    @FormParam("seed")
    public int seed;

    @FormParam("team1")
    public String team1;

    @FormParam("team2")
    public String team2;

    @FormParam("team3")
    public String team3;

    @FormParam("team4")
    public String team4;

    public Set<String> teams;

    public String toString() {
        return "name="+name+" rounds="+rounds;
    }

    public Map<String, String> isValid() {
        Map<String,String> errors=new HashMap<>();

        if(name==null || name.trim().equals("")) {
            log.error("game name missing");
            errors.put("name", "event name required");
        } else {
            if(name.contains("$"))  {
                log.error("game name {} contains invalid characters ",name);
                errors.put("name", "event name cannot contain $");
            }
        }

        if(rounds<1 || rounds>3) errors.put("rounds","number of rounds must be between 1 and 3");
        collectTeams();

        if(teams.isEmpty()) errors.put("general","no teams specified");
        if(teams.size()==1) errors.put("general","more than one unique team required");

        return errors;
    }

    private void collectTeams() {
        teams=new TreeSet<>();
        if(team1!=null && team1.trim().equals("")==false) teams.add(team1);
        if(team2!=null && team2.trim().equals("")==false) teams.add(team2);
        if(team3!=null && team3.trim().equals("")==false) teams.add(team3);
        if(team4!=null && team4.trim().equals("")==false) teams.add(team4);
    }


}
