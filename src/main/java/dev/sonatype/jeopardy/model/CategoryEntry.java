package dev.sonatype.jeopardy.model;

import java.net.URI;

/**
 * The individual entry for a category
 * Contains the cryptic clue, answer and pointer to any related image.
 * The
 */


public class CategoryEntry implements  Comparable<CategoryEntry>{

    public String clue;
    public String answer;
    public URI imageURL;
    public int value;
    public boolean answered=false;

    public CategoryEntry() {

    }
    public CategoryEntry(int dollarValue, String clue, String answer) {
        this.value=dollarValue;
        this.clue=clue;
        this.answer=answer;
    }

    @Override
    public int compareTo(CategoryEntry o) {
        if(o==null) return -1;

        int c=answer.compareTo(o.answer);
        if(c!=0) return c;

        c=clue.compareTo(o.clue);
        if(c!=0) return c;

        return 0;

    }
}
