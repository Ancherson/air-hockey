package airhockey.model;

import java.lang.Math.*;

public class Palet extends Circle {

    private Vector speed;
    private final static double COEFF_FRICTION = 0.92;
    
    public Palet(Vector position, double radius){
        super(position, radius);
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
