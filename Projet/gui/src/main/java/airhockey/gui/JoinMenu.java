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

/**
 * This class the page shown when you try to join a room in the GUI
 */

public class JoinMenu extends BorderPane {

    private Sound sound;
    private Label joinT;

    /**
     *  Constructor of the page JoinMenu
     * @param menu is the main Pane of the gui that allows to navigate to different page
     */
    public JoinMenu(MenuClient menu){

        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        sound = new Sound();

        joinT = new Label("Enter the id");
        TextField id = new TextField();
        ClickButton back = new ClickButton("Back");

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

    /**
     * Function that set the message of the Label that allows to show different message
     * like room full, bad ID and so on
     * @param message String that will be shown in the Label
     */
    public void setMessage(String message) {
        joinT.setText(message);
    }

}
