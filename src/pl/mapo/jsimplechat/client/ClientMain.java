package pl.mapo.jsimplechat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        final String APP_NAME = "JSimpleChat";
        stage.setTitle(APP_NAME);
        Parent root = FXMLLoader.load(getClass().getResource("view/ClientPane.fxml"));
        Scene scene = new Scene(root,600,450);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
