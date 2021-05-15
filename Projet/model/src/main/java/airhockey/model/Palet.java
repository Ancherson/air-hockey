package airhockey.model;

import java.lang.Math.*;

/**
 * This class represents the palet
 */

public class Palet extends Circle {

    /**
     * A constant of the max speed
     */
    private final int MAX_SPEED = 1000;

    /**
     * A constant of the friction of the palet on the table
     */
    private final static double COEFF_FRICTION = 0.92;

    /**
     *A constant of the max angle speed
     */
    private final static double MAX_ANGLE_SPEED = 500;

    /**
     * A constant of the friction of the angle speed
     */
    private final static double COEFF_FRICTION_ANGLE = 0.9;

    /**
     * A constant, the bigger it is, the more the trajectory is curved
     */
    private final static double COEFF_CURVE = 6;

    /**
     * Speed of the palet
     */
    private Vector speed;

    /**
     * Checks in which goal the palet has been scored, is initialized at -1
     */
    private int scoredGoal = -1;

    /**
     * Checks if the palet has been in collision with a wall
     */    
    private boolean hasHit;

    /**
     * The position of the collision of the palet with a wall
     */
    private Vector hitPosition;

    /**
     * The normal of the position of the collsion of the palet with a wall
     */
    private Vector hitNormal;

    /**
     * Checks if the palet has been in collision with a pusher
     */
    public boolean collisionned;

    /**
     * The angle speed of the palet
     */
    private double angleSpeed = 0;

    /**
     * The angle of the palet
     */
    private double angle = 0;

    /**
     * Constructor of the palet
     * @param position the position of the palet
     * @param radius the radius of the palet
     */    
    public Palet(Vector position, double radius){
        super(position, radius);
        speed = new Vector(0,0);

        hasHit = false;
        hitPosition = new Vector(0, 0);
        hitNormal = new Vector(0, 0);
    }

    /**
     * Returns the speed of the palet
     * @return the speed of the palet
     */
    public Vector getSpeed(){
        return speed;
    }

    /**
     * Sets a new speed to the palet
     * @param v Vector of the new speed of the palet
     */
    public void setSpeed(Vector v){
        speed = v;
        if(speed.length() > MAX_SPEED) {
            speed = speed.normalize().multiply(MAX_SPEED);
        }
    }

    /**
     * Returns the angle of the palet
     * @return the angle of the palet
     */
    public double getAngle(){
        return angle;
    }

    /**
     * Sets a new angle to the palet
     * @param angle double of the new angle of the palet
     */
    public void setAngle(double angle){
        this.angle = angle;
    }

    /**
     * Returns the angle speed of the palet
     * @return the angle speed of the palet
     */
    public double getAngleSpeed(){
        return angleSpeed;
    }

    /**
     * Sets a new angle speed to the palet
     * @param vitesse double of the new angle speed of the palet
     */
    public void setAngleSpeed(double vitesse){
        this.angleSpeed = vitesse;
    }

    /**
     * Returns hasHit of the palet
     * @return hasHit of the palet
     */
    public boolean getHasHit(){
        return hasHit;
    }

    /**
     * Returns hitPosition of the palet
     * @return hitPosition of the palet
     */
    public Vector getHitPosition(){
        return hitPosition;
    }

    /**
     * Returns hitNormal of the palet
     * @return hitNomal of the palet
     */
    public Vector getHitNormal(){
        return hitNormal;
    }

