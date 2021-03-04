package airhockey.model;

import airhockey.model.Vector;

import java.io.Serializable;

/**
 * Creates a Pusher which extends Circle
 */

public class Pusher extends Circle implements Serializable {

    private Vector lastPosition;
    private Vector speed;

    /**
     * Constructor of a Pusher
     * Creates a Pusher of center Vector position, a Vector of the speed, a Vector of its last position and a double of the radius
     * @param position Vector of the center of the Pusher
     * @param radius double of the radius of the Pusher
     */
    public Pusher(Vector position, double radius){
        super(position,radius);
        resetMovement();
        speed = new Vector(0, 0);
    }

    /**
     * Returns the speed of the Pusher
     * @return Vector of the speed of the Pusher
     */
    public Vector getSpeed(){
        return speed;
    }

    /**
     * Changes the speed of the Pusher
     * @param s Vector of the new Vector speed
     */
    public void setSpeed(Vector s){
        speed = s;
    }

    /**
     * Returns the last position of the Pusher
     * @return Vector of the last position of the Pusher
     */
    public Vector getLastPosition(){
        return lastPosition;
    }

    /**
     * Changes the last position of the Pusher
     * @param p Vector of the new Vector last position
     */
    public void setLastPosition(Vector p){
        lastPosition = p;
    }

    /**
     * Changes the value of the last position with the actual position
     */
    public void resetMovement(){
        lastPosition = position;
    }
    
    /**
     * Describes the position, the radius and the last position of the Pusher
     * @return String of description of the Pusher
     */
    public String toString(){
        return super.toString()+"\nLastPosition: "+lastPosition;
    }

    /**
     * Changes the position the Pusher when it collides a Wall
     * @param walls an array of Wall created in the model with which, is tested the collision of the Pusher
     * and modifies its position
     * @return boolean is true when the Pusher collided one of the walls
     */
    public boolean wallCollisions(Wall[] walls){
        boolean hasCollided =false;
        for(Wall w : walls){
            if(isColliding(w)){
                resolveCollision(w);
                hasCollided = true;
            }
        }
        return hasCollided;
    }

    /**
     * Changes the position of the Pusher when it collides a Palet
     * @param p Palet with which, is tested the collision of the Pusher and modifies its position
     * @return boolean is true when the Pusher collided the Palet
     */
    public boolean paletCollision(Palet p){
        boolean hasCollided = false;
        if(isColliding(p)){
            resolveCollision(p);
            hasCollided = true;
        }
        return hasCollided;
    }

    /**
     * Moves the Pusher to the arrival position until it collides a Wall or a Palet
     * @param arrival Vector of arrival position of the pusher
     * @param walls an array of walls with which, is tested if the Pusher collides a Wall in its trajectory
     * @param p Palet with which, is tested if the Pusher collided the Palet in its trajectory
     */
    public void moveTo(Vector arrival, Wall[] walls, Palet p){
        Vector distance = arrival.add(position.multiply(-1));
        Vector dir = distance.normalize();
        double length = distance.length();
        double step = getRadius()*0.5;

        Vector p0 = new Vector(position.getX(), position.getY());

        for(double l=step; l < length+step; l+=step){
            position = p0.add(dir.multiply(Math.min(l,length)));
            if(paletCollision(p)||wallCollisions(walls)){
                break;
            }
        }

    }
}
