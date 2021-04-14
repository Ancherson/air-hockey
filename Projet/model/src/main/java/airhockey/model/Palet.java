package airhockey.model;

import java.lang.Math.*;

public class Palet extends Circle {

    private final int MAX_SPEED = 1000;
    private Vector speed;
    private final static double COEFF_FRICTION = 0.92;
    private int scoredGoal = -1;
    private boolean hasHit;
    private Vector hitPosition;
    private Vector hitNormal;
    public boolean collisionned;
    
    public Palet(Vector position, double radius){
        super(position, radius);
        speed = new Vector(0,0);

        hasHit = false;
        hitPosition = new Vector(0, 0);
        hitNormal = new Vector(0, 0);
    }

    public Vector getSpeed(){
        return speed;
    }

    public void setSpeed(Vector v){
        speed = v;
        if(speed.length() > MAX_SPEED) {
            speed = speed.normalize().multiply(MAX_SPEED);
        }
    }

    public boolean getHasHit(){
        return hasHit;
    }

    public Vector getHitPosition(){
        return hitPosition;
    }

    public Vector getHitNormal(){
        return hitNormal;
    }

    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }

    public Palet copy() {
        Palet copy = new Palet(position.copy(), getRadius());
        copy.speed = this.speed;
        copy.scoredGoal = this.scoredGoal;
        copy.hasHit = this.hasHit;
        copy.hitNormal = this.hitNormal;
        copy.hitPosition = this.hitPosition;
        return copy;
    }

    public boolean wallCollisions(Wall[] walls){
        boolean hasCollided = false;
        for(Wall w : walls){
            if(isColliding(w)){
                hasHit = true;
                hitPosition = w.closestPoint(position);
                hitNormal = position.sub(hitPosition).normalize();//w.getNormal(hitPosition);

                resolveCollision(w);
                speed = speed.reflection(w.getNormal(position));
                speed = speed.multiply(0.94);

                hasCollided = true;
            }
        }
        return hasCollided;
    }

    public boolean pusherCollisions(Pusher[] pushers, Wall[] walls, double dt){
        boolean hasCollided = false;
        for(Pusher p : pushers){
            if(isColliding(p)){
                Vector normal = position.sub(p.position).normalize();
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

    public void resetScoredGoal(){
        scoredGoal = -1;
    }

    public int getScoredGoal(){
        return scoredGoal;
    }

    public boolean goalCollisions(Goal[] goals){
        for(int i = 0; i < goals.length; i++){
            if(goals[i].isInGoal(this)){
                scoredGoal = i;
                return true;
            }
        }
        return false;
    }

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

    public void update(double dt, Wall[] walls, Pusher[] pushers, Goal[] goals){
        hasHit = false;
        speed = speed.multiply(Math.pow(COEFF_FRICTION, dt));

        Vector v = speed.multiply(dt);
        Vector dir = v.normalize();
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
