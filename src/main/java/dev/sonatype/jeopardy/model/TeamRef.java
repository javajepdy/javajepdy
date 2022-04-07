package dev.sonatype.jeopardy.model;

/**
 * Game Player - individual or team.
 */
public class TeamRef implements  Comparable<TeamRef>{

    public String name;
    public Score score;
    public Long teamID;

    public TeamRef() {

    }
    public TeamRef(String name,Long id) {
        this.name=name;
        this.teamID=id;
        score=new Score();
    }

    @Override
    public int compareTo(TeamRef to) {
        if(to ==null) return -1;
        return this.name.compareTo(to.name);
    }

    public int comparePoints(TeamRef winner) {
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
