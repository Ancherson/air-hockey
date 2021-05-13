package airhockey.gui;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import static javafx.scene.paint.Color.*;

/**
 * This class the page shown when you create a room in the GUI
 */

public class CreateMenu extends BorderPane {
    private Label creation;
    private Label copied;
    private String idRoom;
    private Sound sound;
    private int num;

    /**
     *  Constructor of the page CreateMenu
     * @param menu is the main Pane of the gui that allows to navigate to different page
     */

    public CreateMenu(MenuClient menu) {

        sound = new Sound();
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        creation = new Label("CREATION OF THE ROOM");
        creation.setPrefHeight(50);
        creation.setStyle("-fx-font : 28 Ubuntu;");
        creation.setTextFill(LIGHTGRAY);
        ClickButton back = new ClickButton("Back");

        copied = new Label ();
        copied.setTextFill(WHITE);
        copied.setVisible(true);

        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);

        back.setOnAction(value -> {
            sound.play("buttonsRelax");
            menu.setScene("first");
            menu.closeClient();
        });

        VBox box = new VBox(0,creation,copied);
        box.setAlignment(Pos.CENTER);
        this.setCenter(box);
        this.setAlignment(box, Pos.CENTER);

        this.setBottom(back);
        this.setAlignment(back,Pos.BASELINE_RIGHT);

        this.setPadding(new Insets(30,50,50,50));

    }

    /**
     * A function that sets the text of the Label
     * @param id is the text shown in the Label which is the ID of the room created
     */
    public void setID(String id){
        creation.setText(id);
        idRoom = id;

        copied.setText("Click on the ID !");

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();


        creation.setOnMouseClicked((ActionEvent) -> {
            content.putString(idRoom);
            clipboard.setContent(content);
            switch (num){
                case 0: copied.setText("Copied");
                    break;
                case 1: copied.setText("Double copy!");
                    break;
                case 2: copied.setText("Triple copy!");
                    break;
                case 3: copied.setText("Killing spree!");
                    break;
                case 4: copied.setText("Domination!");
                    break;
                case 5: copied.setText("On fire !");
                    copied.setTextFill(RED);
                    break;
                case 9:
                    num = 0;
                    copied.setTextFill(WHITE);
                    copied.setText("Click on the ID !");
                    copied.setVisible(true);
                default :
                    break;
            }
            copied.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1.5)
            );
            visiblePause.setOnFinished(event -> {
                        if (!copied.getText().equals("Click on the ID !")) {
                            copied.setVisible(false);
                        }
                    }
            );
            visiblePause.play();
            num++;
        } );
    }


}
