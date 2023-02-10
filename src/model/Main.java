package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    /*
    The Main class that acts as the entry point of your JavaFX application. This class
    extends the Application class and overrides the start method to create and show the login
    window of your application.
    In the start method, you use the FXMLLoader class to load the FXML file login.fxml and
    set it as the root of your scene. You then set the title of the stage to "MessageGram",
    set the scene to have a size of 330 by 560, and make the stage non-resizable. Finally,
    you call primaryStage.show() to display the window on the screen.
    The main method simply calls the launch method from the Application class to start the
    JavaFX application.
     */


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("MessageGram");
        primaryStage.setScene(new Scene(root, 330, 560));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
