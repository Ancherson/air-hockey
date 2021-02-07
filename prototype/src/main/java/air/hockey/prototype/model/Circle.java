package air.hockey.prototype.model;

import air.hockey.prototype.model.Vector;

/* Creates a circle that can be the palette or the pusher
 */

public class Circle{

    private Vector position;
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

    private Vector closestPoint(Wall w){
        Vector P = w.getPosition();
        Vector D = w.getDirection();
        double lambda = (position.add(P.multiply(-1)).dotProduct(D))/(D.dotProduct(D));
        return P.add(D.multiply(lambda));
    }

    public boolean isColliding(Circle c){
        return c.position.add(position.multiply(-1)).length()<radius+c.radius;
    }

    public boolean isColliding(Wall w){
        /*Vector distance = position.add(w.getPosition().multiply(-1));
        Vector direction = w.getDirection();
        double h = Math.max(0, Math.min(1, distance.dotProduct(direction)/direction.dotProduct(direction)));
        return distance.add(direction.multiply(-h)).length()<radius;*/
        return position.add(closestPoint(w).multiply(-1)).length()<radius;
    }

    public void resolveCollision(Circle c){
        position = c.position.add(position.add(c.position.multiply(-1)).normalize().multiply(c.radius+radius));
    }

    public void resolveCollision(Wall w){
        Vector cp = closestPoint(w);
        resolveCollision(new Circle(cp,0));
    }

}