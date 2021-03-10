package airhockey.model;

public class Model {
    private Board board;
    private boolean hasPusherMoved = false;

    public Model() {
        board = new Board();
    }

    public void update(double dt){
        board.update(dt);
    }

    public boolean hasPusherMoved() {
        if(hasPusherMoved) {
            hasPusherMoved = false;
            return true;
        }
        return false;
    }

    public void setLocationPusher(double x, double y, double dt,int numplayer) {
        pushers[numplayer].resetMovement();
        pushers[numplayer].moveTo(new Vector(x,y),walls,palet);
        pushers[numplayer].setSpeed(pushers[numplayer].getPosition().add(pushers[numplayer].getLastPosition().multiply(-1)).normalize().multiply(1.0/dt));
        pushers[numplayer].wallCollisions(walls);
        hasPusherMoved = true;
    }

    public void setLocationPalet(double x, double y) {
        palet.setPosition(new Vector(x, y));
    }

    public boolean isInPusher(double x, double y, int numplayer) {
        return pushers[numplayer].getPosition().add(new Vector(x, y).multiply(-1)).length() < pushers[numplayer].getRadius();
    }
}
