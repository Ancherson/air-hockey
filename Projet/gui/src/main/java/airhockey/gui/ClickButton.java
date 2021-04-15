package airhockey.gui;

import javafx.scene.control.Button;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class ClickButton extends Button {

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
