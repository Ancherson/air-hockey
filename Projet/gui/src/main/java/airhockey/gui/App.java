/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package airhockey.gui;

import airhockey.network.Client;

public class App {
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args){
        if(args.length > 1) {
            System.out.println("Too much arguments");
            System.exit(0);
        }
        if(args.length == 1) {
            Client.setHostname(args[0]);
        }
        MenuClient.launch(MenuClient.class, args);
    }
}
