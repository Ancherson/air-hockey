package airhockey.model;

import airhockey.model.Vector;

import java.io.Serializable;

/**
 * Creates a Circle object
 */

public class Circle implements Serializable {

    protected Vector position;
    private double radius;

    /**
     * Constructor of a Circle
     * Creates a circle of center Vector position and of radius a double
     * @param position Vector of the center of the Circle
     * @param radius double of the radius of the Circle
     */
    public Circle(Vector position, double radius){
        this.position = position;
        this.radius = radius;
    }
    /**
     * Returns the position of the Circle
     * @return Vector of the position of the Circle
     */
    public Vector getPosition(){
        return position;
    }

    /**
     * Changes the position of the Circle
     * @param v Vector of the new position of the Circle
     */
    public void setPosition(Vector v){
        position = v;        
    }    

    /**
     * Returns the radius of the Circle
     * @return double radius of Circle
     */
    public double getRadius(){
        return radius;
    }

    /**
     * Describes the position and radius of the Circle
     * @return String of description of the Circle
     */
    public String toString(){
        return "Position:"+position+"\nRadius: "+radius;
    }

    /**
     * Calculates the closest point to the Circle on the Wall w
     * @param w Wall reference
     * @return Vector of the closest point to the Circle on the Wall
     */
    private Vector closestPoint(Wall w){
        Vector P = w.getPosition();
        Vector D = w.getDirection();
        //lambda is in [0, 1], lambda = 0 means that the closest point is on the start point of the wall, lambda = 1 means that the closest point is the end point of the wall
        double lambda = Math.max(0, Math.min(1, (position.add(P.multiply(-1)).dotProduct(D))/(D.dotProduct(D))));
        //by multiplying D by lambda and adding it to P we get the actual position of the point
        return P.add(D.multiply(lambda));
    }

    /**
     * Tests if there is a collision between the Circle and Circle in parameters
     * @param c Circle tested for the collision
     * @return boolean is true when there is a collision
     */
    public boolean isColliding(Circle c){
        return c.position.add(position.multiply(-1)).length()<radius+c.radius;
    }

    /**
     * Tests if there is a collision between the Circle and Wall in parameters
     * @param w Wall tested for the collision
     * @return boolean is true when there is a collision
     */
    public boolean isColliding(Wall w){
        return position.add(closestPoint(w).multiply(-1)).length()<radius;
    }

    /**
     * Resolves collision between the Circle and Circle in parameters
     * @param c Circle from which the position of the Circle is corrected
     */
    public void resolveCollision(Circle c){
        position = c.position.add(position.add(c.position.multiply(-1)).normalize().multiply(c.radius+radius));
    }

    /**
     * Resolves collision between the Circle and Wall in parameters
     * @param w Wall from which the position of the Circle is corrected
     */
    public void resolveCollision(Wall w){
        Vector cp = closestPoint(w);
        resolveCollision(new Circle(cp,0));
    }
}