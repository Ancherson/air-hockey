package airhockey.gui;


import airhockey.model.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import java.util.LinkedList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;


public class View extends BorderPane {

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
    private ParticleManager particles;

    public View(MenuClient menu, Model model, int numplayer) {
        this.model = model;
        this.menu = menu;
        this.numplayer = numplayer;

        particles = new ParticleManager();
        camera = new Camera(new Vector(model.getBoard().getWIDTH()/2, model.getBoard().getHEIGHT()/2), 1, true);
        canvas = new Canvas(WIDTH,HEIGHT);

        Button back = new Button("back");
        back.setStyle("-fx-background-color: #565656;");
        back.setTextFill(WHITE);

        back.setOnMouseEntered((action)->{
            back.setStyle("-fx-background-color: #FFFFFF;");
            back.setTextFill(BLACK);
        });

        back.setOnMouseExited((action)->{
            back.setStyle("-fx-background-color: #565656;");
            back.setTextFill(WHITE);
        });

        back.setOnAction(value -> {
            menu.setScene(1);
            menu.close();
            close();
        });
        back.setAlignment(Pos.TOP_RIGHT);
        ctx = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        isPressed = false;

        this.setCenter(canvas);
        this.setBottom(back);
        this.setAlignment(back,Pos.TOP_RIGHT);
        this.setStyle("-fx-background-color: #282828;");

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
        Vector screenPos = gameToScreen(pos);
        Vector size = new Vector(radius * 2 * camera.zoom, radius * camera.zoom * 2);
        if(camera.flipX) {
            screenPos = screenPos.sub(new Vector(size.getX(), 0));
            arcExtent *= -1;
        }
        ctx.strokeArc(screenPos.getX(), screenPos.getY(), size.getX(), size.getY(), startAngle, arcExtent, ArcType.OPEN);
    }

    public void drawLinesBoard() {
        drawLine(new Vector(Board.WIDTH / 2, 0), new Vector(Board.WIDTH / 2, Board.HEIGHT), WHITE);
        drawLine(new Vector(Board.WIDTH / 4, 0), new Vector(Board.WIDTH / 4, Board.HEIGHT), WHITE);
        drawLine(new Vector( 3 * Board.WIDTH / 4, 0), new Vector(3 * Board.WIDTH / 4, Board.HEIGHT), WHITE);
        drawArc(new Vector(0, Board.HEIGHT / 2), 100, 270, 180, WHITE);
        drawArc(new Vector(Board.WIDTH, Board.HEIGHT / 2), 100, 90, 180, WHITE);
    }

    public void drawScore() {
        ctx.setFill(WHITE);
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
        drawScore();
        drawLinesBoard();
        Pusher p1 = model.getBoard().getPushers()[numplayer];
        Pusher p2 = model.getBoard().getPushers()[1 - numplayer];
        drawPalet();
        drawCircle(p1.getPosition(), p1.getRadius(), WHITE, bgColor);
        drawCircle(p2.getPosition(), p2.getRadius(), Color.RED, bgColor);
        for(Wall w : model.getBoard().getWalls()){
            drawWall(w, WHITE);
        }
        for(Particle p: particles.getParticles()){
            double alpha = p.getLife()/p.getStartLife();
            drawCircle(p.getPosition(), p.getRadius(), Color.rgb(255, 255, 255, alpha), Color.rgb(255, 255, 255, alpha));
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
        private int frame = 0;
        @Override
        public void handle(long now){
            frame++;
            if(frame % 2 == 0) {
                long dt = now-lastUpdateTime;
                model.update(dt/(1e9*1.0));
                if(model.getBoard().getPalet().getHasHit()){
                    Vector hitPos = model.getBoard().getPalet().getHitPosition();
                    Vector hitNorm = model.getBoard().getPalet().getHitNormal();
                    Vector hitOrth = new Vector(-hitNorm.getY(), hitNorm.getX());
                    int n = 10 +(int)Math.floor(Math.random()*10);
                    for(int i = 0; i < n; i++){
                        particles.addParticle(new Particle(hitPos, hitNorm.multiply(Math.random()*35+5).add(hitOrth.multiply(Math.random()*80-40)), 0.5, 1));
                    }
                }
                particles.update(dt/(1e9*1.0));
                draw();
                lastUpdateTime = now;
                if(model.isFinished()) menu.endRoom(model.hasWon(numplayer));
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
