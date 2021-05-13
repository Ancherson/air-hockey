package airhockey.model;

/**
 * This class represents the walls that are on the borders of the air hockey board
*/
public class Wall {
    /**
     * The position of the starting point of the wall
     */
    private Vector position;
    /**
     * The vector between the starting point and the ending point of the wall
     */
    private Vector direction;

    /**
     * The constructor of the wall
     * @param position the position of the starting point of the wall
     * @param direction the vector between the starting point and the ending point of the wall
     */
    public Wall(Vector position, Vector direction){
        this.position=position;
        this.direction=direction;
    }

    /**
     * Constructor that takes the coordinates of the vectors position and direction
     * @param x the x coordinate of the position vector
     * @param y the y coordinate of the position vector
     * @param dx the x coordinate of the direction vector
     * @param dy the y coordinate of the direction vector
     */
    public Wall(double x, double y, double dx, double dy){
        position = new Vector(x, y);
        direction = new Vector(dx, dy);
    }

    public Vector getPosition(){
        return position;
    }

    public Vector getDirection(){
        return direction;
    }

    /**
     * Returns the closest point to p on the wall
     * @param p a vector representing any point on the plane
     * @return the closest point to p on the wall
     */
    public Vector closestPoint(Vector p){
        //lambda is in [0, 1], lambda = 0 means that the closest point is on the start point of the wall, lambda = 1 means that the closest point is the end point of the wall
        double lambda = Math.max(0, Math.min(1, (p.add(position.multiply(-1)).dotProduct(direction))/(direction.dotProduct(direction))));
        //by multiplying D by lambda and adding it to P we get the actual position of the point
        return position.add(direction.multiply(lambda));
    }

    /**
     * Returns the normal vector of the wall near the position p
     * @param p the point at which we want to calculate the normal
     * @return the normal vector of the wall near the position p
     */
    public Vector getNormal(Vector p){
        //The normal is the normalized difference between p and the closest point to p on the wall
        Vector cp = closestPoint(p);
        return p.sub(cp).normalize();
    }

    public String toString(){
        return "Position: "+position+ "\nDirection: "+direction;
    }

}