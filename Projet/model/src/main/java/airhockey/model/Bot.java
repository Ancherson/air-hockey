package airhockey.model;

/**
 * This class represents the AI, during the training mode
 */
public class Bot extends Player{
    /**
     * His score
     */
    private int score;
    /**
     * speed of the Bot
     */
    private Vector speed;

    /**
     * Constants that are used to move the Bot
     */
    private final double ACCELERATION = 40000;
    private final double MAX_SPEED = 300;
    private final double MAX_SHOOTING_SPEED = 800;
    private final double FRICTION = 0.01;

    /**
     * This is the targets of the AI when The AI is in attack mode
     */
    public static final Vector[] TARGETS = {new Vector(Board.WIDTH, -Board.HEIGHT/2), new Vector(Board.WIDTH, Board.HEIGHT/2), new Vector(Board.WIDTH, 3 * Board.HEIGHT/2)};
    private static final Vector[] DEVIATE_TARGETS = {new Vector(Board.WIDTH/8, 0) , new Vector(Board.WIDTH/8, Board.HEIGHT)};

    /**
     * To store the current target
     */
    private Vector target;
    private Vector deviateTarget;

    /**
     * If he is in shooting mode
     */
    private int wasShooting;

    /**
     * Constructor
     */
    public Bot(){
        super();
        speed = new Vector(0, 0);
        wasShooting = 0;
        target = TARGETS[1];
        deviateTarget = DEVIATE_TARGETS[0];
    }

    /**
     * Returns the score
     * @return the score
     */
    public int getScore(){
        return score;
    }

    /**
     * Modifies the score
     * @param s the new score
     */
    public void setScore(int s){
        score=s;
    }

    /**
     * The bot moves in the direction of the point p
     * @param p the point that the Bot wants to target
     * @param pusher AI's pusher
     * @param dt delta time
     */
    public void moveTowards(Vector p, Pusher pusher, double dt){
        Vector d = p.sub(pusher.getPosition());
        d = d.normalize();
        speed = speed.add(d.multiply(ACCELERATION*dt));
        if(wasShooting == 0 && speed.length() > MAX_SPEED){
            speed = speed.normalize().multiply(MAX_SPEED);
        }else if(wasShooting != 0 && speed.length() > MAX_SHOOTING_SPEED) {
            speed = speed.normalize().multiply(MAX_SHOOTING_SPEED);
        }
    }

    /**
     * Function to target the center of his board part
     * @return the position of the center
     */
    public Vector toCenter() {
        return new Vector(Board.WIDTH * 1.5 / 8, Board.HEIGHT / 2);
    }

    /**
     * Function that allows the AI to intercept the palet
     * @param model of the game
     * @return the point to target
     */
    public Vector intercept(Model model) {
        //Calcule the closest position of the palet during 30 steps
        Pusher myPusher = model.getBoard().getPushers()[0];
        Palet copy = model.getBoard().getPalet().copy();
        double minDist = Double.POSITIVE_INFINITY;
        Vector closestPos = copy.getPosition().copy();
        for(int i = 0; i < 30; i++) {
            copy.update(1.0 / 30, model.getBoard().getWalls(), model.getBoard().getPushers(), model.getBoard().getGoals());
            double dist = copy.getPosition().sub(myPusher.getPosition()).length();
            if(dist < minDist) {
                minDist = dist;
                closestPos = copy.getPosition().copy();
            }
        }

        //Based on the position of the enemy pusher, we decide which target to target
        Pusher enemy = model.getBoard().getPushers()[1];
        if((enemy.getPosition().getY() > 2 * Board.HEIGHT / 3 && target == TARGETS[2])){
            target = TARGETS[(int)(Math.random() * 2)];
        }else if(enemy.getPosition().getY() < Board.HEIGHT / 3 && target == TARGETS[0]){
            target = TARGETS[(int)(Math.random() * 2 + 1)];
        }else if(enemy.getPosition().getY() > Board.HEIGHT / 3 && enemy.getPosition().getY() < 2 * Board.HEIGHT / 3 && target == TARGETS[1]){
            target = TARGETS[(int)(Math.random() * 2) * 2];
        }
        //return the point to target
        return aim(model, closestPos);
    }

