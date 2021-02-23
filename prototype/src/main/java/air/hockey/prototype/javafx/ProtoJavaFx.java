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

	private final double FRIXION = 0.99;
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
		circle = new CircleMoving(WIDTH / 2, HEIGHT / 2, 25, Color.BLUE, 5, 5);
		circleMouse = new Circle(200,100,25,Color.RED);
		new Animation().start();
	}

	public void press(MouseEvent event) {
		if(isInCircleMouse(event.getX(), event.getY())) {
			isPressed = true;
		}
	}

	public void dragged(MouseEvent event) {
		if(isPressed) {
			if(event.getX()<100 || event.getX()>400||event.getY()<50||event.getY()>750){
				released(event);
			}else{
				circleMouse.update(event.getX(), event.getY());
				if(circleMouse.collide(circle)) {
					circle.addSpeed(circleMouse.x - circleMouse.previousX, circleMouse.y - circleMouse.previousY);
				}
			}
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
		ctx.fillOval(c.x - c.r, c.y - c.r, c.r * 2, c.r * 2);
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

		public void addSpeed(double vx, double vy) {
			this.vx += vx;
			this.vy += vy;
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
			this.vx *= FRIXION;
			this.vy *= FRIXION;

			this.x += this.vx * (delta * 10 * 2);
			this.y += this.vy * (delta * 10 * 2);
			this.edges();
		}

	}

	public class Circle {
		protected double x;
		protected double y;
		protected double r;

		private double previousX;
		private double previousY;

		private Color color;

		public Circle(double x, double y, double r, Color color) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.color = color;

			previousX = x;
			previousY = y;
		}

		public boolean collide(Circle c) {
			return ((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y) < (c.r + r) * (c.r + r));
		}

		public void update(double x, double y) {
			this.previousX = this.x;
			this.x = x;

			this.previousY = this.y;
			this.y = y;
		}
	}

}
