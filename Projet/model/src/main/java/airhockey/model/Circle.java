package airhockey.model;

import airhockey.model.Vector;

import java.io.Serializable;

/**
 *  This class represents a circle that can be a pusher or a palet
 */


public class Circle implements Serializable {
    /**
     * Position of the circle
     */
    protected Vector position;

    /**
     * Radius of the circle
     */
    private double radius;


    /**
     * Constructor of the circles
     * @param position the position of circles
     * @param radius the radius of circles
     */
    public Circle(Vector position, double radius){
        this.position = position;
        this.radius = radius;
    }

    /**
     * Returns the position of the circle
     * @return the position of the circle
     */
    public Vector getPosition(){
        return position;
    }

    /**
     * Sets a new position
     * @param v Vector of the new position of the circle
     */
    public void setPosition(Vector v){
        position = v;        
    }    

    /**
     * Returns the radisu of the circle
     * @return the radius of the circle
     */
    public double getRadius(){
        return radius;
    }

    /**
     * Returns a description of the circle
     * @return a the description of a circle : its position and its radius
     */
    public String toString(){
        return "Position:"+position+"\nRadius: "+radius;
    }

    /**
     * @param w a Wall 
     * @return the closest point to the circle on the Wall w
     */
    private Vector closestPoint(Wall w){
        Vector P = w.getPosition();
        Vector D = w.getDirection();
        //lambda is in [0, 1], lambda = 0 means that the closest point is on the start point of the wall, lambda = 1 means that the closest point is the end point of the wall
        double lambda = Math.max(0, Math.min(1, (position.sub(P).dotProduct(D))/(D.dotProduct(D))));
        //by multiplying D by lambda and adding it to P we get the actual position of the point
        return P.add(D.multiply(lambda));
    }

    /**
     * Checks if the circle is colliding with the circle in parameter
     * @param c a Circle
     * @return if the circle is colliding with the Circle c 
     */
    public boolean isColliding(Circle c){
        return c.position.sub(position).length()<radius+c.radius;
    }

    /**
     * Checks if the circle is colliding with the wall in parameter
     * @param w a Wall
     * @return if the circle is colliding with the Wall w
     */
    public boolean isColliding(Wall w){
        return position.sub(closestPoint(w)).length()<radius;
    }

    /**
     * Resolves the collision with the circle in parameter
     * @param c a Circle with whom the collision is resolved
     */
    public void resolveCollision(Circle c){
        position = c.position.add(position.sub(c.position).normalize().multiply(c.radius+radius));
    }

    /**
     * Resolves the collision with the wall in parameter
     * @param w a Wall with whom the collision is resolved
     */
    public void resolveCollision(Wall w){
        Vector cp = w.closestPoint(position);
        resolveCollision(new Circle(cp,0));
    }
}