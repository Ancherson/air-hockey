package air.hockey.prototype.model;

import air.hockey.prototype.model.Vector;

import java.io.Serializable;

/* Creates the pusher of a player
 */

public class Pusher extends Circle implements Serializable {

    private Vector lastPosition;

    public Pusher(Vector position, double radius){
        super(position,radius);
        resetMovement();
    }

    public Vector getSpeed(double dt){
        return position.add(lastPosition.multiply(-1)).normalize().multiply(1./dt);
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
