package air.hockey.prototype.javafx;

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
		circle = new Circle(WIDTH / 2 - 25, HEIGHT / 2 - 25, 50);
		draw();
	}

	public void drawCircle(Circle c) {
		ctx.setFill(Color.BLUE);
		ctx.fillOval(c.x, c.y, c.r, c.r);
	}

	public void draw() {
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0,0, WIDTH, HEIGHT);
		drawCircle(circle);
	}

	public class Circle {
		private int x;
		private int y;
		private int r;

		public Circle(int x, int y, int r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}
	}

}
