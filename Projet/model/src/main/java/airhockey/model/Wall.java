package airhockey.model;

/**
 * Creates a Wall
 */
public class Wall {

    private Vector position;
    private Vector direction;

    /**
     * Constructor of a Wall
     * @param position Vector of the position of the Wall
     * @param direction Vector of the direction of the Wall
     */
    public Wall(Vector position, Vector direction){
        this.position=position;
        this.direction=direction;
    }

    /**
     * Constructor of a Wall
     * @param x double of the x position of its Vector position
     * @param y double of the y postition of its Vector position
     * @param dx double of the x position of its Vector direction
     * @param dy double of the y position of its Vector direction
     */
    public Wall(double x, double y, double dx, double dy){
        position = new Vector(x, y);
        direction = new Vector(dx, dy);
    }

    /**
     * Returns the position of the Wall
     * @return Vector of the position of the Wall
     */
    public Vector getPosition(){
        return position;
    }

    /**
     * Returns the direction of the Wall
     * @return Vector of the direction of the Wall
     */
    public Vector getDirection(){
        return direction;
    }

    /**
     * Returns the normal of the Wall's direction
     * @return Vector of the normal of the Wall's direction
     */
    public Vector getNormal(){
        Vector normal = new Vector(-direction.getY(),direction.getX());
        return normal.normalize();       
    }

    /**
     * Describes the position and the direction of the Wall
     * @return String description of the Wall's position and direction
     */
    public String toString(){
        return "Position: "+position+ "\nDirection: "+direction;
    }

}