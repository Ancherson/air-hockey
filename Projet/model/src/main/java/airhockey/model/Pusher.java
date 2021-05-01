package airhockey.model;

import java.io.Serializable;

/* This class represents a pusher
 */

public class Pusher extends Circle implements Serializable {

    /**
     * The last position of the last position of the pusher
     */
    private Vector lastLastPosition;
   
    /**
     * The last position of the pusher
     */
    private Vector lastPosition;

    /**
     * The speed of the pusher
     */
    private Vector speed;

    /**
     * Constructor of the pushers
     * @param position position of pushers
     * @param radius radius of pushers
     */
    public Pusher(Vector position, double radius){
        super(position,radius);
        resetMovement();
        speed = new Vector(0, 0);
    }

    /**
     * Actualize the speed of the pusher
     * @param dt double of the time
     */
    public void actualizeSpeed (double dt) {
        Vector speed1 = position.sub(lastPosition);
        speed = speed1.multiply(0.2 / dt);
    }

    /**
     * Resets the speed of the pusher to 0
     */
    public void resetSpeed(){
        this.speed = new Vector(0,0);
    }

    /**
     * @param s Vector of the new speed of the pusher
     */
    public void setSpeed(Vector s){
        speed = s;
    }

    /**
     * @return the speed of the pusher
     */
    public Vector getSpeed(){
        return speed;
    }

    /**
     * @return the last position of the pusher
     */
    public Vector getLastPosition(){
        return lastPosition;
    }

    /**
     * @param p Vector of the new last position of the pusher
     */
    public void setLastPosition(Vector p){
        lastPosition = p;
    }

    /**
     * Changes the last last position of the pusher with its last position
     * Changes the last position of the pusher with its position
     */
    public void resetMovement(){
        lastLastPosition = lastPosition;
        lastPosition = position;
    }
    
    /**
     * @return the descritpion of the pusher : its position, radius and last position
     */
    public String toString(){
        return super.toString()+"\nLastPosition: "+lastPosition;
    }

    /**
     * Changes the position and the speed of the pusher if it's colliding with a wall
     * @param walls an array of Wall
     * @return if the pusher is colliding with one of the walls
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
     * Changes the position and the speed of the pusher if it's colliding with a palet
     * @param p Palet
     * @param walls an array of Wall
     * @return if the pusher is colliding with the palet
     */
    public boolean paletCollision(Palet p, Wall[] walls){
        boolean hasCollided = false;
        if(isColliding(p)){
            Vector normal = p.getPosition().sub(position).normalize();
            Vector orthogonal = normal.getOrthogonal();
            double angleSpeed = orthogonal.dotProduct(speed.sub(p.getSpeed()))*(-0.1);
            double coeffAngleSpeed = orthogonal.dotProduct(speed.sub(p.getSpeed()).normalize())*(-0.1);
            p.setAngleSpeed(p.getAngleSpeed()+angleSpeed);
            if(Math.abs(p.getAngleSpeed()) > 50){
                p.setAngleSpeed(50*p.getAngleSpeed()/Math.abs(p.getAngleSpeed()));
            }
            p.setSpeed(normal.multiply(p.getSpeed().length()*(1 - Math.abs(coeffAngleSpeed))).add(speed));
            Circle newPaletPosition = new Circle(new Vector(p.getPosition().getX(), p.getPosition().getY()), p.getRadius());
            newPaletPosition.resolveCollision(this);
            p.moveTo(newPaletPosition.getPosition(), walls);
            //p.setSpeed(p.getSpeed().multiply(0.96));
            hasCollided = true;
        }
        return hasCollided;
    }

    /**
     * Stop the direction taken of the pusher is it's colliding
     * @param arrival position of where the pusher is supposed to stop
     * @param walls an array of Wall
     * @param invisibleWalls an array of Wall
     * @param p Palet
     */
    public void moveTo(Vector arrival, Wall[] walls, Wall[] invisibleWalls,Palet p){
        Vector distance = arrival.add(position.multiply(-1));
        Vector dir = distance.normalize();
        double length = distance.length();
        double step = getRadius()*0.5;

        Vector p0 = new Vector(position.getX(), position.getY());

        for(double l=step; l < length+step; l+=step){
            position = p0.add(dir.multiply(Math.min(l,length)));
            if(paletCollision(p, walls)||wallCollisions(walls)||wallCollisions(invisibleWalls)){
                break;
            }
        }

    }


}
