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

    public boolean paletCollision(Palet p){
        boolean hasCollided = false;
        if(isColliding(p)){
            //Vector normal = p.getPosition().add(position.multiply(-1)).normalize();
            //p.setSpeed(normal.multiply(p.getSpeed().length()).add(speed));
            resolveCollision(p);
            //p.setSpeed(p.getSpeed().multiply(0.96));
            hasCollided = true;
        }
        return hasCollided;
    }

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

    public void resetSpeed(){
        this.speed = new Vector(0,0);
    }
}