    /**
     * Function that returns the best point to target to shoot the palet
     * @param model of the game
     * @param paletPos the palet position
     * @return the point to target
     */
    public Vector aim(Model model, Vector paletPos){

        Pusher myPusher = model.getBoard().getPushers()[0];
        Vector dirPaletGoal = target.sub(paletPos).normalize();
        model.getDEBUG_LINES().put(target, paletPos);
        Vector dirPusherPalet = paletPos.sub(myPusher.getPosition()).normalize();
        //If the Bot is not aligned in the direction of the target, and the AI is not close enough, AI targets a point behind paletPos
        if(wasShooting == 0 && (model.getBoard().getPalet().getPosition().sub(paletPos).length() > model.getBoard().getPalet().getRadius()*.9 || dirPusherPalet.dotProduct(dirPaletGoal) < .80)){
            return paletPos.sub(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
        }
        //Shoot the pusher
        if(wasShooting == 0) wasShooting = 5;
        return myPusher.getPosition().add(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
    }

    /**
     * Function that returns the best point to target to deviate the palet
     * @param model of the game
     * @param paletPos position of the palet
     * @param t deviate target
     * @return return the point to target
     */
    public Vector aimDeviate(Model model, Vector paletPos, Vector t){
        Pusher myPusher = model.getBoard().getPushers()[0];
        Vector dirPaletGoal = t.sub(paletPos).normalize();
        model.getDEBUG_LINES().put(t, paletPos);
        Vector dirPusherPalet = paletPos.sub(myPusher.getPosition()).normalize();
        //If the Bot is not aligned in the direction of the target, and the AI is not close enough, AI targets a point behind paletPos
        if(wasShooting == 0 && (model.getBoard().getPalet().getPosition().sub(paletPos).length() > model.getBoard().getPalet().getRadius()*.9 || dirPusherPalet.dotProduct(dirPaletGoal) < .80)){
            return paletPos.sub(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
        }
        //Shoot the palet
        if(wasShooting == 0) wasShooting = 5;
        return paletPos.add(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
    }

    /**
     * Function that allows the Bot to avoid the palet
     * @param myPusher AI's pusher
     * @param palet of the game
     * @return the point to target to avoid the palet
     */
    public Vector avoid(Pusher myPusher, Palet palet){
        Vector orth = palet.getSpeed().normalize().getOrthogonal().multiply(myPusher.getRadius()+palet.getRadius()*1.5);
        return myPusher.getPosition().add(orth);
    }

    /**
     * Function that returns the point to deviate the palet
     * @param model of the game
     * @param myPusher AI's pusher
     * @param paletPos position of the palet
     * @return the point to target
     */
    public Vector deviate(Model model, Pusher myPusher, Vector paletPos){
        if(wasShooting == 0) {
            deviateTarget = (myPusher.getPosition().getY() > paletPos.getY() ? DEVIATE_TARGETS[0] : DEVIATE_TARGETS[1]);
        }
        return aimDeviate(model, paletPos, deviateTarget);
    }


    /**
     * Returns the point to target to avoid or deviate the palet
     * @param model of the game
     * @return the point to target
     */
    public Vector avoidAndDeviate(Model model) {
        Pusher myPusher = model.getBoard().getPushers()[0];
        Palet palet = model.getBoard().getPalet();
        //If the palet goes to the pusher, AI needs to avoid it
        if(palet.getSpeed().normalize().dotProduct(myPusher.getPosition().sub(palet.getPosition()).normalize()) > 0.9){
            return avoid(myPusher, palet);
        }

        //If the palet is slow, AI deviate it
        if(Math.abs(palet.getSpeed().getX()) < 20){
            return deviate(model, myPusher, palet.getPosition());
        }

        //If in a near future, the palet goes to the goal, AI needs to deviate it
        Palet copy = palet.copy();
        for(int i = 0; i < 60; i++) {
            copy.update(1.0 / 30, model.getBoard().getWalls(), model.getBoard().getPushers(), model.getBoard().getGoals());
        }
        if(copy.getScoredGoal() != -1){
            return deviate(model, myPusher, palet.getPosition());
        }
        //Otherwise AI must not move
        return myPusher.getPosition();
    }

    /**
     * This is where the AI make his decision, this is where the AI chooses the point to target
     * @param model of the game
     * @return the point to target
     */
    public Vector think(Model model) {
        Palet p = model.getBoard().getPalet();
        Pusher myPusher = model.getBoard().getPushers()[0];
        //IF the palet moves in the direction of the enemy goal in the enemy's board part
        //AI goes to the center of his board part
        if((p.getSpeed().getX() >= 0 && p.getPosition().getX() > Board.WIDTH / 2)) {
            wasShooting = 0;
            return toCenter();
        }
        //If the palet is in front of his pusher and the palet is going to the AI's board part
        //AI needs to intercept the palet
        if(p.getPosition().getX() > myPusher.getPosition().getX()) {
            if(p.getSpeed().getX() < 0) {
                Palet copy = model.getBoard().getPalet().copy();
                for (int i = 0; i < 5; i++) {
                    copy.update(1.0 / 30, model.getBoard().getWalls(), model.getBoard().getPushers(), model.getBoard().getGoals());
                }
                if (copy.getPosition().getX() < myPusher.getPosition().getX()) {
                    return p.getPosition();
                }
                return intercept(model);
            }
        }
        //If the palet is behind the AI's pusher we need to :
        //- deviate the palet if in a near future, the palet enters the goal
        //- just avoid the palet otherwise
        else {
            return avoidAndDeviate(model);
        }

        //If the palet is going to the enemy's board part and the palet is in the AI's board part
        if(p.getSpeed().getX() >= 0 && p.getPosition().getX() < Board.WIDTH / 2) {
            //If the AI is in shooting mode, we continue to shoot
            if(wasShooting > 0) {
               wasShooting--;
               return aim(model, p.getPosition());
           }
            //Else, we look the speed of the palet,
            // if the palet is slow, AI targets the palet, else AI targets the center of his board part
            else {
               if(p.getSpeed().getX() < 300) return p.getPosition();
               else return toCenter();
           }
        }
        wasShooting = 0;
        return p.getPosition();
    }

    /**
     * Function that updates position of the AI
     * @param model of the game
     * @param dt delta time
     */
    public void update(Model model, double dt){
        model.getDEBUG_POINTS().clear();
        model.getDEBUG_LINES().clear();

        //get the pusher and save its position
        Pusher p = model.getBoard().getPushers()[0];
        Vector lastPos = new Vector(p.getPosition().getX(), p.getPosition().getY());

        //change the speed (this is where the AI will make its decisions)
        //Choose a point to target
        Vector v = think(model);
        model.getDEBUG_POINTS().add(v);

        //And move in direction of this point
        if(v.sub(p.getPosition()).length() > p.getRadius() * 1.2) {
            moveTowards(v, p, dt);
        }else {
            speed = speed.multiply(Math.pow(FRICTION, dt));
        }




        //apply movement to the pusher
        Vector position = p.getPosition().add(speed.multiply(dt));

        //resolve collisions, hit the palet...
        p.moveTo(position,model.getBoard().getWalls(), model.getBoard().getInvisibleWalls(), model.getBoard().getPalet());
        p.wallCollisions(model.getBoard().getWalls());
        p.wallCollisions(model.getBoard().getInvisibleWalls());

        //set the speed according to the actual distance moved
        speed = p.position.sub(lastPos).multiply(1/dt);
        p.setSpeed(speed);
    }
}