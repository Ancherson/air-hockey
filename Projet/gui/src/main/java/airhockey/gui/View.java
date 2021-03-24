package airhockey.gui;


import airhockey.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

import java.lang.management.OperatingSystemMXBean;
import java.util.LinkedList;

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
    private final Color bgColor = Color.rgb(40, 40, 40, .8);

    private LinkedList<Vector> listPosPalet = new LinkedList<>();
    private int maxLengthListPalet = 20;

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

    public Vector gameToScreenNoFlip(Vector v) {
        v = v.sub(camera.position).multiply(camera.zoom);
        return v.add(new Vector(WIDTH, HEIGHT).multiply(.5));
    }

    public Vector screenToGame(Vector v){
        v = v.sub(new Vector(WIDTH, HEIGHT).multiply(.5));
        if(camera.flipX) v.setX(-v.getX());
        return v.multiply(1.0/camera.zoom).add(camera.position);
    }

    public void drawCircle(Vector center, double radius, Color strokeCol, Color fillColor){
        ctx.setStroke(strokeCol);
        ctx.setFill(fillColor);
        Vector pos = center.sub(new Vector(radius, radius));
        Vector screenPos = gameToScreen(pos);

        Vector size = new Vector(2 * radius * camera.zoom, 2 * radius * camera.zoom);
        if(camera.flipX) screenPos = screenPos.sub(new Vector(size.getX(), 0));
        ctx.fillOval(screenPos.getX(), screenPos.getY(), size.getX(), size.getY());
        ctx.strokeOval(screenPos.getX(), screenPos.getY(), size.getX(), size.getY());
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

    public void drawLine(Vector start, Vector end, Color col) {
        ctx.setStroke(col);
        ctx.beginPath();
        Vector screenPos = gameToScreen(start);
        ctx.moveTo(screenPos.getX(), screenPos.getY());
        Vector screenEndPos = gameToScreen(end);
        ctx.lineTo(screenEndPos.getX(), screenEndPos.getY());
        ctx.stroke();
    }

    public void drawArc(Vector center, double radius, double startAngle, double arcExtent, Color col) {
        ctx.setStroke(col);
        Vector pos = center.sub(new Vector(radius, radius));
        Vector screenPos = gameToScreenNoFlip(pos);
        Vector size = new Vector(radius * 2 * camera.zoom, radius * camera.zoom * 2);
        ctx.strokeArc(screenPos.getX(), screenPos.getY(), size.getX(), size.getY(), startAngle, arcExtent, ArcType.OPEN);
    }

    public void drawLinesBoard() {
        drawLine(new Vector(Board.WIDTH / 2, 0), new Vector(Board.WIDTH / 2, Board.HEIGHT), Color.WHITE);
        drawLine(new Vector(Board.WIDTH / 4, 0), new Vector(Board.WIDTH / 4, Board.HEIGHT), Color.WHITE);
        drawLine(new Vector( 3 * Board.WIDTH / 4, 0), new Vector(3 * Board.WIDTH / 4, Board.HEIGHT), Color.WHITE);
        drawArc(new Vector(0, Board.HEIGHT / 2), 100, 270, 180, Color.WHITE);
        drawArc(new Vector(Board.WIDTH, Board.HEIGHT / 2), 100, 90, 180, Color.WHITE);
    }

    public void drawScore() {
        ctx.setFill(Color.WHITE);
        Vector pos = new Vector(3 * Board.WIDTH / 8 + 10, Board.HEIGHT / 8);
        pos = gameToScreen(pos);
        ctx.fillText(Integer.toString(model.getScore(0)), pos.getX(), pos.getY());

        pos = new Vector(5 * Board.WIDTH / 8 + 10, Board.HEIGHT / 8);
        pos = gameToScreen(pos);
        ctx.fillText(Integer.toString(model.getScore(1)), pos.getX(), pos.getY());
    }

    public void drawPalet() {
        listPosPalet.add(model.getBoard().getPalet().getPosition());
        while(listPosPalet.size() > maxLengthListPalet) {
            listPosPalet.removeFirst();
        }
        double radius = model.getBoard().getPalet().getRadius();
        for(int i = listPosPalet.size() - 1; i >= 0; i--) {
            drawCircle(listPosPalet.get(i), radius * i / (listPosPalet.size() - 1), Color.rgb(255,255,255, i * 1.0 / (listPosPalet.size() - 1)), Color.rgb(255,255,255, i * 1.0 / (listPosPalet.size() - 1)));
        }
    }

    public void draw(){

        ctx.setFill(bgColor);
        ctx.fillRect(0, 0, WIDTH, HEIGHT);

//        ctx.setFill(Color.WHITE);
//        ctx.fillText(model.getScore(), (WIDTH/2)-25, 100,50);
        drawScore();
        drawLinesBoard();
        //Palet p = model.getBoard().getPalet();
        Pusher p1 = model.getBoard().getPushers()[numplayer];
        Pusher p2 = model.getBoard().getPushers()[1 - numplayer];
        drawPalet();
        //drawCircle(p.getPosition(), p.getRadius(), Color.WHITE, Color.WHITE);
        drawCircle(p1.getPosition(), p1.getRadius(), Color.WHITE, bgColor);
        drawCircle(p2.getPosition(), p2.getRadius(), Color.RED, bgColor);
        for(Wall w : model.getBoard().getWalls()){
            drawWall(w, Color.WHITE);
        }
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
        private int frame = 0;
        @Override
        public void handle(long now){
            frame++;
            if(frame % 2 == 0) {
                long dt = now-lastUpdateTime;
                model.update(dt/(1e9*1.0));
                draw();
                lastUpdateTime = now;
                frame = 0;
            }
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