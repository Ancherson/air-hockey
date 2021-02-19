package air.hockey.prototype.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;

import air.hockey.prototype.model.*;

public class ProtoPhysicJavaFx extends Application {
    private Canvas canvas;
    private GraphicsContext ctx;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private final int FPS = 60;
    private final long NANOTIME_PER_FRAME = Math.round(1.0 / FPS * 1e9);//in nanoseconds
    private boolean isPressed;
    
    private Palet palet;
    private Pusher[] pusher;
    private Wall[] walls;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        canvas = new Canvas(WIDTH,HEIGHT);
        ctx = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);

        


        Pane root = new Pane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        isPressed = false;
        
        palet = new Palet(new Vector(400, 200), 20);
        palet.setSpeed(new Vector(0, 1));

        pusher = new Pusher[1];
        pusher[0] = new Pusher(new Vector(300, 100), 25);
        walls = new Wall[4];
        walls[0] = new Wall(50, 50, WIDTH-100, 0);
        walls[1] = new Wall(50, 50, 0, HEIGHT-100);
        walls[2] = new Wall(WIDTH-50, 50, 0, HEIGHT-100);
        walls[3] = new Wall(50, HEIGHT-50, WIDTH-100, 0);

        draw();
        new Animation().start();
    }

    public void drawCircle(Circle c, Color col){
        ctx.setFill(col);
        ctx.fillOval(c.getPosition().getX()-c.getRadius(), c.getPosition().getY()-c.getRadius(), 2*c.getRadius(), 2*c.getRadius());
    }

    public void drawWall(Wall w, Color col){
        ctx.setFill(col);
        ctx.beginPath();
        ctx.moveTo(w.getPosition().getX(), w.getPosition().getY());
        Vector end = w.getPosition().add(w.getDirection());
        ctx.lineTo(end.getX(), end.getY());
        ctx.stroke();
    }


    public void draw(){
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, WIDTH, HEIGHT);
        drawCircle(palet, Color.BLUE);
        drawCircle(pusher[0], Color.RED);
        for(Wall w : walls){
            drawWall(w, Color.BLACK);
        }
    }

    public void update(double dt){
        palet.update(dt, walls, pusher);
        pusher[0].paletCollision(palet);
        pusher[0].resetMovement();
    }

    public boolean isInCircleMouse(double x, double y){
        return pusher[0].getPosition().add(new Vector(x, y).multiply(-1)).length() < pusher[0].getRadius(); 
    }

    public void mousePressed(MouseEvent event) {
		if(isInCircleMouse(event.getX(), event.getY())) {
            isPressed = true;
		}
    }
    
    public void mouseDragged(MouseEvent event) {
		if(isPressed) {
            pusher[0].setPosition(new Vector(event.getX(), event.getY()));
            pusher[0].wallCollisions(walls);
		}
	}

	public void mouseReleased(MouseEvent event) {
		isPressed = false;
	}

    public class Animation extends AnimationTimer{
        private long lastUpdateTime = System.nanoTime();

        @Override
        public void handle(long now){
            long dt = now-lastUpdateTime;

            //if(dt >= NANOTIME_PER_FRAME){
                update(dt/(1e9*1.0));
                draw();
                lastUpdateTime = now;
            //}
        }
    }
}
