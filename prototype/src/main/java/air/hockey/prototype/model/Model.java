package air.hockey.prototype.model;

public class Model {
    private Palet palet;
    private Pusher[] pushers;
    private Wall[] walls;

    private final int WIDTH = 800;
    private final int HEIGHT = 500;

    private boolean hasPusherMoved = false;

    public Model() {
        palet = new Palet(new Vector(400, 250), 20);
        palet.setSpeed(new Vector(50, 70));

        pushers = new Pusher[2];
        pushers[0] = new Pusher(new Vector(300, 250), 25);
        pushers[1] = new Pusher(new Vector(600, 250), 25);
        walls = new Wall[4];
        walls[0] = new Wall(50, 50, WIDTH-100, 0);
        walls[1] = new Wall(50, 50, 0, HEIGHT-100);
        walls[2] = new Wall(WIDTH-50, 50, 0, HEIGHT-100);
        walls[3] = new Wall(50, HEIGHT-50, WIDTH-100, 0);
    }

    public void update(double dt){
        palet.update(dt, walls, pushers);
        pushers[0].resetMovement();
    }

    public Pusher[] getPushers() {
        return pushers;
    }

    public Palet getPalet() {
        return palet;
    }

    public void setPalet(Palet p){
        palet = p;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public void swapPushers(){
        Pusher tmp = pushers[0];
        pushers[0] = pushers[1];
        pushers[1] = tmp;
    }

    public boolean hasPusherMoved() {
        if(hasPusherMoved) {
            hasPusherMoved = false;
            return true;
        }
        return false;
    }

    public void setLocationPusher(double x, double y) {
        pushers[0].setPosition(new Vector(x, y));
        pushers[0].wallCollisions(walls);
        hasPusherMoved = true;
    }

    public void setLocationPalet(double x, double y) {
        palet.setPosition(new Vector(x, y));
    }

    public boolean isInPusher(double x, double y) {
        return pushers[0].getPosition().add(new Vector(x, y).multiply(-1)).length() < pushers[0].getRadius();
    }
}
