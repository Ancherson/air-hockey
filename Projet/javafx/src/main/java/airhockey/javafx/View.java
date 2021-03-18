package airhockey.javafx;


import airhockey.model.Circle;
import airhockey.model.Model;
import airhockey.model.Vector;
import airhockey.model.Wall;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class View extends Pane {

    private MenuClient menu;
    private Canvas canvas;
    private GraphicsContext ctx;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private boolean isPressed;
    private double lastDragTime;
    private Model model;
    private int numplayer;
    private Animation animation;

    public View(MenuClient menu, Model model, int numplayer) {
        this.model = model;
        this.menu = menu;
        this.numplayer = numplayer;

        canvas = new Canvas(WIDTH,HEIGHT);
        ctx = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        isPressed = false;

        this.getChildren().add(canvas);

        draw();
        animation = new Animation();
        animation.start();
    }

    public void drawCircle(Circle c, Color col){
        ctx.setFill(col);
        ctx.fillOval(c.getPosition().getX()-c.getRadius(), c.getPosition().getY()-c.getRadius(), 2*c.getRadius(), 2*c.getRadius());
    }

    public void drawWall(Wall w, Color col){
        ctx.setStroke(col);
        ctx.beginPath();
        ctx.moveTo(w.getPosition().getX(), w.getPosition().getY());
        Vector end = w.getPosition().add(w.getDirection());
        ctx.lineTo(end.getX(), end.getY());
        ctx.stroke();
    }


    public void draw(){
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, WIDTH, HEIGHT);
        drawCircle(model.getBoard().getPalet(), Color.BLUE);
        drawCircle(model.getBoard().getPushers()[numplayer], Color.GREEN);
        drawCircle(model.getBoard().getPushers()[1-numplayer], Color.RED);
        for(Wall w : model.getBoard().getWalls()){
            drawWall(w, Color.BLACK);
        }
        for(Wall w : model.getBoard().getInvisibleWalls()) {
            drawWall(w, Color.GREEN);
        }
    }


    public void mousePressed(MouseEvent event) {
        if(model.isInPusher(event.getX(), event.getY(),numplayer)) {
            isPressed = true;
            lastDragTime = System.nanoTime();
        }
    }

    public void mouseDragged(MouseEvent event) {
        if(isPressed) {
            double time = System.nanoTime();
            double dt = (time-lastDragTime)/(1e9*1.0);
            lastDragTime = time;
            model.setLocationPusher(event.getX(), event.getY(), dt, numplayer);
        }
    }

    public void mouseReleased(MouseEvent event) {
        model.PusherReleased(numplayer);
        isPressed = false;
    }

    public void close() {
        animation.stop();
    }

    public class Animation extends AnimationTimer {
        private long lastUpdateTime = System.nanoTime();

        @Override
        public void handle(long now){
            long dt = now-lastUpdateTime;
            model.update(dt/(1e9*1.0));
            draw();
            lastUpdateTime = now;
        }
    }
}
