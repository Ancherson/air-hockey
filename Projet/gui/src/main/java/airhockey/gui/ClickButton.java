package airhockey.gui;

import javafx.scene.control.Button;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

/**
 * This class represents the click button in the GUI
 */
public class ClickButton extends Button {

    /**
     * Constructor of a click button without a name in param
     */
    public ClickButton() {
        this.setStyle("-fx-background-color: #565656;");
        this.setTextFill(WHITE);

        this.setOnMouseEntered((action)->{
            this.setStyle("-fx-background-color: #FFFFFF;");
            this.setTextFill(BLACK);
        });

        this.setOnMouseExited((action)->{
            this.setStyle("-fx-background-color: #565656;");
            this.setTextFill(WHITE);
        });
    }

    /**
     * Constructor of a click button with a name in param
     * @param name String of the label of the button
     */
    public ClickButton(String name) {
        super(name);
        this.setStyle("-fx-background-color: #565656;");
        this.setTextFill(WHITE);

        this.setOnMouseEntered((action)->{
            this.setStyle("-fx-background-color: #FFFFFF;");
            this.setTextFill(BLACK);
        });

        this.setOnMouseExited((action)->{
            this.setStyle("-fx-background-color: #565656;");
            this.setTextFill(WHITE);
        });
    }
}
