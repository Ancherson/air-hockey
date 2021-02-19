package air.hockey.prototype.javafx;

import air.hockey.prototype.udp.Client;
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

import java.io.IOException;

public class ProtoPhysicJavaFx extends Application {
    private Canvas canvas;
    private GraphicsContext ctx;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private final int FPS = 60;
    private final long NANOTIME_PER_FRAME = Math.round(1.0 / FPS * 1e9);//in nanoseconds
    private boolean isPressed;

    private Model model;

    private Client client;

    public ProtoPhysicJavaFx() {
        super();
    }

    @Override
    public void init() throws Exception {
        super.init();
        model = new Model();
        try {
            client = new Client(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        draw();
        new Animation().start();
    }

    public Model getModel() {
        return model;
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
        drawCircle(model.getPalet(), Color.BLUE);
        drawCircle(model.getPushers()[0], Color.GREEN);
        drawCircle(model.getPushers()[1], Color.RED);
        for(Wall w : model.getWalls()){
            drawWall(w, Color.BLACK);
        }
    }


    public void mousePressed(MouseEvent event) {
		if(model.isInPusher(event.getX(), event.getY())) {
            isPressed = true;
		}
    }
    
    public void mouseDragged(MouseEvent event) {
		if(isPressed) {
            model.setLocationPusher(event.getX(), event.getY());
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

                model.update(dt/(1e9*1.0));
                draw();
                lastUpdateTime = now;
        }
    }

    /*public static void main(String[] args) {
        System.out.println("a");
        ProtoPhysicJavaFx.launch(args);
        System.out.println("b");
    }*/
}
