package airhockey.gui;

import airhockey.model.Vector;
import airhockey.model.Circle;

public class Particle extends Circle{
    private Vector speed;
    private double startLife;
    private double life;

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

    public void update(double dt){
        position = position.add(speed.multiply(dt));
        life -= dt;
        if(life < 0){
            life = 0;
        }
    }
}
