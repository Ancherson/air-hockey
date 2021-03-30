package airhockey.model;


public class Model {
    private Board board;
    private Player[] players;
    private boolean hasPusherMoved = false;
    private static final int SCORE_MAX=7;

    private int counter = 0;

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

    public boolean isFinished(){
        return (hasWon(0) || hasWon(1));
    }

    public boolean hasWon(int numplayer){
        return players[numplayer].getScore()>=SCORE_MAX;
    }

    public int getCounter(){
        return counter;
    }

    public void update(double dt){
        board.update(dt);
        if(board.getPalet().getScoredGoal() != -1){
            counter++;
            if(counter > 30) reset();
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

    public void PusherReleased(int numplayer){
        this.board.getPushers()[numplayer].resetSpeed();
        hasPusherMoved = true;
    }

    public void setLocationPusher(double x, double y, double dt,int numplayer) {
        Pusher[] pushers = board.getPushers();
        pushers[numplayer].resetMovement();
        pushers[numplayer].moveTo(new Vector(x,y),board.getWalls(), board.getInvisibleWalls(), board.getPalet());
        pushers[numplayer].setSpeed(pushers[numplayer].getPosition().add(pushers[numplayer].getLastPosition().multiply(-1)).normalize().multiply(1.0/dt));
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