    /**
     * Returns a description of the palet
     * @return a description of the palet : its position, radius and speed
     */
    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }

    /**
     * Returns a copy of the palet
     * @return a copy of the palet
     */
    public Palet copy() {
        Palet copy = new Palet(position.copy(), getRadius());
        copy.speed = this.speed;
        copy.scoredGoal = this.scoredGoal;
        copy.hasHit = this.hasHit;
        copy.hitNormal = this.hitNormal;
        copy.hitPosition = this.hitPosition;
        return copy;
    }

    /**
     * Changes the position, the speed and the angle speed of the palet if it's colliding with a wall
     * @param walls an array of Wall
     * @return if the palet has collided with one of the walls
     */
    public boolean wallCollisions(Wall[] walls){
        boolean hasCollided = false;
        for(Wall w : walls){
            if(isColliding(w)){
                //Calculates the new position of the palet if it has colllided a wall
                hasHit = true;
                hitPosition = w.closestPoint(position);
                hitNormal = position.sub(hitPosition).normalize();
                resolveCollision(w);

                /*Calculates the new speed of the palet
                The angle speed of the palet is lowered by 15%
                The energy of the palet is given by the difference between its old energy and its new energy
                Its new speed is calculated from the wall direction and multiply by its energy
                */
                speed = speed.reflection(w.getNormal(position));
                double oldEnergy = angleSpeed * angleSpeed * getRadius() * getRadius();
                angleSpeed *= 0.5;
                double diffEnergy = oldEnergy - angleSpeed * angleSpeed * getRadius() * getRadius();
                Vector wallDir = w.closestPoint(getPosition()).sub(getPosition()).normalize().getOrthogonal();
                speed = speed.add(wallDir.multiply(-Math.sqrt(2*diffEnergy)*.5));
                speed = speed.multiply(0.94);

                hasCollided = true;
            }
        }
        return hasCollided;
    }

    /**
     * Changes the position, the speed and the angle speed of the palet if it's colliding with a pusher
     * @param pushers an array of Pusher
     * @param walls an array of Wall
     * @param dt a double of the time
     * @return if the palet has collided with one of the pushers
     */
    public boolean pusherCollisions(Pusher[] pushers, Wall[] walls, double dt){
        boolean hasCollided = false;
        for(Pusher p : pushers){
            if(isColliding(p)){
                /*
                The angle speed of the palet is calculated by the interraction between it and the pusher,
                its angle speed is limited to (+/-)MAX_ANGLE_SPEED
                */
                Vector normal = position.sub(p.position).normalize();
                Vector orthogonal = normal.getOrthogonal();
                double angleSpeed = orthogonal.dotProduct(p.getSpeed().sub(speed))*(-0.1);
                setAngleSpeed(getAngleSpeed()+angleSpeed);

                if(Math.abs(this.angleSpeed) > MAX_ANGLE_SPEED){
                    this.angleSpeed = MAX_ANGLE_SPEED*this.angleSpeed/Math.abs(this.angleSpeed);

                }
                /* 
                The speed of the palet is calculated in this part
                The speed is limited by the coefficient MAX_SPEED
                */
                speed = normal.multiply(speed.length()).add(p.getSpeed());
                
                if(speed.length() > MAX_SPEED) {
                    speed = speed.normalize().multiply(MAX_SPEED);
                }
                
                /* 
                The new position of the palet is calculated in this part
                The speed is reduced by 4% with the friction
                */
                Circle newPosition = new Circle(new Vector(getPosition().getX(), getPosition().getY()), getRadius());
                newPosition.resolveCollision(p);
                moveTo(newPosition.getPosition(), walls);

                speed = speed.multiply(0.96);
                hasCollided = true;
                collisionned = true;
            }
        }
        return hasCollided;
    }

    /**
     * Sets scoredGoal to -1
     */
    public void resetScoredGoal(){
        scoredGoal = -1;
    }

    /**
     * Returns scoredGoal
     * @return scoredGoal of the palet
     */
    public int getScoredGoal(){
        return scoredGoal;
    }

    /**
     * Changes scoredGoal if a goal is scored
     * @param goals an array of Goal
     * @return if the palet has collided one of the goals
     */
    public boolean goalCollisions(Goal[] goals){
        for(int i = 0; i < goals.length; i++){
            if(goals[i].isInGoal(this)){
                scoredGoal = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Stops the direction taken of the palet if there is a collision with a Wall
     * @param p Vector of the direction of the palet
     * @param walls an array of Walls
     */
    public void moveTo(Vector p, Wall[] walls){
        /*
        Calculates the trajectory of the pusher
        */
        Vector distance = p.add(position.multiply(-1));
        Vector dir = distance.normalize();
        double length = distance.length();
        double step = getRadius()*0.5;
        Vector p0 = new Vector(position.getX(), position.getY());

        /* 
        Checks from the original position of the palet to the arrival, step by step, 
        if there is an object on the trajectory of the palet        
        */
        for(double l=step; l < length+step; l+=step){
            position = p0.add(dir.multiply(Math.min(l,length)));
            if(wallCollisions(walls)){
                break;
            }
        }
    }

    /**
     * Changes the position, the speed and the angle speed of the palet
     * @param dt double of the time
     * @param walls an array of Wall
     * @param pushers an array of Pusher
     * @param goals an array of Goal
     */
    public void update(double dt, Wall[] walls, Pusher[] pushers, Goal[] goals){
        hasHit = false;

        /* 
        Applies the friction on the speed of the palet
        */
        double friction = Math.pow(COEFF_FRICTION, dt);
        speed = speed.multiply(friction);

       /*Calculates the new angle speed
        The angle speed of the palet is lowered by 0.85^dt in order to have an angle speed time depedent
        The energy of the palet is given by the difference between its old energy and its new energy
        */
        double oldEnergy = angleSpeed * angleSpeed * getRadius() * getRadius();
        angleSpeed *= Math.pow(COEFF_FRICTION_ANGLE, Math.sqrt(dt));
        double diffEnergy = oldEnergy - angleSpeed * angleSpeed * getRadius() * getRadius();
        angle += angleSpeed*dt;

        /*
        Calculates the new speed of the palet which is time depedent and adds to it the transmission of energy
        */
        Vector v = speed.multiply(dt);
        Vector dir = v.normalize();
        /* 
        Calculates the trajectory of the palet
        */
        speed = speed.add(dir.getOrthogonal().multiply(Math.sqrt(COEFF_CURVE*diffEnergy)*2*dt));
        if(speed.length() > MAX_SPEED ) {
            speed = speed.normalize().multiply(MAX_SPEED);
        }
        double length = v.length();
        double step = getRadius()*0.5;
        Vector p0 = new Vector(position.getX(), position.getY());
        
        /* 
        Checks from the original position of the palet to the arrival, step by step, 
        if there is an object on the trajectory of the palet        
        */
        for(double l = step; l <= length+step; l += step){
            position = p0.add(dir.multiply(Math.min(l, length)));
            if(goalCollisions(goals) || wallCollisions(walls) || pusherCollisions(pushers, walls, dt)){
                break;
            }
        }

    }
}
