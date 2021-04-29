package airhockey.model;


import java.util.HashMap;
import java.util.LinkedList;

public class Model {
    private Board board;
    private Player[] players;
    private boolean hasPusherMoved = false;
    private static final int SCORE_MAX=7;

    private LinkedList<Vector> DEBUG_POINTS = new LinkedList<>();
    private HashMap<Vector, Vector> DEBUG_LINES = new HashMap<>();

    private int counter = 0;
    private int maxCount = 30;

    public Model() {
        board = new Board();
        players = new Player[2];
        players[0] = new Player();
        players[1] = new Player();
    }

    public Board getBoard(){
        return board;
    }

    public int getScore(int numPlayer) {
        return players[numPlayer].getScore();
    }

    public void setScore(int numPlayer, int score) {
        players[numPlayer].setScore(score);
    }

    public void setBot(int numPlayer){
        players[numPlayer] = new Bot();
    }

    public boolean isFinished(){
        return (hasWon(0) || hasWon(1));
    }

    public boolean hasWon(int numplayer){
        return players[numplayer].getScore()>=SCORE_MAX;
    }

    public int getCounter(){
        return counter;
    }

    public LinkedList<Vector> getDEBUG_POINTS() {
        return DEBUG_POINTS;
    }

    public HashMap<Vector, Vector> getDEBUG_LINES() {
        return DEBUG_LINES;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

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

    public void reset() {
        int p = board.getPalet().getScoredGoal();
        board.getPalet().resetScoredGoal();
        players[1-p].setScore(players[1-p].getScore()+1);
        board.reset(p);
        counter = 0;
    }

    public boolean hasPusherMoved() {
        if(hasPusherMoved) {
            hasPusherMoved = false;
            return true;
        }
        return false;
    }

    public boolean hasPalletCollided(){
        Palet palet = board.getPalet();
        if(palet.collisionned) {
            palet.collisionned = false;
            return true;
        }
        return false;
    }

    public void PusherReleased(int numplayer){
        this.board.getPushers()[numplayer].resetSpeed();
        hasPusherMoved = true;
    }

    public void setLocationPusher(double x, double y, double dt,int numplayer) {
        Pusher[] pushers = board.getPushers();
        pushers[numplayer].resetMovement();
        pushers[numplayer].moveTo(new Vector(x,y),board.getWalls(), board.getInvisibleWalls(), board.getPalet());
        //pushers[numplayer].setSpeed(pushers[numplayer].getPosition().add(pushers[numplayer].getLastPosition().multiply(-1)).normalize().multiply(1.0/dt));
        pushers[numplayer].actualizeSpeed(dt);
        pushers[numplayer].wallCollisions(board.getWalls());
        pushers[numplayer].wallCollisions(board.getInvisibleWalls());
        hasPusherMoved = true;
    }

    public void setLocationPalet(double x, double y) {
        board.getPalet().setPosition(new Vector(x, y));
    }

    public boolean isInPusher(double x, double y, int numplayer) {
        return board.getPushers()[numplayer].getPosition().add(new Vector(x, y).multiply(-1)).length() < board.getPushers()[numplayer].getRadius();
    }
}
