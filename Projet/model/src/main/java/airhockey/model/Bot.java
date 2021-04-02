package airhockey.model;

public class Bot extends Player{
    private int score;
    private Vector speed;
    private final double ACCELERATION = 4000;
    private final double MAX_SPEED = 600;
    private final double FRICTION = 0.1;

    public Bot(){
        super();
        speed = new Vector(0, 0);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score=s;
    }

    public void moveTowards(Vector p, Pusher pusher, double dt){
        Vector d = p.sub(pusher.getPosition());
        d = d.normalize();
        speed = speed.add(d.multiply(ACCELERATION*dt));
        if(speed.length() > MAX_SPEED){
            speed = speed.normalize().multiply(MAX_SPEED);
        }
    }

    public Vector toCenter() {
        return new Vector(Board.WIDTH * 1.5 / 8, Board.HEIGHT / 2);
    }

    public Vector intercept(Model model) {
        Pusher myPusher = model.getBoard().getPushers()[0];
        Palet copy = model.getBoard().getPalet().copy();
        double minDist = Double.POSITIVE_INFINITY;
        Vector closestPos = copy.getPosition().copy();
        for(int i = 0; i < 30; i++) { //A REGLER LE NOMBRE DE PREVISIONS !
            copy.update(1.0 / 30, model.getBoard().getWalls(), model.getBoard().getPushers(), model.getBoard().getGoals());
            double dist = copy.getPosition().sub(myPusher.getPosition()).length();
            if(dist < minDist) {
                minDist = dist;
                closestPos = copy.getPosition().copy();
            }
        }
        return closestPos;
    }

    public Vector avoidAndIntercept(Pusher myPusher, Palet palet) {
        Vector dist = palet.getPosition().sub(myPusher.getPosition());
        Vector normalDist = new Vector(-dist.getY(), dist.getX());
        if(normalDist.getX() > 0) normalDist.multiply(-1);
        normalDist = normalDist.normalize().multiply(myPusher.getRadius() + palet.getRadius());
        return palet.getPosition().add(normalDist);
    }

    public Vector think(Model model) {
        Palet p = model.getBoard().getPalet();
        Pusher myPusher = model.getBoard().getPushers()[0];
        if((p.getSpeed().getX() >= 0 && p.getPosition().getX() > Board.WIDTH / 2)) {
            return toCenter();
        }
        if(p.getSpeed().getX() < 0) {
            if(p.getPosition().getX() > myPusher.getPosition().getX()) {
                return intercept(model);
            }else {
                return avoidAndIntercept(myPusher, p);
            }
        }
        if(p.getSpeed().getX() >= 0 && p.getPosition().getX() < Board.WIDTH / 2) {
            if(p.getSpeed().getX() > 100) return toCenter();
            else {
                return p.getPosition();
            }
        }
        return p.getPosition();
        /**
         * SI PALET S'ELOIGNE ET QU IL EST DE L'AUTRE COTE
         *        ON SE CENTRE
         * SI IL VIENT :
         *         SOIT IL EST A GAUCHE -> ON L'INTERCEPTE
         *         SOIT IL EST A DROITE -> ON SE RAMENE AU CAGE EN EVITANT LA TRAJECTOIRE DU PALET
         *
         * SI IL S'ELOIGNE ET QU IL EST DANS LE CAMPS:
         *          SI SA VITESSE EST SUPERIEUR A UNE CONSTANTE(le palet va assez vite vers le camps adverse)
         *                   ON SE RECENTRE
         *          SINON LE TAPPE EN VISANT DE UNE ZONE LIBRE
         */

    }

    public void update(Model model, double dt){
        //get the pusher and save its position
        Pusher p = model.getBoard().getPushers()[0];
        Vector lastPos = new Vector(p.getPosition().getX(), p.getPosition().getY());

        //change the speed (this is where the AI will make its decisions)
        //TODO ACTUAL AI LOGIC
        Vector target = think(model);
        if(target.sub(p.getPosition()).length() > p.getRadius() * 2) {
            moveTowards(target, p, dt);
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