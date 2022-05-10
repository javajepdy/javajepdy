package dev.sonatype.jeopardy;

import dev.sonatype.jeopardy.model.Category;
import dev.sonatype.jeopardy.model.Game;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.io.*;
import java.util.*;

@Singleton
public class ClueStore implements PanacheMongoRepository<Category> {

    private static final Logger log = Logger.getLogger(ClueStore.class);

    public ClueStore() throws IOException {

        log.warn("----> clue store created");


        if(count()==0) {
            insertTestData();
        }



        int total=listAll().stream().mapToInt(c -> c.clueCount()).sum();

        System.out.println("Clue store has "+count()+" categories, with total "+total+" clues");



    }

    public String getDescription(String cat) {
        Category c=getCategoryByTitle(cat);
        if(c==null) return "?"+cat;
        return c.description;
    }

    private void insertTestData() throws IOException {

        InputStream is=null;

        File f=new File("localclues.csv");

        if(f.exists()) {
            log.info("using local file csv");
            is=new FileInputStream(f);
        } else {
            log.warn("using embedded  test file csv");
           is=getClass().getResourceAsStream("/clues.csv");
        }

        if(is==null) throw new RuntimeException("no clue file");
        Reader in = new InputStreamReader(is);

        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        int r=0;
        for (CSVRecord record : records) {
            r++;
            String category ="";
            String value ="100";
            String clue = "";
            String answer = "";

            if(record.isSet("Category")) category=record.get("Category");
            if(record.isSet("Value")) value = record.get("Value");
            if(record.isSet("Clue"))  clue = record.get("Clue");
            if(record.isSet("Answer"))  answer = record.get("Answer");


            value=value.replace(",","");
            value=value.replace("'","");
            value=value.replace("\"","");

            clue=clue.trim();
            value=value.trim();
            category=category.trim().toUpperCase();
            answer=answer.trim();

            if(category.equals('"') || clue.equals("") || value.equals("") || answer.equals("")) {
                System.out.println("record "+r+"invalid");
                continue;
            }

            int dollarValue=Integer.parseInt(value);

            Category cat=getCategoryByTitle(category);

            if(cat==null) {
                cat=new Category();
                cat.title=category;

            }

            cat.addEntry(dollarValue,clue,answer);
            persistOrUpdate(cat);
        }

        in.close();
    }

    public Category getCategoryByTitle(String title) {
        return find("title", title).firstResult();
    }


    public List<Category> getCategories() {
        return listAll();
    }
}
