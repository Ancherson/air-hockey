package airhockey.model;

import airhockey.model.Vector;

import java.io.Serializable;

/* Creates a circle that can be the palette or the pusher
 */

public class Circle implements Serializable {

    protected Vector position;
    private double radius;

    public Circle(Vector position, double radius){
        this.position = position;
        this.radius = radius;
    }

    public Vector getPosition(){
        return position;
    }

    public void setPosition(Vector v){
        position = v;        
    }    

    public double getRadius(){
        return radius;
    }

    public String toString(){
        return "Position:"+position+"\nRadius: "+radius;
    }

    // calculates the closest point to the circle on the wall w
    private Vector closestPoint(Wall w){
        Vector P = w.getPosition();
        Vector D = w.getDirection();
        //lambda is in [0, 1], lambda = 0 means that the closest point is on the start point of the wall, lambda = 1 means that the closest point is the end point of the wall
        double lambda = Math.max(0, Math.min(1, (position.sub(P).dotProduct(D))/(D.dotProduct(D))));
        //by multiplying D by lambda and adding it to P we get the actual position of the point
        return P.add(D.multiply(lambda));
    }

    public boolean isColliding(Circle c){
        return c.position.sub(position).length()<radius+c.radius;
    }

    public boolean isColliding(Wall w){
        return position.sub(closestPoint(w)).length()<radius;
    }

    public void resolveCollision(Circle c){
        position = c.position.add(position.sub(c.position).normalize().multiply(c.radius+radius));
    }

    public void resolveCollision(Wall w){
        Vector cp = closestPoint(w);
        resolveCollision(new Circle(cp,0));
    }
}