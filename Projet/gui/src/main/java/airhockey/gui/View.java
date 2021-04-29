package airhockey.gui;


import airhockey.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.LinkedList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;


public class View extends BorderPane {

    private MenuClient menu;
    private Canvas canvas;
    private GraphicsContext ctx;

    private final double WIDTH = 800;
    private final double HEIGHT = 500;
    private double currentWidth = WIDTH;
    private double currentHeight = HEIGHT;

    private boolean isPressed;
    private double lastDragTime;
    private Model model;
    private int numplayer;
    private boolean training;
    private Camera camera;
    private int shake;
    private Animation animation;
    private final Color bgColor = Color.rgb(40, 40, 40, .8);

    private LinkedList<Vector> listPosPalet = new LinkedList<>();
    private int maxLengthListPalet = 20;
    private ParticleManager particles;
    private Sound sound;

    private boolean finished = false;
    private String endMessage = "";
    private Color endColorMessage = WHITE;
    private int endCounter = 60;

    private boolean debugMode = false;

    public View(MenuClient menu, Model model, int numplayer, boolean training) {
        this.model = model;
        this.menu = menu;
        this.numplayer = numplayer;
        this.training = training;

        particles = new ParticleManager();
        camera = new Camera(new Vector(model.getBoard().getWIDTH()/2, model.getBoard().getHEIGHT()/2), 1, true);
        canvas = new Canvas(WIDTH,HEIGHT);
        shake = -1;
        
        HBox pane = new HBox();
        pane.setStyle("-fx-background-color: #282828;");
        sound = new Sound();

        if(training) {
            ClickButton debug = new ClickButton("DEBUG");
            debug.setOnAction(value -> {
                debugMode = !debugMode;
            });
            pane.getChildren().add(debug);

        }

        ClickButton quit = new ClickButton("Quit");
        quit.setOnAction(value -> {
            sound.play("buttonsRelax");
            menu.setScene("first");
            menu.close();
        });

        pane.getChildren().add(quit);

        ctx = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        isPressed = false;
        this.setCenter(canvas);
        pane.setAlignment(Pos.TOP_RIGHT);
        this.setBottom(pane);
        this.setStyle("-fx-background-color: #282828;");

        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setFont(new Font("Ubuntu", 24));
        draw();
        animation = new Animation();
        animation.start();
    }

    public void resizeCanvas(double width, double height) {
        if(width == -1) {
            currentHeight = height;
            canvas.setHeight(currentHeight);
        }
        else if(height == -1){
            currentWidth = width;
            canvas.setWidth(currentWidth);
        }
        double zoom = Math.min(currentWidth / WIDTH, currentHeight / HEIGHT);
        ctx.setFont(new Font("Ubuntu", 24 * zoom));
        camera.zoom = zoom;
    }

    public void lostConnexion() {
        endMessage = "Connexion Lost";
        finished = true;
        menu.closeClient();
    }

    public Vector gameToScreen(Vector v){
        v = v.sub(camera.position).multiply(camera.zoom);
        if(camera.flipX) v.setX(-v.getX());
        return v.add(new Vector(currentWidth, currentHeight).multiply(.5));
    }

    public Vector screenToGame(Vector v){
        v = v.sub(new Vector(currentWidth, currentHeight).multiply(.5));
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
        Vector pos = new Vector(3 * Board.WIDTH / 8, Board.HEIGHT / 8);
        pos = gameToScreen(pos);
        ctx.fillText(Integer.toString(model.getScore(0)), pos.getX(), pos.getY());

        pos = new Vector(5 * Board.WIDTH / 8, Board.HEIGHT / 8);
        pos = gameToScreen(pos);
        ctx.fillText(Integer.toString(model.getScore(1)), pos.getX(), pos.getY());
    }

    public void drawPalet() {
        Palet pal = model.getBoard().getPalet();
        if(model.getBoard().getPalet().getScoredGoal() == -1){
            listPosPalet.add(model.getBoard().getPalet().getPosition());
        } else if(!listPosPalet.isEmpty()){
            listPosPalet.removeFirst();
        }

        while(listPosPalet.size() > maxLengthListPalet) {
            listPosPalet.removeFirst();
        }
        double radius = model.getBoard().getPalet().getRadius();
        int listSize = listPosPalet.size();
        for(int i = 0; i < listSize; i++) {
             if(i == listSize - 1 && model.getBoard().getPalet().getScoredGoal() == -1) {
                 drawCircle(listPosPalet.get(i), radius * i / (maxLengthListPalet - 1), BLACK, WHITE);
                 Vector angulaire = new Vector(Math.cos(pal.getAngle()),Math.sin(pal.getAngle()));
                 double rad = radius * i / (maxLengthListPalet - 1);
                 //ctx.setLineWidth(5);
                 drawLine(pal.getPosition().add(angulaire.multiply(rad*0.4)),angulaire.multiply(rad).add(pal.getPosition()), BLACK);
                 drawLine(pal.getPosition().add(angulaire.multiply(rad*-0.4)),angulaire.multiply(-rad).add(pal.getPosition()), BLACK);
                 drawCircle(pal.getPosition(), 0.4*rad, BLACK, WHITE);
                 //ctx.setLineWidth(1);
             } else {
                 drawCircle(listPosPalet.get(i), radius * i / (maxLengthListPalet - 1), Color.rgb(255,255,255, i * 1.0 / (listSize - 1)),Color.rgb(255,255,255, i * 1.0 / (listSize - 1)));
             }
        }
    }

    public void drawEnd() {
        ctx.setFill(endColorMessage);
        ctx.fillText(endMessage, currentWidth / 2, currentHeight / 2);
    }

