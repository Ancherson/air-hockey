package airhockey.model;

import java.io.Serializable;

/**
 * Class which creates Vector
 */
 public class Vector implements Serializable {
    private double x;
    private double y;

    /**
     * Constructor of a Vector
     * @param x double representing the x position 
     * @param y double representing the y position
     */
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }
    /**
     * @return double of the x position
     */
    public double getX(){
        return x;
    }

    /**
     * @return double of the y position
     */
    public double getY(){
        return y;
    }

    /**
     * @param x double changing the x position
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * @param y double changing the y position
     */
    public void setY(double y){
        this.y = y;
    }

    /**
     * @return String of the position of a Vector
     */
    public String toString(){
        return "x: "+this.x+", y: "+this.y;
    }

    /**
     *Makes the addition of two Vector
     *@param v Vector that is added to the Vector
     *@return Vector of the additon of Vector in parameters and the other Vector
     */

    public Vector add(Vector v){
        return new Vector(x+v.x, y+v.y);
    }

    /**
     * Makes the substraction of two Vector
     * @param v Vector that substracted the Vector
     * @return Vector of the substraction of Vector in parameters and the other Vector
     */
    public Vector sub(Vector v){
        return new Vector(x-v.x, y-v.y);
    }

    /**
     * Multiplie the Vector by a scalar
     * @param m double of the scalar
     * @return Vector of the multiplicatiton of the Vector by a scalar
     */
    public Vector multiply(double m){
        return new Vector(x*m, y*m);
    }

    /**
     * Returns the dot product between two Vector
     * @param v Vector that multiplies the Vector
     * @return double the dot product of the two Vector
     */
    public double dotProduct(Vector v){
        return x*v.x+y*v.y;
    }

    /**
     * Returns the length of the Vector 
     * @return double the length of the Vector
     */
    public double length(){
        return Math.sqrt(dotProduct(this));
    }

    /**
     * Returns a Vector of length 1 with the same direction at ifself
     * @return Vector of length 1 with the same direction at itself
     */
    public Vector normalize(){
        double l = length();
        if(l == 0) return this;
        return multiply(1./l);
    }

    /**
     * Returns the reflection of the vector according to the vector n
     * @param n Vector of length 1
     * @return Vector of the reflection according to Vector n
     */
    public Vector reflection(Vector n){
        //n multiplied by the dot product of this and n is the projection of this on the vector n (since ||n|| = 1)
        //so subtracting 2 times this vector to this means reversing the coordinate of this along the axis that goes in the direction of n, which is effectively a reflection
        return add(n.multiply(-2*dotProduct(n)));
    }
}