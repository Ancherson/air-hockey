/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package airhockey.javafx;

public class App /*extends Application*/ {
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args){
        MenuClient.launch(MenuClient.class, args);
    }

    /*public void start(Stage primaryStage) throws Exception{
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.show();

    }*/
}