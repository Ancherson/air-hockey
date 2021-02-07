package air.hockey.prototype.model;

public class Palet extends Circle {

    private Vector speed;
    
    public Palet(Vector position, double radius){
        super(position, radius);
        speed = new Vector(0,0);
    }

    public Vector getSpeed(){
        return speed;
    }

    public void setSpeed(Vector v){
        speed = v;
    }

    public String toString(){
        return super.toString()+"\nSpeed: "+speed;
    }
}
