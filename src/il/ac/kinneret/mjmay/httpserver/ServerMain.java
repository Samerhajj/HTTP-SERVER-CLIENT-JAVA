package il.ac.kinneret.mjmay.httpserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerMain extends Application {

    public static Stage theStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("httpserverwindow.fxml"));
        primaryStage.setTitle("HTTP Server Tool");
        primaryStage.getIcons().add(new Image("file:./tornado.png"));
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();

        theStage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
