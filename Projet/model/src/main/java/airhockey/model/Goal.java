package airhockey.model;

/**
 * This class represents the goals at both sides of the board
 * <br>The goals are represented by a rectangular hitbox
 */
public class Goal{
    /**
     * The first corner's position of the goal
     */
    private Vector position;
    /**
     * The diagonal vector of the goal (its coordinates are the goal's width and height)
     */
    private Vector size;

    /**
     * The constructor of the goal
     * @param p the first corner's position of the goal
     * @param s the diagonal vector of the goal (its coordinates are the goal's width and height)
     */
    public Goal(Vector p, Vector s){
        position = p;
        size = s;
    }

    /**
     * Constructor that takes the coordinates of the vectors position and size
     * @param x the x coordinate of the position vector
     * @param y the y coordinate of the position vector
     * @param width the width of the goal
     * @param height the height of the goal
     */
    public Goal(double x, double y, double width, double height){
        position = new Vector(x, y);
        size = new Vector(width, height);
    }

    /**
     * Returns a description of the goal
     * @return String of a description of the goal : its position and its size
     */
    public String toString(){
        return "Position: "+position+", Size: "+size;
    }

    /**
     * Checks if the circle in parameter is entirely in the goal
     * @param c the circle to check whether it is in the goal
     * @return true if the circle c is in the goal
     */
    public boolean isInGoal(Circle c){
        double x = c.getPosition().getX();
        double y = c.getPosition().getY();
        double r = c.getRadius();
        //checks if no part of the circle is outside of the goal, or more mathematically if the intersection of the goal and c is equal to c
        return  x-r >= position.getX() && x+r <= position.getX()+size.getX() && y-r >= position.getY() && y+r <= position.getY()+size.getY();
    }
}