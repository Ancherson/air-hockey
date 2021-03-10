package airhockey.model;

public class Model {
    private Board board;
    private boolean hasPusherMoved = false;

    public Model() {
        board = new Board();
    }

    public Board getBoard(){
        return board;
    }

    public void update(double dt){
        board.update(dt);
        if(board.getPalet().getScoredGoal() != -1){
            board.reset(board.getPalet().getScoredGoal());
            board.getPalet().resetScoredGoal();
        }
    }

    public boolean hasPusherMoved() {
        if(hasPusherMoved) {
            hasPusherMoved = false;
            return true;
        }
        return false;
    }

    public void setLocationPusher(double x, double y, double dt,int numplayer) {
        Pusher[] pushers = board.getPushers();
        pushers[numplayer].resetMovement();
        pushers[numplayer].moveTo(new Vector(x,y),board.getWalls(),board.getPalet());
        pushers[numplayer].setSpeed(pushers[numplayer].getPosition().add(pushers[numplayer].getLastPosition().multiply(-1)).normalize().multiply(1.0/dt));
        pushers[numplayer].wallCollisions(board.getWalls());
        hasPusherMoved = true;
    }

    public void setLocationPalet(double x, double y) {
        board.getPalet().setPosition(new Vector(x, y));
    }

    public boolean isInPusher(double x, double y, int numplayer) {
        return board.getPushers()[numplayer].getPosition().add(new Vector(x, y).multiply(-1)).length() < board.getPushers()[numplayer].getRadius();
    }
}
