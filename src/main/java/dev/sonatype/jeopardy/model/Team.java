package dev.sonatype.jeopardy.model;

/**
 * Game Player - individual or team.
 */
public class Team implements  Comparable<Team>{

    public String name;
    public Score score;


    public Team() {

    }
    public Team(String name) {
        this.name=name;
        score=new Score();
    }

    @Override
    public int compareTo(Team to) {
        if(to ==null) return -1;
        return this.name.compareTo(to.name);
    }

    public int comparePoints(Team winner) {
        if(winner==null) return 1;  // we are higher

        if(winner.score.points>score.points) return -1; // the win
        if(winner.score.points>score.points) return 1; // we win

        // equal points
        if(winner.score.wrongAnswers<score.wrongAnswers) return -1; // they win - less bad answers
        if(winner.score.wrongAnswers>score.wrongAnswers) return 1; // we  win - less bad answers
        if(winner.score.correctAnswers>score.correctAnswers) return -1;  // they win answered more questions
        if(winner.score.correctAnswers<score.correctAnswers) return 1;  // we win answered more questions

        return 0;  //exactly the same

     }
}
