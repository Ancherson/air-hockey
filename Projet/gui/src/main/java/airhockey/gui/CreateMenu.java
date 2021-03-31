package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;



public class CreateMenu extends BorderPane {
    private Label creation;
    private Label checkRules;
    private TextField rules;
    private Button back;
    private Button enter;
    private String idRoom;

    public CreateMenu(MenuClient menu) {

        creation = new Label("creation de la room");
        back = new Button("back");

        checkRules = new Label("The number of rounds is correct");
        rules = new TextField("11");
        enter = new Button("enter");

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();

        creation.setPrefHeight(100);
        creation.setOnMouseClicked((event) -> {
            content.putString(idRoom);
            clipboard.setContent(content);
        } );

        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);

        back.setOnAction(value -> {
            menu.setScene(1);
            menu.closeClient();
        });

        checkRules.setPrefHeight(100);

        rules.setMaxSize(50,50);
        rules.setPrefSize(50,50);

        enter.setMaxSize(100,60);
        enter.setPrefSize(60,50);

        enter.setOnAction(value ->{
            int maxScore = Integer.parseInt(rules.getCharacters().toString());
            if(maxScore<5){
                checkRules.setText("You can't have a number of rounds less than 5");
            }else if(maxScore>21){
                checkRules.setText("You can't have number of rounds more than 21");
            }else{
                menu.getModel().setScoreMax(maxScore);
                checkRules.setText("The number of rounds is correct");
                
            }
        });

        this.setCenter(creation);
        this.setAlignment(creation, Pos.CENTER);


        this.setBottom(back);
        this.setAlignment(back,Pos.BASELINE_RIGHT);

        this.setRight(checkRules);
        this.setAlignment(checkRules,Pos.CENTER);

        this.setLeft(rules);
        this.setAlignment(rules,Pos.CENTER);

        this.setTop(enter);
        this.setAlignment(enter,Pos.BASELINE_RIGHT);

        this.setPadding(new Insets(30,50,50,50));

    }

    public void setID(String id){
        creation.setText(id);
        idRoom = id;

    }


}
