package airhockey.javafx;

import Projet.MenuClient;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class View extends Pane {

    private MenuClient menu;
    private Canvas canvas;
    private GraphicsContext ctx;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private boolean isPressed;
    private double lastDragTime;
    //private Model model;


    public View(MenuClient menu) {

    }
}
