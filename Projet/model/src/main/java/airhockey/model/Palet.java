package airhockey.model;

import java.lang.Math.*;

/**
* Creates a Palet which extends Circle
*/
public class Palet extends Circle {

    private Vector speed;
    private final static double COEFF_FRICTION = 0.92;

    /**
     * Constructor of a Palet
     * Creates a Palet of center Vector position, a Vector of the speed and a double of the radius
     * @param position Vector of the center of the Palet
     * @param radius double of the radius of the Palet
     */

    public Palet(Vector position, double radius){
        super(position, radius);
        speed = new Vector(0,0);
    }

    /**
     * Returns the speed of the Palet
     * @return Vector speed of the Palet
     */
    public Vector getSpeed(){
        return speed;
    }

    /**
     * Changes the speed of the Palet
     * @param v Vector of the new speed of the Palet
     */
    public void setSpeed(Vector v){
        speed = v;
    }

    /**
     * Describes the position, the radius and the speed of the Palet
     * @return String of description of the Palet
     */
    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }

    /**
     * Changes the position and the speed of the Palet when it collides a Wall
     * @param walls an array of Wall created in the model with which, is tested the collision of the Palet
     * and modifies its position and speed
     * @return boolean is true when the Palet collided one of the walls
     */
    public boolean wallCollisions(Wall[] walls){
        boolean hasCollided = false;
        for(Wall w : walls){
            if(isColliding(w)){
                resolveCollision(w);
                speed = speed.reflection(w.getNormal());
                speed = speed.multiply(0.94);
                hasCollided = true;
            }
        }
        return hasCollided;
    }

    /**
     * Changes the position and the speed of the Palet when it collides a Pusher
     * @param pushers an array of Pusher created in the model with which, is tested the collision of the Palet
     * and modifies its position and speed
     * @param dt double of time between two frames
     * @return boolean is true when the Palet collided one of the pushers
     */
    public boolean pusherCollisions(Pusher[] pushers, double dt){
        boolean hasCollided = false;
        for(Pusher p : pushers){
            if(isColliding(p)){
                Vector normal = position.add(p.position.multiply(-1)).normalize();
                speed = normal.multiply(speed.length()).add(p.getSpeed());
                resolveCollision(p);
                speed = speed.multiply(0.96);
                hasCollided = true;
            }
        }
        return hasCollided;
    }

    /**
     * Updates the position and the speed of the Palet between two frames
     * @param dt double of time between two frames
     * @param walls an array of Wall with which is tested the collision between the Palet and walls
     * @param pushers an array of Pusher with which is tested the collision between the Palet and pushers
     */
    public void update(double dt, Wall[] walls, Pusher[] pushers){
        speed = speed.multiply(Math.pow(COEFF_FRICTION, dt));

        Vector v = speed.multiply(dt);
        Vector dir = v.normalize();
        double length = v.length();
        double step = getRadius()*0.5;
        Vector p0 = new Vector(position.getX(), position.getY());

        for(double l = step; l <= length+step; l += step){
            position = p0.add(dir.multiply(Math.min(l, length)));
            if(wallCollisions(walls) || pusherCollisions(pushers, dt)){
                break;
            }
        }

    }
}
