package dev.sonatype.jeopardy.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NewTeamForm {

    @FormParam("name")
    public String name;

    @FormParam("description")
    public String description;

    @FormParam("image")
    public File image;


    public Map<String, String> isValid() {
        Map<String,String> errors=new HashMap<>();
        if(name==null || name.trim().equals("")) errors.put("name","cannot be blank");
        if(description==null || description.trim().equals("")) errors.put("description","cannot be blank");

        return errors;
    }

    public MyTeam newTeam() {
        MyTeam t=new MyTeam();
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
}
