package dev.sonatype.jeopardy.model;

public enum EntryValue {

    easy,middling,hard,fiendish,superfiendish;

    public int score() {
        return (this.ordinal()+1)*100;
    }
}
