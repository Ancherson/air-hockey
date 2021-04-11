package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.BLACK;

public class JoinMenu extends BorderPane {

    private Sound sound;

    public JoinMenu(MenuClient menu){

        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        sound = new Sound();

        Label joinT = new Label("Enter the id");
        TextField id = new TextField();
        Button back = new Button("Back");

        VBox box = new VBox(joinT,id);
        box.setAlignment(Pos.CENTER);

        joinT.setMinHeight(100);
        joinT.setMaxHeight(100);        
        joinT.setStyle("-fx-font : 28 Ubuntu;");
        joinT.setTextFill(WHITE);


        id.setMaxWidth(300);
        id.setPromptText("id");
        id.setOnMousePressed( event -> {
            id.selectAll();
        });
        id.setOnAction(event -> {
            menu.joinRoom(id.getText());
        });

        back.setMaxSize(100,50);
        back.setPrefSize(60,50);

        back.setStyle("-fx-background-color: #565656;");
        back.setTextFill(WHITE);

        back.setOnMouseEntered((action)->{
            back.setStyle("-fx-background-color: #FFFFFF;");
            back.setTextFill(BLACK);
        });

        back.setOnMouseExited((action)->{
            back.setStyle("-fx-background-color: #565656;");
            back.setTextFill(WHITE);
        });

        back.setOnAction(value -> {
            sound.play("buttonsRelax");
            menu.setScene("first");
            menu.closeClient();
        });

        this.setPadding(new Insets(30,50,50,50));
        
        this.setAlignment(box, Pos.CENTER_LEFT);
        this.setCenter(box);

        this.setAlignment(back,Pos.BOTTOM_RIGHT);
        this.setBottom(back);
    }

}
