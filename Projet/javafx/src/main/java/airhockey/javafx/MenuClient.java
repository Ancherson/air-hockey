package airhockey.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuClient extends Application {
    private int WIDTH = 800;
    private int HEIGHT = 500;
    private Stage primaryStage;

    private Scene scene1;
    private Scene scene2;
    private Scene scene3;


    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;





        FirstMenu pane = new FirstMenu(this);
        Pane createMenu = new CreateMenu(this);
        JoinMenu joinMenu = new JoinMenu(this);

        primaryStage.widthProperty().addListener((obs,oldVal,newVal) ->{
            WIDTH = newVal.intValue();
            int heigth = HEIGHT/ 2 -60;
            int width = newVal.intValue()/2-100;
            if((width) <0) width =0;
            Insets pad = new Insets(heigth,width,heigth,width);
            pane.setPadding(pad);
        });

        primaryStage.heightProperty().addListener((obs,oldVal,newVal) ->{
            HEIGHT = newVal.intValue();
            int width = WIDTH/ 2 -100;
            int heigth = newVal.intValue()/2-60;
            if((heigth) <0) heigth =0;
            Insets pad = new Insets(heigth,width,heigth,width);
            pane.setPadding(pad);
        });

         scene1 = new Scene(pane);
         scene2 = new Scene(createMenu);
         scene3 = new Scene(joinMenu);


        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);




        primaryStage.setScene(scene1);
        primaryStage.show();

    }

    public void setScene(int S){
        switch (S){
            case 1:
                primaryStage.setScene(scene1);
                primaryStage.setMinHeight(100);
                primaryStage.setMinWidth(250);
                break;

            case 2:
                primaryStage.setScene(scene2);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;

            case 3:
                primaryStage.setScene(scene3);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;

        }

    }


}
