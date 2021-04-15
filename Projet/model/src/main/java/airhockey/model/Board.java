package airhockey.model;

public class Board {

    private Palet palet;
    private Pusher[] pushers;
    private Wall[] walls;
    private Wall[] invisibleWalls;
    private Goal[] goals;

    public static final int WIDTH = 700;
    public static final int HEIGHT = 400;
    public static final int GOAL_SIZE = 125;


    public Board() {
        palet = new Palet(new Vector(WIDTH/2, HEIGHT/2), 18);

        pushers = new Pusher[2];
        pushers[0] = new Pusher(new Vector(200, 200), 30);
        pushers[1] = new Pusher(new Vector(600, 200), 30);

        walls = new Wall[6];
        walls[0] = new Wall(0, 0, WIDTH, 0); //mur haut
        walls[1] = new Wall(0, 0, 0, HEIGHT/2-GOAL_SIZE/2); //mur gauche 1
        walls[2] = new Wall(0,HEIGHT/2+GOAL_SIZE/2,0, HEIGHT/2-GOAL_SIZE/2); //mur gauche 2
        walls[3] = new Wall(WIDTH, 0, 0, HEIGHT/2-GOAL_SIZE/2); //mur droite 1
        walls[4] = new Wall(WIDTH, HEIGHT/2+GOAL_SIZE/2, 0, HEIGHT/2-GOAL_SIZE/2); //mur droite 2
        walls[5] = new Wall(0, HEIGHT, WIDTH, 0); //mur bas

        invisibleWalls = new Wall[3];
        invisibleWalls[0] = new Wall(0, HEIGHT/2-GOAL_SIZE/2, 0, GOAL_SIZE);
        invisibleWalls[1] = new Wall(WIDTH, HEIGHT/2-GOAL_SIZE/2, 0, GOAL_SIZE);
        invisibleWalls[2] = new Wall(WIDTH / 2, 0, 0, HEIGHT);

        goals = new Goal[2];
        goals[0] = new Goal(-200, HEIGHT/2-GOAL_SIZE,200, GOAL_SIZE*2); //goal gauche
        goals[1] = new Goal(WIDTH,HEIGHT/2-GOAL_SIZE,200, GOAL_SIZE*2); // goal droite
    }

    public int getWIDTH(){
        return WIDTH;
    }

    public int getHEIGHT(){
        return HEIGHT;
    }

    public Pusher[] getPushers() {
        return pushers;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public Wall[] getInvisibleWalls() {
        return invisibleWalls;
    }

    public Goal[] getGoals(){
        return goals;
    }

    public Palet getPalet() {
        return palet;
    }

    public void setPalet(Palet p){
        palet = p;
    }

    public void reset(int numPlayer){
        //pushers[0].setPosition(new Vector(200, 250));
        //pushers[1].setPosition(new Vector(700, 250));
        switch(numPlayer){
            case -1:
                palet.setPosition(new Vector(WIDTH/2, HEIGHT/2));
                break;
            case 0:
                Circle test0 = new Circle(new Vector(WIDTH/2-80, HEIGHT/2),18);
                Vector starting0 = pushers[0].isColliding(test0)?  new Vector(WIDTH/2-80-60, HEIGHT/2+60) : new Vector(WIDTH/2-80, HEIGHT/2);
                palet.setPosition(starting0);
                break;
            case 1:
                Circle test1 = new Circle(new Vector(WIDTH/2+80, HEIGHT/2),18);
                Vector starting1 = pushers[1].isColliding(test1)? new Vector(WIDTH/2+80+60, HEIGHT/2+60) : new Vector(WIDTH/2+80, HEIGHT/2);
                palet.setPosition(starting1);
                break;
        }

        palet.setSpeed(new Vector(0, 0));
    }


    public void update(double dt){
        palet.update(dt, walls, pushers, goals);
        pushers[0].resetMovement();
        pushers[1].resetMovement();
    }
}
