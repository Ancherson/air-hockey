package airhockey.model;


import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents the model of the game
 */
public class Model {
    /**
     * The board of the game
     */
    private Board board;

    /**
     * The two players in the game
     */
    private Player[] players;

    /**
     * Boolean that changes when the pusher has moved, is initialized at false
     */
    private boolean hasPusherMoved = false;

    /**
     * The maximum of point that a player can score
     */
    private static final int SCORE_MAX=7;

    /**
     * Linked list of Vector which represents points in debug mode
     */
    private LinkedList<Vector> DEBUG_POINTS = new LinkedList<>();

    /**
     * Hash map of (Vector,Vector) which represents lines in debug mode
     */
    private HashMap<Vector, Vector> DEBUG_LINES = new HashMap<>();

    /**
     * Counter
     */
    private int counter = 0;

    /**
     * Maximum value of the counter
     */
    private int maxCount = 30;

    /**
     * Constructor of the model
     * Contains a bord and an array of two players
     */
    public Model() {
        board = new Board();
        players = new Player[2];
        players[0] = new Player();
        players[1] = new Player();
    }

    /**
     * @return Board of the game
     */
    public Board getBoard(){
        return board;
    }

    /**
     * @param numPlayer the number of the player
     * @return the score of the player
     */
    public int getScore(int numPlayer) {
        return players[numPlayer].getScore();
    }

    /**
     * Changes the score of the player with a new one
     * @param numPlayer the number of the player
     * @param score the new score of the player
     */
    public void setScore(int numPlayer, int score) {
        players[numPlayer].setScore(score);
    }

    /**
     * Changes the player to a bot
     * @param numPlayer the number of the player
     */
    public void setBot(int numPlayer){
        players[numPlayer] = new Bot();
    }

    /**
     * @return if the game is finished
     */
    public boolean isFinished(){
        return (hasWon(0) || hasWon(1));
    }

    /**
     * Checks if the player's score is equal to score max
     * @param numplayer the number of the player
     * @return if the player has won
     */
    public boolean hasWon(int numplayer){
        return players[numplayer].getScore()>=SCORE_MAX;
    }

    /**
     * @return the counter
     */
    public int getCounter(){
        return counter;
    }

    /**
     * @return the debug points
     */
    public LinkedList<Vector> getDEBUG_POINTS() {
        return DEBUG_POINTS;
    }

    /**
     * @return the debug lines
     */
    public HashMap<Vector, Vector> getDEBUG_LINES() {
        return DEBUG_LINES;
    }

    /**
     * @param maxCount the new value of max count
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * Updates the board, the bot relating to the time dt
     * Resets when a goal has been scored 
     * @param dt double of the time
     */
    public void update(double dt){
        for(Player p : players){
            if(p instanceof Bot) {
                ((Bot) p).update(this,dt);
            }
        }
        board.update(dt);
        if(board.getPalet().getScoredGoal() != -1){
            counter++;
            if(counter > maxCount) reset();
        }else {
            counter = 0;
        }
    }

    /**
     * Reset the board and add a point to the player that scored a goal
     */
    public void reset() {
        int p = board.getPalet().getScoredGoal();
        board.getPalet().resetScoredGoal();
        players[1-p].setScore(players[1-p].getScore()+1);
        board.reset(p);
        counter = 0;
    }

    /**
     * @return if the pusher has moved
     */
    public boolean hasPusherMoved() {
        if(hasPusherMoved) {
            hasPusherMoved = false;
            return true;
        }
        return false;
    }

    /**
     * @return if the palet has collided
     */
    public boolean hasPalletCollided(){
        Palet palet = board.getPalet();
        if(palet.collisionned) {
            palet.collisionned = false;
            return true;
        }
        return false;
    }

    /**
     * Resets the speed of the player's pusher
     * @param numplayer the number of the player
     */
    public void PusherReleased(int numplayer){
        this.board.getPushers()[numplayer].resetSpeed();
        hasPusherMoved = true;
    }

    /**
     * Changes the position of the pusher of the player relating to the time dt
     * @param x the new x position of the pusher
     * @param y the new y position of the pusher
     * @param dt double of the time
     * @param numplayer the number of the player
     */
    public void setLocationPusher(double x, double y, double dt,int numplayer) {
        Pusher[] pushers = board.getPushers();
        pushers[numplayer].resetMovement();
        pushers[numplayer].moveTo(new Vector(x,y),board.getWalls(), board.getInvisibleWalls(), board.getPalet());
        
        pushers[numplayer].actualizeSpeed(dt);
        pushers[numplayer].wallCollisions(board.getWalls());
        pushers[numplayer].wallCollisions(board.getInvisibleWalls());
        hasPusherMoved = true;
    }

    /**
     * Changes the position of the palet relating to the time dt
     * @param x double of the new x position of the palet
     * @param y double of the new y position of the palet
     */
    public void setLocationPalet(double x, double y) {
        board.getPalet().setPosition(new Vector(x, y));
    }

    /**
     * Checks if the position of the mouse of a player is in a pusher
     * @param x the x position of the player's mouse
     * @param y the y position of the player's mouse
     * @param numplayer the number of the player
     * @return if the player is in his pusher
     */
    public boolean isInPusher(double x, double y, int numplayer) {
        return board.getPushers()[numplayer].getPosition().add(new Vector(x, y).multiply(-1)).length() < board.getPushers()[numplayer].getRadius();
    }
}
