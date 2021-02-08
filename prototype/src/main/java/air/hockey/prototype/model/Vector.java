package air.hockey.prototype.model;

public class Vector{
    private double x;
    private double y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public String toString(){
        return "x: "+this.x+", y: "+this.y;
    }

    public Vector add(Vector v){
        return new Vector(x+v.x, y+v.y);
    }

    public Vector multiply(double m){
        return new Vector(x*m, y*m);
    }

    public double dotProduct(Vector v){
        return x*v.x+y*v.y;
    }

    // returns the length of the vector
    public double length(){
        return Math.sqrt(dotProduct(this));
    }

    // returns a vector of length 1 with the same direction as itself
    public Vector normalize(){
        double l = length();
        if(l == 0) return this;
        return multiply(1./l);
    }

    // returns the reflexion of the vector according to the vector n
    // n must be of length 1
    public Vector reflection(Vector n){
        return add(n.multiply(-2*dotProduct(n)));
    }
}