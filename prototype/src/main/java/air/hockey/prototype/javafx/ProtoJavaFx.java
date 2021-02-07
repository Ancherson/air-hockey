package air.hockey.prototype.javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProtoJavaFx extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 500;
	private final int NUM_FRAME_PER_SEC = 60;
	private final long NB_NSEC_PER_FRAME = Math.round(1.0 / NUM_FRAME_PER_SEC * 1e9);

	private Canvas canvas;
	private GraphicsContext ctx;
	private Circle circle;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setHeight(HEIGHT);
		primaryStage.setWidth(WIDTH);

		canvas = new Canvas(WIDTH,HEIGHT);
		canvas.setOnMouseClicked(this::handle);
		ctx = canvas.getGraphicsContext2D();

		Pane root = new Pane();
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();

		setupAnimation();
	}

	public void setupAnimation() {
		circle = new Circle(WIDTH / 2, HEIGHT / 2, 50, 5, 5);
		new Animation().start();
		draw();
	}

	public void handle(MouseEvent event) {
		
	}

	public void drawCircle(Circle c) {
		ctx.setFill(Color.BLUE);
		ctx.fillOval(c.x - c.r, c.y - c.r, c.r, c.r);
	}

	public void update(double deltaTime) {
		circle.update(deltaTime);
	}

	public void draw() {
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0,0, WIDTH, HEIGHT);
		drawCircle(circle);
	}

	public class Animation extends AnimationTimer {
		private long lastUpdate = System.nanoTime();
		@Override
		public void handle(long now) {
			long deltaT = now - lastUpdate;
			if(deltaT >= NB_NSEC_PER_FRAME) {
				lastUpdate = now;
				update(deltaT / 1e9);
				draw();
			}
		}
	}

	public class Circle {
		private double x;
		private double y;
		private double r;

		private double vx;
		private double vy;

		private boolean out = false;

		public Circle(double x, double y, double r, double vx, double vy) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.vx = vx;
			this.vy = vy;
		}

		public void edges() {
			if(!out && (x < r || x > WIDTH - r)){
				this.vx *= -1;
				out = true;
			}
			else if(!out && (y < r || y > HEIGHT - r)){
				this.vy *= -1;
				out = true;
			}else {
				out = false;
			}

		}

		public void update(double delta) {
			this.x += this.vx * (delta * 10 * 2);
			this.y += this.vy * (delta * 10 * 2);
			this.edges();
		}
	}

}
