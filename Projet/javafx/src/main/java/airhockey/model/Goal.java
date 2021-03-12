package airhockey.model;

public class Goal{
    private Vector position;
    private Vector size;

    public Goal(Vector p, Vector s){
        position = p;
        size = s;
    }

    public Goal(double x, double y, double width, double height){
        position = new Vector(x, y);
        size = new Vector(width, height);
    }

    public String toString(){
        return "Position: "+position+", Size: "+size;
    }

    public boolean isInGoal(Circle c){
        double x = c.getPosition().getX();
        double y = c.getPosition().getY();
        double r = c.getRadius();
        return  x-r >= position.getX() && x+r <= position.getX()+size.getX() && y-r >= position.getY() && y+r <= position.getY()+size.getY();
    }
}