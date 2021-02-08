package air.hockey.prototype.model;

import air.hockey.prototype.model.Vector;

/* Creates the pusher of a player
 */

public class Pusher extends Circle {

    private Vector speed;

    public Pusher(Vector position, double radius){
        super(position,radius);
        speed = new Vector(0,0);
    }

    public Vector getSpeed(){
        return speed;
    }

    public void setSpeed(Vector v){
        speed = v;
    }
    
    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }

    public void wallCollisions(Wall[] walls){
        for(Wall w : walls){
            if(isColliding(w)){
                resolveCollision(w);
            }
        }
    }
}
