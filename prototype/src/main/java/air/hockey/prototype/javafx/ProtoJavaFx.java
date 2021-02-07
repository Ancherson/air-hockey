package air.hockey.prototype.javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProtoJavaFx extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 500;

	private Canvas canvas;
	private GraphicsContext ctx;
	private Circle circle;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setHeight(HEIGHT);
		primaryStage.setWidth(WIDTH);

		canvas = new Canvas(WIDTH,HEIGHT);
		ctx = canvas.getGraphicsContext2D();

		Pane root = new Pane();
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();

		setupAnimation();
	}

	public void setupAnimation() {
		circle = new Circle(WIDTH / 2, HEIGHT / 2, 50, 1, 1);
		new Animation().start();
//		draw();
	}

	public void drawCircle(Circle c) {
		ctx.setFill(Color.BLUE);
		ctx.fillOval(c.x - c.r / 2, c.y - c.r / 2, c.r, c.r);
	}

	public void update(long deltaTime) {
		circle.update(deltaTime);
	}

	public void draw() {
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0,0, WIDTH, HEIGHT);
		drawCircle(circle);
	}

	public class Animation extends AnimationTimer {
		private long oldTime = System.nanoTime();
		@Override
		public void handle(long now) {
//			System.out.println(now - oldTime);
			update(now - oldTime);
			draw();
			oldTime = now;
		}
	}

	public class Circle {
		private double x;
		private double y;
		private double r;

		private double vx;
		private double vy;

		public Circle(double x, double y, double r, double vx, double vy) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.vx = vx;
			this.vy = vy;
		}

		public Circle(double x, double y, double r) {
			this(x,y,r,0,0);
		}

		public void edges() {
			if(x < r || x > WIDTH - r){
				this.vx *= -1;
			}
			if(y < r || y > HEIGHT - r){
				this.vy *= -1;
			}
		}

		public void update(long delta) {
			this.x += this.vx * delta / 10000000;
			this.y += this.vy * delta / 10000000;
			this.edges();
		}
	}

}
