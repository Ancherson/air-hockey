package air.hockey.prototype.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProtoPhysicJavaFx extends Application {
    private Canvas canvas;
    private GraphicsContext ctx;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        canvas = new Canvas(800,500);
        ctx = canvas.getGraphicsContext2D();


        Pane root = new Pane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        
    }
}
