package airhockey.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class EndMenu extends BorderPane{
    
    private Button back;
    private Label message;

    private MenuClient menu;
    private boolean won;

    public EndMenu(MenuClient menu, boolean won){
        this.menu = menu;
        this.won = won;

        back = new Button("back");
        message = new Label();

        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);

        back.setOnAction(value->{
            menu.setScene(1);
        });

        if(won){
            message.setText("Vous avez gagné!");
        }else{
            message.setText("Vous avez perdu au jeu! T'es nul");
        }

        this.setTop(back);
        this.setAlignment(back, Pos.TOP_LEFT);

        this.setCenter(message);
        this.setAlignment(message, Pos.CENTER);
    }

   
}
