package airhockey.model;


public class Bot extends Player{
    private int score;
    private Vector speed;
    private final double ACCELERATION = 40000;
    private final double MAX_SPEED = 300;
    private final double MAX_SHOOTING_SPEED = 800;
    private final double FRICTION = 0.01;
    public static final Vector[] TARGETS = {new Vector(Board.WIDTH, -Board.HEIGHT/2), new Vector(Board.WIDTH, Board.HEIGHT/2), new Vector(Board.WIDTH, 3 * Board.HEIGHT/2)};
    private static final Vector[] DEVIATE_TARGETS = {new Vector(Board.WIDTH/8, 0) , new Vector(Board.WIDTH/8, Board.HEIGHT)};
    private Vector target;
    private Vector deviateTarget;
    private int wasShooting;

    public Bot(){
        super();
        speed = new Vector(0, 0);
        wasShooting = 0;
        target = TARGETS[1];
        deviateTarget = DEVIATE_TARGETS[0];
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score=s;
    }

    public void moveTowards(Vector p, Pusher pusher, double dt){
        Vector d = p.sub(pusher.getPosition());
        //speed = d.multiply(5);
        d = d.normalize();
        speed = speed.add(d.multiply(ACCELERATION*dt));
        if(wasShooting == 0 && speed.length() > MAX_SPEED){
            speed = speed.normalize().multiply(MAX_SPEED);
        }else if(wasShooting != 0 && speed.length() > MAX_SHOOTING_SPEED) {
            speed = speed.normalize().multiply(MAX_SHOOTING_SPEED);
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
            //Vector aim = aim(model, copy.getPosition(), new Vector(Board.WIDTH, -Board.HEIGHT/2));
            //double dist = aim.sub(myPusher.getPosition()).length();
            double dist = copy.getPosition().sub(myPusher.getPosition()).length();
            if(dist < minDist) {
                minDist = dist;
                //closestPos = aim.copy();
                closestPos = copy.getPosition().copy();
            }
        }
        //return closestPos;
        //Vector aimUp = aim(model, closestPos, new Vector(Board.WIDTH, -Board.HEIGHT/2));
        //Vector aimMiddle = aim(model, closestPos, new Vector(Board.WIDTH, Board.HEIGHT/2));
        //Vector aimDown = aim(model, closestPos, new Vector(Board.WIDTH,3 * Board.HEIGHT/2));

        //double distAimUp = aimUp.length();
        //double distAimMiddle = aimMiddle.length();
        //double distAimDown = aimDown.length();

        //if(distAimUp < distAimDown && distAimUp < distAimMiddle) return aimUp;
        //if(distAimDown < distAimMiddle) return aimDown;
        //target = new Vector(Board.WIDTH, Board.HEIGHT/2);
        //return aimMiddle;

        Pusher enemy = model.getBoard().getPushers()[1];
        if((enemy.getPosition().getY() > 2 * Board.HEIGHT / 3 && target == TARGETS[2])){
            target = TARGETS[(int)(Math.random() * 2)];
        }else if(enemy.getPosition().getY() < Board.HEIGHT / 3 && target == TARGETS[0]){
            target = TARGETS[(int)(Math.random() * 2 + 1)];
        }else if(enemy.getPosition().getY() > Board.HEIGHT / 3 && enemy.getPosition().getY() < 2 * Board.HEIGHT / 3 && target == TARGETS[1]){
            target = TARGETS[(int)(Math.random() * 2) * 2];
        }
        return aim(model, closestPos);
    }

