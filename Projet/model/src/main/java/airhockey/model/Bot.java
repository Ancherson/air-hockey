package airhockey.model;

public class Bot extends Player{
    private int score;

    public Bot(){
        super();
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        score=s;
    }

    public void update(Model model, double dt){
        Pusher p = model.getBoard().getPushers()[0];
        Vector position = p.getPosition().add(new Vector(Math.sin(dt*60), Math.cos(dt*60)));
        p.moveTo(position,model.getBoard().getWalls(), model.getBoard().getInvisibleWalls(), model.getBoard().getPalet());
        p.wallCollisions(model.getBoard().getWalls());
        p.wallCollisions(model.getBoard().getInvisibleWalls());

    }
}