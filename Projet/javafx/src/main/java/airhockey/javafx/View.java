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
import javafx.scene.text.Font;

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
    private Camera camera;
    private Animation animation;

    public View(MenuClient menu, Model model, int numplayer) {
        this.model = model;
        this.menu = menu;
        this.numplayer = numplayer;

        camera = new Camera(new Vector(model.getBoard().getWIDTH()/2, model.getBoard().getHEIGHT()/2), 1, true);

        canvas = new Canvas(WIDTH,HEIGHT);
        ctx = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        isPressed = false;

        this.getChildren().add(canvas);
        ctx.setFont(new Font("Ubuntu", 24));
        draw();
        animation = new Animation();
        animation.start();
    }

    public Vector gameToScreen(Vector v){
        v = v.sub(camera.position).multiply(camera.zoom);
        if(camera.flipX) v.setX(-v.getX());
        return v.add(new Vector(WIDTH, HEIGHT).multiply(.5));
    }

    public Vector screenToGame(Vector v){
        v = v.sub(new Vector(WIDTH, HEIGHT).multiply(.5));
        if(camera.flipX) v.setX(-v.getX());
        return v.multiply(1.0/camera.zoom).add(camera.position);
    }

    public void drawCircle(Circle c, Color col){
        ctx.setFill(col);
        Vector pos = c.getPosition().sub(new Vector(c.getRadius(), c.getRadius()));
        Vector screenPos = gameToScreen(pos);

        Vector size = new Vector(2*c.getRadius()*camera.zoom, 2*c.getRadius()*camera.zoom);
        if(camera.flipX) screenPos = screenPos.sub(new Vector(size.getX(), 0));
        ctx.fillOval(screenPos.getX(), screenPos.getY(), size.getX(), size.getY());
    }

    public void drawWall(Wall w, Color col){
        ctx.setStroke(col);
        ctx.beginPath();
        Vector screenPos = gameToScreen(w.getPosition());
        ctx.moveTo(screenPos.getX(), screenPos.getY());
        Vector screenEndPos = gameToScreen(w.getPosition().add(w.getDirection()));
        ctx.lineTo(screenEndPos.getX(), screenEndPos.getY());
        ctx.stroke();
    }


    public void draw(){
        ctx.setFill(Color.rgb(255, 255, 255, 0.8));
        ctx.fillRect(0, 0, WIDTH, HEIGHT);
        ctx.setFill(Color.BLACK);
        ctx.fillText(model.getScore(), (WIDTH/2)-25, 100,50);
        drawCircle(model.getBoard().getPalet(), Color.BLUE);
        drawCircle(model.getBoard().getPushers()[numplayer], Color.GREEN);
        drawCircle(model.getBoard().getPushers()[1-numplayer], Color.RED);
        for(Wall w : model.getBoard().getWalls()){
            drawWall(w, Color.BLACK);
        }
        /*for(Wall w : model.getBoard().getInvisibleWalls()) {
            drawWall(w, Color.GREEN);
        }*/
    }


    public void mousePressed(MouseEvent event) {
        Vector gamePos = screenToGame(new Vector(event.getX(), event.getY()));
        if(model.isInPusher(gamePos.getX(), gamePos.getY(), numplayer)) {
            isPressed = true;
            lastDragTime = System.nanoTime();
        }
    }

    public void mouseDragged(MouseEvent event) {
        if(isPressed) {
            double time = System.nanoTime();
            double dt = (time-lastDragTime)/(1e9*1.0);
            lastDragTime = time;
            Vector gamePos = screenToGame(new Vector(event.getX(), event.getY()));
            model.setLocationPusher(gamePos.getX(), gamePos.getY(), dt, numplayer);
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

    class Camera{
        private Vector position;
        private double zoom;
        private boolean flipX;
        public Camera(Vector p, double z, boolean fx){
            position = p;
            zoom = z;
            flipX = fx;
        }
    }
}