    public Vector aim(Model model, Vector paletPos){
        Pusher myPusher = model.getBoard().getPushers()[0];
        Vector dirPaletGoal = target.sub(paletPos).normalize();
        model.getDEBUG_LINES().put(target, paletPos);
        Vector dirPusherPalet = paletPos.sub(myPusher.getPosition()).normalize();
        if(wasShooting == 0 && (model.getBoard().getPalet().getPosition().sub(paletPos).length() > model.getBoard().getPalet().getRadius()*.9 || dirPusherPalet.dotProduct(dirPaletGoal) < .80)){
            return paletPos.sub(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
        }
        if(wasShooting == 0) wasShooting = 5;
        return myPusher.getPosition().add(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
    }

    public Vector aimDeviate(Model model, Vector paletPos, Vector t){
        Pusher myPusher = model.getBoard().getPushers()[0];
        Vector dirPaletGoal = t.sub(paletPos).normalize();
        model.getDEBUG_LINES().put(t, paletPos);
        Vector dirPusherPalet = paletPos.sub(myPusher.getPosition()).normalize();
        if(wasShooting == 0 && (model.getBoard().getPalet().getPosition().sub(paletPos).length() > model.getBoard().getPalet().getRadius()*.9 || dirPusherPalet.dotProduct(dirPaletGoal) < .80)){
            return paletPos.sub(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
        }
        if(wasShooting == 0) wasShooting = 5;
        return paletPos.add(dirPaletGoal.multiply(model.getBoard().getPalet().getRadius() + myPusher.getRadius()*2));
    }

    public Vector avoidAndIntercept(Pusher myPusher, Palet palet) {
        Vector dist = palet.getPosition().sub(myPusher.getPosition());
        Vector normalDist = new Vector(-dist.getY(), dist.getX());
        if(normalDist.getX() > 0) normalDist.multiply(-1);
        normalDist = normalDist.normalize().multiply(myPusher.getRadius() + palet.getRadius());
        return palet.getPosition().add(normalDist);
    }

    public Vector avoid(Pusher myPusher, Palet palet){
        Vector orth = palet.getSpeed().normalize().getOrthogonal().multiply(myPusher.getRadius()+palet.getRadius()*1.5);
        Vector pos1 = myPusher.getPosition().add(orth);
        /*Vector pos2 = myPusher.getPosition().sub(orth);
        Vector middle = new Vector(Board.WIDTH/4, Board.HEIGHT/2);
        if(pos1.sub(middle).length() < pos2.sub(middle).length()){
            return pos1;
        }*/
        return pos1;
    }

    public Vector deviate(Model model, Pusher myPusher, Vector paletPos){
        if(wasShooting == 0) {
            deviateTarget = (myPusher.getPosition().getY() > paletPos.getY() ? DEVIATE_TARGETS[0] : DEVIATE_TARGETS[1]);
        }
        return aimDeviate(model, paletPos, deviateTarget);
    }


    public Vector avoidAndDeviate(Model model) {
        Pusher myPusher = model.getBoard().getPushers()[0];
        Palet palet = model.getBoard().getPalet();
        if(palet.getSpeed().normalize().dotProduct(myPusher.getPosition().sub(palet.getPosition()).normalize()) > 0.9){
            return avoid(myPusher, palet);
        }

        if(Math.abs(palet.getSpeed().getX()) < 20){
            return deviate(model, myPusher, palet.getPosition());
        }

        Palet copy = palet.copy();
        for(int i = 0; i < 60; i++) { //A REGLER LE NOMBRE DE PREVISIONS !
            copy.update(1.0 / 30, model.getBoard().getWalls(), model.getBoard().getPushers(), model.getBoard().getGoals());
        }
        if(copy.getScoredGoal() != -1){
            return deviate(model, myPusher, palet.getPosition());
        }

        return myPusher.getPosition();
    }

    /**
     *
     * 2 fonctions avoid and deviate
     *
     * on devie si au bout d'une seconde le palet rentre dans la cage
     * on avoid si elle vient vers le pusher
     * on fait rien sinon
     *
     * avoid :
     *    
     *
     */

    public Vector think(Model model) {
        Palet p = model.getBoard().getPalet();
        Pusher myPusher = model.getBoard().getPushers()[0];
        if((p.getSpeed().getX() >= 0 && p.getPosition().getX() > Board.WIDTH / 2)) {
            wasShooting = 0;
            return toCenter();
        }
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
        }else {
            return avoidAndDeviate(model);
        }

        if(p.getSpeed().getX() >= 0 && p.getPosition().getX() < Board.WIDTH / 2) {
           if(wasShooting > 0) {
               wasShooting--;
               return aim(model, p.getPosition());
           }else {
               if(p.getSpeed().getX() < 300) return p.getPosition();
               else return toCenter();
           }
        }
        wasShooting = 0;
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
        model.getDEBUG_POINTS().clear();
        model.getDEBUG_LINES().clear();

        //get the pusher and save its position
        Pusher p = model.getBoard().getPushers()[0];
        Vector lastPos = new Vector(p.getPosition().getX(), p.getPosition().getY());

        //change the speed (this is where the AI will make its decisions)
        //TODO ACTUAL AI LOGIC
        Vector v = think(model);
        model.getDEBUG_POINTS().add(v);
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