package airhockey.model;

/*Class that creates border of air hockey 
*/
public class Wall {

    private Vector position;
    private Vector direction;

    //Constructor takes a vector position which is a point and a vector direction which is a direction
    public Wall(Vector position, Vector direction){
        this.position=position;
        this.direction=direction;
    }

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

    // calculates the closest point to p on the wall
    public Vector closestPoint(Vector p){
        //lambda is in [0, 1], lambda = 0 means that the closest point is on the start point of the wall, lambda = 1 means that the closest point is the end point of the wall
        double lambda = Math.max(0, Math.min(1, (p.add(position.multiply(-1)).dotProduct(direction))/(direction.dotProduct(direction))));
        //by multiplying D by lambda and adding it to P we get the actual position of the point
        return position.add(direction.multiply(lambda));
    }

    //Return the normal of wall's vector direction
    public Vector getNormal(Vector p){
        Vector cp = closestPoint(p);
        //if the closest point is the start point of the wall, the normal is the direction from the start point to p
        if(cp.getX() == position.getX() && cp.getY() == position.getY()){
            return p.add(position.multiply(-1)).normalize();
        }
        //if the closest point is the end point of the wall, the normal is the direction from the end point to p
        if(cp.getX() == position.getX()+direction.getX() && cp.getY() == position.getY()+ direction.getY()){
            return p.add(position.add(direction).multiply(-1)).normalize();
        }
        //else it is the orthogonal vector to the direction
        Vector normal = new Vector(-direction.getY(),direction.getX());
        return normal.normalize();
    }

    public String toString(){
        return "Position: "+position+ "\nDirection: "+direction;
    }

}