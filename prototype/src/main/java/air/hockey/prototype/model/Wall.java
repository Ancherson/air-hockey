package air.hockey.prototype.model;

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

    //Return the normal of wall's vector direction
    public Vector getNormal(){
        Vector normal = new Vector(-direction.getY(),direction.getX());
        return normal.normalize();       
    }

    public String toString(){
        return "Position: "+position+ "\nDirection: "+direction;
    }

}