    public void draw(){
        ctx.setFill(bgColor);
        ctx.fillRect(0, 0, currentWidth, currentHeight);
        drawScore();
        drawLinesBoard();
        Pusher p1 = model.getBoard().getPushers()[numplayer];
        Pusher p2 = model.getBoard().getPushers()[1 - numplayer];
        drawPalet();
        drawCircle(p1.getPosition(), p1.getRadius(), Color.BLUE, Color.rgb(0,0,255,0.4));
        drawCircle(p2.getPosition(), p2.getRadius(), Color.RED, Color.rgb(255, 0, 0, 0.4));
        for(Wall w : model.getBoard().getWalls()){
            drawWall(w, WHITE);
        }

        if(debugMode) {
            for (Vector v : model.getDEBUG_POINTS()) {
                drawCircle(v, 6, Color.GREEN, Color.GREEN);
            }
            for (var elt : model.getDEBUG_LINES().entrySet()) {
                if (elt.getKey() == Bot.TARGETS[2]) {
                    Vector line = elt.getKey().sub(elt.getValue());
                    line = line.multiply(1.0 / line.getY() * (Board.HEIGHT - elt.getValue().getY()));
                    drawLine(elt.getValue(), elt.getValue().add(line), Color.RED);
                    drawLine(elt.getValue().add(line), new Vector(Board.WIDTH, Board.HEIGHT / 2), Color.RED);
                } else if (elt.getKey() == Bot.TARGETS[0]) {
                    Vector line = elt.getKey().sub(elt.getValue());
                    line = line.multiply(1.0 / line.getY() * -1 * (elt.getValue().getY()));
                    drawLine(elt.getValue(), elt.getValue().add(line), Color.RED);
                    drawLine(elt.getValue().add(line), new Vector(Board.WIDTH, Board.HEIGHT / 2), Color.RED);
                } else {
                    drawLine(elt.getKey(), elt.getValue(), Color.BLUE);
                }
                //Vector v = new Vector((line.getY() - Board.HEIGHT) / (-1), Board.HEIGHT);
                //drawCircle(v, 10, Color.RED, Color.RED);
            }
        }

        for(Particle p: particles.getParticles()){
            double alpha = p.getLife()/p.getStartLife();
            drawCircle(p.getPosition(), p.getRadius(), Color.rgb(255, 255, 255, alpha), Color.rgb(255, 255, 255, alpha));
        }

        if(finished) {
            ctx.setFill(Color.rgb(40,40,40,1 - endCounter * 1.0 / 60 ));
            ctx.fillRect(0, 0, currentWidth, currentHeight);
            if(endCounter > 15) endCounter--;
            else drawEnd();
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

    public void createParticules() {
        new Thread(() -> {
            sound.play("collisionRelax");
        }).start();
        Vector hitPos = model.getBoard().getPalet().getHitPosition();
        Vector hitNorm = model.getBoard().getPalet().getHitNormal();
        Vector hitOrth = new Vector(-hitNorm.getY(), hitNorm.getX());

        int n = 10 +(int)Math.floor(Math.random()*10);
        for(int i = 0; i < n; i++){
            particles.addParticle(new Particle(hitPos, hitNorm.multiply(Math.random()*35+5).add(hitOrth.multiply(Math.random()*80-40)), 0.5, 1));
        }
    }

    public void explosion() {
        Platform.runLater(()->{
            sound.play("shakingRelax");
        });

        shake = 20;
        for(int i = 0; i < 500; i++){
            double dirPos = Math.random()*Math.PI*2;
            Vector dir = new Vector(Math.cos(dirPos), Math.sin(dirPos));
            double dirSpd = Math.random()*Math.PI*2;
            particles.addParticle(new Particle(model.getBoard().getPalet().getPosition().add(dir.multiply(model.getBoard().getPalet().getRadius()*Math.random())), dir.multiply(Math.random()*85+5), 0.5+Math.random(), 1));
        }
    }

    public void shaking() {
        if(shake > 0){
            camera.position = new Vector(model.getBoard().getWIDTH()/2+Math.random()*2*shake-shake, model.getBoard().getHEIGHT()/2+Math.random()*2*shake-shake);
            shake--;
        } else if(shake == 0){
            Vector c = new Vector(model.getBoard().getWIDTH()/2, model.getBoard().getHEIGHT()/2);
            Vector d = c.sub(camera.position);
            if(d.length() > 1) {
                camera.position = camera.position.add(d.multiply(0.4));
            } else {
                shake = -1;
            }
        }
    }

    public void endGame() {
        finished = true;
        if(model.hasWon(numplayer)) {
            endMessage = "Congratulations! You won!";
            endColorMessage = Color.GREEN;
        }
        else {
            endMessage = "Bravo, you are the worst player in this game";
            endColorMessage = Color.RED;
        }
        menu.closeClient();
    }

    public class Animation extends AnimationTimer {
        private long lastUpdateTime = System.nanoTime();
        private int frame = 0;
        @Override
        public void handle(long now){
            frame++;
            if(frame % 2 == 0) {
                long dt = now-lastUpdateTime;
                if(!finished) model.update(dt/(1e9*1.0));
                if(!finished && model.getBoard().getPalet().getHasHit()){
                    new Thread(() -> {
                        sound.setVolume("collisionRelax", (model.getBoard().getPalet().getSpeed().length()/dt)/1e-4);
                        sound.play("collisionRelax");
                    }).start();
                    createParticules();
                }
                if(!finished && model.getBoard().getPalet().getScoredGoal() != -1 && model.getCounter() == 1){
                    explosion();
                }
                shaking();
                particles.update(dt/(1e9*1.0));
                draw();
                if((training && model.isFinished()) || (!training && menu.isFinished())) {
                    endGame();
                }
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
