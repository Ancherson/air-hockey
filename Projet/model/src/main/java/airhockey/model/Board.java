package airhockey.model;
/**
 * This class represents the board of the game with the differents elements with which the player can play
 */
public class Board {

    /**
     * A palet
     */
    private Palet palet;

    /**
     * The pusher of each player
     */
    private Pusher[] pushers;

    /**
     * The walls delimiting the board
     */
    private Wall[] walls;

    /**
     * Invisble walls that only the palet can cross
     */
    private Wall[] invisibleWalls;

    /**
     * The goal of each player
     */
    private Goal[] goals;

    /**
     * The width of the board
     */
    public static final int WIDTH = 700;

    /**
     * The height of the board
     */
    public static final int HEIGHT = 400;

    /**
     * The size of the goals
     */
    public static final int GOAL_SIZE = 125;


    /**
     * Constructor of the board
     * Contains one palet, two pushers, 6 walls, 3 invisble walls and 2 goals
     */
    public Board() {
        palet = new Palet(new Vector(WIDTH/2, HEIGHT/2), 18);

        pushers = new Pusher[2];
        pushers[0] = new Pusher(new Vector(200, 200), 30); //left player pusher
        pushers[1] = new Pusher(new Vector(600, 200), 30); //right player pusher

        walls = new Wall[6];
        walls[0] = new Wall(0, 0, WIDTH, 0); //top wall
        walls[1] = new Wall(0, 0, 0, HEIGHT/2-GOAL_SIZE/2); //left wall 1
        walls[2] = new Wall(0,HEIGHT/2+GOAL_SIZE/2,0, HEIGHT/2-GOAL_SIZE/2); //left wall 2
        walls[3] = new Wall(WIDTH, 0, 0, HEIGHT/2-GOAL_SIZE/2); //right wall 1
        walls[4] = new Wall(WIDTH, HEIGHT/2+GOAL_SIZE/2, 0, HEIGHT/2-GOAL_SIZE/2); //right wall 2
        walls[5] = new Wall(0, HEIGHT, WIDTH, 0); //bottom wall

        invisibleWalls = new Wall[3];
        invisibleWalls[0] = new Wall(0, HEIGHT/2-GOAL_SIZE/2, 0, GOAL_SIZE); //invisible wall in the left goal
        invisibleWalls[1] = new Wall(WIDTH, HEIGHT/2-GOAL_SIZE/2, 0, GOAL_SIZE); //invisible wall in the right goal
        invisibleWalls[2] = new Wall(WIDTH / 2, 0, 0, HEIGHT); //invisible wall in the center of the board

        goals = new Goal[2];
        goals[0] = new Goal(-200, HEIGHT/2-GOAL_SIZE,200, GOAL_SIZE*2); //left goal
        goals[1] = new Goal(WIDTH,HEIGHT/2-GOAL_SIZE,200, GOAL_SIZE*2); // right goal
    }

    /**
     * @return int of the WIDTH of the board
     */
    public int getWIDTH(){
        return WIDTH;
    }

    /**
     * @return int of the HEIGHT of the board
     */
    public int getHEIGHT(){
        return HEIGHT;
    }

    /**
     * @return an array of the two pushers
     */
    public Pusher[] getPushers() {
        return pushers;
    }

    /**
     * @return an array of Wall
     */
    public Wall[] getWalls() {
        return walls;
    }

    /**
     * @return an array of invisible Wall
     */
    public Wall[] getInvisibleWalls() {
        return invisibleWalls;
    }

    /**
     * @return an array of the two goals
     */
    public Goal[] getGoals(){
        return goals;
    }

    /**
     * @return the Palet
     */
    public Palet getPalet() {
        return palet;
    }

    /**
     * @param p Palet of the new palet in the board
     */
    public void setPalet(Palet p){
        palet = p;
    }

    /**
     * Resets the position of the palet, its speed and its angle speed
     * At the center of the board at the start of the game
     * At the side of the goal has been scored
     * @param numPlayer the number of the player to know in which he's located
     */
    public void reset(int numPlayer){
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
        palet.setAngleSpeed(0);
    }

    /**
     * Updates the position of the palet and the two pushers relating to the time dt
     * @param dt double of the time
     */
    public void update(double dt){
        palet.update(dt, walls, pushers, goals);
    }
}
