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
     * Speed of the palet
     */
    private Vector speed;

    /**
     * A constant of the friction of the palet on the table
     */
    private final static double COEFF_FRICTION = 0.92;

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
     * @return the speed of the palet
     */
    public Vector getSpeed(){
        return speed;
    }

    /**
     * @param v Vector of the new speed of the palet
     */
    public void setSpeed(Vector v){
        speed = v;
        if(speed.length() > MAX_SPEED) {
            speed = speed.normalize().multiply(MAX_SPEED);
        }
    }

    /**
     * @return the angle of the palet
     */
    public double getAngle(){
        return angle;
    }

    /**
     * @param angle double of the new angle of the palet
     */
    public void setAngle(double angle){
        this.angle = angle;
    }

    /**
     * @return the angle speed of the palet
     */
    public double getAngleSpeed(){
        return angleSpeed;
    }

    /**
     * @param vitesse double of the new angle speed of the palet
     */
    public void setAngleSpeed(double vitesse){
        this.angleSpeed = vitesse;
    }

    /**
     * @return hasHit of the palet
     */
    public boolean getHasHit(){
        return hasHit;
    }

    /**
     * @return hitPosition of the palet
     */
    public Vector getHitPosition(){
        return hitPosition;
    }

    /**
     * @return hitNomal of the palet
     */
    public Vector getHitNormal(){
        return hitNormal;
    }

    /**
     * @return a description of the palet : its position, radius and speed
     */
    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }

    /**
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
                hasHit = true;
                hitPosition = w.closestPoint(position);
                hitNormal = position.sub(hitPosition).normalize();//w.getNormal(hitPosition);

                resolveCollision(w);
                speed = speed.reflection(w.getNormal(position));
                double oldEnergy = angleSpeed * angleSpeed * getRadius() * getRadius();
                angleSpeed *= 0.85;
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
                Vector normal = position.sub(p.position).normalize();
                Vector orthogonal = normal.getOrthogonal();
                double angleSpeed = orthogonal.dotProduct(p.getSpeed().sub(speed))*(-0.1);
                setAngleSpeed(getAngleSpeed()+angleSpeed);
                if(Math.abs(angleSpeed) > 50){
                    angleSpeed = 50*angleSpeed/Math.abs(angleSpeed);
                }
                speed = normal.multiply(speed.length()).add(p.getSpeed());
                if(speed.length() > MAX_SPEED) {
                    speed = speed.normalize().multiply(MAX_SPEED);
                }
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
        Vector distance = p.add(position.multiply(-1));
        Vector dir = distance.normalize();
        double length = distance.length();
        double step = getRadius()*0.5;

        Vector p0 = new Vector(position.getX(), position.getY());

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
        double friction = Math.pow(COEFF_FRICTION, dt);
        speed = speed.multiply(friction);
        double oldEnergy = angleSpeed * angleSpeed * getRadius() * getRadius();
        angleSpeed *= Math.pow(0.85, Math.sqrt(dt));
        double diffEnergy = oldEnergy - angleSpeed * angleSpeed * getRadius() * getRadius();
        angle += angleSpeed*dt;
        Vector v = speed.multiply(dt);
        Vector dir = v.normalize();
        speed = speed.add(dir.getOrthogonal().multiply(Math.sqrt(2*diffEnergy)*2*dt));
        double length = v.length();
        double step = getRadius()*0.5;
        Vector p0 = new Vector(position.getX(), position.getY());

        for(double l = step; l <= length+step; l += step){
            position = p0.add(dir.multiply(Math.min(l, length)));
            if(goalCollisions(goals) || wallCollisions(walls) || pusherCollisions(pushers, walls, dt)){
                break;
            }
        }

    }
}
