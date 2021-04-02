package airhockey.model;

public class Bot extends Player{
    private int score;
    private Vector speed;
    private final double ACCELERATION = 2000;
    private final double MAX_SPEED = 400;

    public Bot(){
        super();
        speed = new Vector(0, 0);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score=s;
    }

    public void moveTowards(Vector p, Pusher pusher, double dt){
        Vector d = p.sub(pusher.getPosition());
        d = d.normalize();
        speed = speed.add(d.multiply(ACCELERATION*dt));
        if(speed.length() > MAX_SPEED){
            speed = speed.normalize().multiply(MAX_SPEED);
        }
    }

    public void update(Model model, double dt){
        //get the pusher and save its position
        Pusher p = model.getBoard().getPushers()[0];
        Vector lastPos = new Vector(p.getPosition().getX(), p.getPosition().getY());

        //change the speed (this is where the AI will make its decisions)
        //TODO ACTUAL AI LOGIC
        moveTowards(model.getBoard().getPalet().getPosition(), p, dt);





        //apply movement to the pusher
        Vector position = p.getPosition().add(speed.multiply(dt));

        //resolve collisions, hit the palet...
        p.moveTo(position,model.getBoard().getWalls(), model.getBoard().getInvisibleWalls(), model.getBoard().getPalet());
        p.wallCollisions(model.getBoard().getWalls());
        p.wallCollisions(model.getBoard().getInvisibleWalls());

        //set the speed according to the actual distance moved
        speed = p.position.sub(lastPos).multiply(1/dt);
        p.setSpeed(speed);
    }
}