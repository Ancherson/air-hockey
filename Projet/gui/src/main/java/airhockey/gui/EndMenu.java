package airhockey.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.BLACK;

public class EndMenu extends BorderPane{
    
    private ClickButton back;
    private Label message;

    private Sound sound;
    private MenuClient menu;
    private boolean won;

    public EndMenu(MenuClient menu, boolean won){
        this.menu = menu;
        this.won = won;

        sound = new Sound();

        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        message = new Label();
        message.setMinHeight(100);
        message.setMaxHeight(100);        
        message.setStyle("-fx-font : 28 Ubuntu;");
        message.setTextFill(WHITE);

        back = new ClickButton("back");
 

        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);
        
        back.setOnAction(value->{
            sound.play("buttonsRelax");
            menu.setScene("first");
        });

        if(won){
            message.setText("Congratulations! You won!");
        }else{
            message.setText("Bravo, you are the worst player in this game");
        }

        this.setBottom(back);
        this.setAlignment(back, Pos.BOTTOM_RIGHT);

        this.setCenter(message);
        this.setAlignment(message, Pos.CENTER);
    }

   
}
