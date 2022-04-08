package dev.sonatype.jeopardy.model.forms;

import dev.sonatype.jeopardy.TeamStore;
import dev.sonatype.jeopardy.model.Team;

import javax.ws.rs.FormParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TeamForm {






    @FormParam("name")
    public String name;

    @FormParam("description")
    public String description;

    @FormParam("image")
    public File image;


    public Long id;


    public Map<String, String> isValid() {
        Map<String,String> errors=new HashMap<>();
        if(name==null || name.trim().equals("")) errors.put("name","cannot be blank");
        if(description==null || description.trim().equals("")) errors.put("description","cannot be blank");

        return errors;
    }

    public Team newTeam(TeamStore store, Long id) {

        Team t=null;
        if(id!=null && id>0) {
            t=store.findById(id);
        }
        if(t==null) {
            t = new Team();
        }

        t.name=name;
        t.description=description;

        if(image!=null) {
            try (FileInputStream fis=new FileInputStream(image) ){
                t.picture=fis.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return t;

    }

    public static TeamForm newForm(Team t) {

        TeamForm f=new TeamForm();

        f.description=t.description;
        f.name=t.name;
        f.id=t.id;

        return f;

    }
}
