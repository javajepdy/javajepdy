package dev.sonatype.jeopardy.model.forms;


import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.FormParam;
import java.util.*;

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

    @FormParam("team5")
    public String team5;

    @FormParam("team6")
    public String team6;

    public Set<Team> teams=new HashSet<>();

    public String toString() {
        return "name="+name+" rounds="+rounds;
    }

    public Map<String, String> isValid(TeamStore ts) {

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

        if(rounds<1 || rounds>6) errors.put("rounds","number of rounds must be between 1 and 6");

        Set<Long> teamIDs=collectTeams();
        if(teamIDs.isEmpty()) errors.put("general","no teams specified");
        if(teamIDs.size()==1) errors.put("general","more than one unique team required");

        for(Long l:teamIDs) {

            Team mt=ts.findById(l);
            if(mt==null) {
                errors.put("general","unknown team id "+l);
            } else {
                teams.add(mt);
            }
        }
        return errors;
    }

    private Set<Long> collectTeams() {

        Set<Long> names=new TreeSet<>();
        addIfValid(names,team1);
        addIfValid(names,team2);
        addIfValid(names,team3);
        addIfValid(names,team4);
        addIfValid(names,team5);
        addIfValid(names,team6);

        return names;
    }

    private void addIfValid(Set<Long> names, String s) {
        try {
            Long l=Long.parseLong(s);
            names.add(l);
        }  catch(Exception nfe) {
            ; // ignore - not a valid team id
        }
    }


}
