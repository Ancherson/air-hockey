package airhockey.gui;

import java.util.ArrayList;

/**
 * This class manages the particles in the view
 */
public class ParticleManager {
    /**
     * The list of the particles
     */
    private ArrayList<Particle> particles;

    /**
     * The constructor of the particle manager
     */
    public ParticleManager(){
        particles = new ArrayList<Particle>();
    }

    /**
     * Adds the particle p to the list of particles
     * @param p the particle to add
     */
    public void addParticle(Particle p){
        particles.add(p);
    }

    /**
     * Updates the particles based on the time elapsed since last frame and removes the particles which have reached the end of their lives
     * @param dt the time elapsed since last frame
     */
    public void update(double dt){
        for(int i = 0; i < particles.size(); i++){
            Particle p = particles.get(i);
            p.update(dt);
            if(p.getLife() <= 0){
                particles.remove(p);
                i--;
            }
        }
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }
}
