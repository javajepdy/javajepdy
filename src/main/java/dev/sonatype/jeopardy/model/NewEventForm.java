package dev.sonatype.jeopardy.model;


import javax.ws.rs.FormParam;
import java.util.Set;
import java.util.TreeSet;

public class NewEventForm {

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

    public String check() {

        if(name==null || name.trim().equals("")) return "event name required";

        if(rounds<1 || rounds>3) return "number of rounds must be between 1 and 3";

        collectTeams();

        if(teams.isEmpty()) return "no teams specified";
        if(teams.size()==1) return "more than one unique team required";

        return null;
    }

    private void collectTeams() {
        teams=new TreeSet<>();
        if(team1!=null && team1.trim().equals("")==false) teams.add(team1);
        if(team2!=null && team2.trim().equals("")==false) teams.add(team2);
        if(team3!=null && team3.trim().equals("")==false) teams.add(team3);
        if(team4!=null && team4.trim().equals("")==false) teams.add(team4);
    }
}
