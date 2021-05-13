package airhockey.model;

/**
 * This class represents a player
 */
public class Player {
    /**
     * Score of the player
     */
    private int score;

    /**
     * Contructor
     */
    public Player(){
        score = 0;
    }

    /**
     * return the score of the player
     * @return the score of the player
     */
    public int getScore(){
        return score;
    }

    /**
     * modify the score of the player
     * @param s the new score
     */
    public void setScore(int s){
        score = s;
    }
}
