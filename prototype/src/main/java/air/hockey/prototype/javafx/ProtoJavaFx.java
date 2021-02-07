package air.hockey.prototype.javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
	private CircleMoving circle;

	private Circle circleMouse;
	private boolean isPressed = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setHeight(HEIGHT);
		primaryStage.setWidth(WIDTH);

		canvas = new Canvas(WIDTH,HEIGHT);
		canvas.setOnMousePressed(this::press);
		canvas.setOnMouseDragged(this::dragged);
		canvas.setOnMouseReleased(this::released);
		ctx = canvas.getGraphicsContext2D();

		Pane root = new Pane();
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();

		setupAnimation();
	}

	public void setupAnimation() {
		circle = new CircleMoving(WIDTH / 2, HEIGHT / 2, 50, Color.BLUE, 5, 5);
		circleMouse = new Circle(200,100,50,Color.RED);
		new Animation().start();
	}

	public void press(MouseEvent event) {
		if(isInCircleMouse(event.getX(), event.getY())) {
			isPressed = true;
		}
	}

	public void dragged(MouseEvent event) {
		if(isPressed) {
			circleMouse.x = event.getX();
			circleMouse.y = event.getY();
		}
	}

	public void released(MouseEvent event) {
		isPressed = false;
	}

	public boolean isInCircleMouse(double x, double y) {
		return ((x - circleMouse.x) * (x - circleMouse.x) + (y - circleMouse.y) * (y - circleMouse.y) < circleMouse.r * circleMouse.r);
	}

	public void drawCircle(Circle c) {
		ctx.setFill(c.color);
		ctx.fillOval(c.x - c.r / 2, c.y - c.r / 2, c.r, c.r);
	}

	public void update(double deltaTime) {
		circle.update(deltaTime);
	}

	public void draw() {
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0,0, WIDTH, HEIGHT);
		drawCircle(circle);
		drawCircle(circleMouse);
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


	public class CircleMoving extends Circle{
		private double vx;
		private double vy;
		private boolean out = false;

		public CircleMoving(double x, double y, double r, Color color, double vx, double vy) {
			super(x,y,r,color);
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

	public class Circle {
		protected double x;
		protected double y;
		protected double r;

		private Color color;

		public Circle(double x, double y, double r, Color color) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.color = color;
		}
	}

}
