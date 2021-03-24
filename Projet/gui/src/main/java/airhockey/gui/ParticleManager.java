package airhockey.gui;

import java.util.ArrayList;

public class ParticleManager {
    private ArrayList<Particle> particles;

    public ParticleManager(){
        particles = new ArrayList<Particle>();
    }

    public void addParticle(Particle p){
        particles.add(p);
    }

    public void update(double dt){
        for(int i = 0; i < particles.size(); i++){
            Particle p = particles.get(i);
            p.update(dt);
            if(p.getLife() <= 0){
                particles.remove(p);
                i++;
            }
        }
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }
}
