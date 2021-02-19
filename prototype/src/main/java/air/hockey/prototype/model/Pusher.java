package air.hockey.prototype.model;

import air.hockey.prototype.model.Vector;

import java.io.Serializable;

/* Creates the pusher of a player
 */

public class Pusher extends Circle implements Serializable {

    private Vector lastPosition;
    private Vector speed;

    public Pusher(Vector position, double radius){
        super(position,radius);
        resetMovement();
        speed = new Vector(0, 0);
    }

    public void setSpeed(Vector s){
        speed = s;
    }
    public Vector getSpeed(){
        return speed;
    }

    public Vector getLastPosition(){
        return lastPosition;
    }

    public void setLastPosition(Vector p){
        lastPosition = p;
    }

    public void resetMovement(){
        lastPosition = position;
    }
    
    public String toString(){
        return super.toString()+"\nLastPosition: "+lastPosition;
    }

    public void wallCollisions(Wall[] walls){
        for(Wall w : walls){
            if(isColliding(w)){
                resolveCollision(w);
            }
        }
    }

    public void paletCollision(Palet p){
        if(isColliding(p)){
            resolveCollision(p);
        }
    }
}
