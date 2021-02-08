package air.hockey.prototype.model;

public class Palet extends Circle {

    private Vector speed;
    
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

    public void wallCollisions(Wall[] walls){
        for(Wall w : walls){
            if(isColliding(w)){
                resolveCollision(w);
                speed = speed.reflection(w.getNormal());
            }
        }
    }

    public void pusherCollisions(Pusher[] pushers, double dt){
        for(Pusher p : pushers){
            if(isColliding(p)){
                Vector normal = position.add(p.position.multiply(-1)).normalize();
                speed = speed.reflection(normal).add(p.getSpeed(dt));
                resolveCollision(p);
            }
        }
    }

    public void update(double dt, Wall[] walls, Pusher[] pushers){
        position = position.add(speed.multiply(dt));
        wallCollisions(walls);
        pusherCollisions(pushers, dt);
    }
}
