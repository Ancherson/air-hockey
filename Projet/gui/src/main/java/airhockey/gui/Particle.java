package airhockey.gui;

import airhockey.model.Vector;
import airhockey.model.Circle;

/**
 * This class represents a particle, used in visual effects for collisions and explosions
 */
public class Particle extends Circle{
    /**
     * The speed of the particle
     */
    private Vector speed;
    /**
     * The amount of time the particle will live in total (in seconds).
     * It is used for calculating the opacity of the particle
     */
    private double startLife;
    /**
     * The amount of time the particle has left to live (in seconds)
     */
    private double life;

    /**
     * The constructor of the particle
     * @param p the particle's position
     * @param s the particle's speed
     * @param r the particle's radius
     * @param l the amount of time the particle will live (in seconds)
     */
    public Particle(Vector p, Vector s, double r, double l){
        super(p, r);
        speed = s;
        startLife = l;
        life = l;
    }

    public double getStartLife(){
        return startLife;
    }

    public double getLife(){
        return life;
    }

    /**
     * Updates the particle's position and life based on the time elapsed since last frame
     * @param dt the time elapsed since last frame
     */
    public void update(double dt){
        position = position.add(speed.multiply(dt));
        life -= dt;
        if(life < 0){
            life = 0;
        }
    }
}
