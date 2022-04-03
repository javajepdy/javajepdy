package dev.sonatype.jeopardy.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Game Category:  "rivers of london", "presidents of the USA"
 * Contains all clues for the category
 */
@MongoEntity(collection="categories")
public class Category  {

    public ObjectId id;
    public String title;
    public String description;
    public Set<CategoryEntry> entries=new TreeSet<>(new CategoryEntryComparator());



    public void addEntry(int dollarValue, String clue, String answer) {

        CategoryEntry ce=new CategoryEntry(dollarValue,clue,answer);
        entries.add(ce);
    }

    public int clueCount() {
        return entries.size();
    }

    public List<CategoryEntry> getSortedEntries() {
        List<CategoryEntry> clues=new LinkedList<>();
        clues.addAll(entries);
        Collections.sort(clues,new CategoryEntryComparator());
        return clues;
    }

    public String reference() {
        return ""+this.hashCode();
    }
}
