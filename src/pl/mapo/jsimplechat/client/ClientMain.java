package pl.mapo.jsimplechat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientMain extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {

        final String APP_NAME = "JSimpleChat";
        this.stage = stage;
        stage.setTitle(APP_NAME);
        Parent root = FXMLLoader.load(getClass().getResource("view/ClientPane.fxml"));
        Scene scene = new Scene(root,800,600);

        stage.setScene(scene);
        stage.show();


    }

    public static void setEvent(MethodAsParameter methodAsParameter){
        stage.addEventHandler(WindowEvent.WINDOW_HIDING, event -> {
            methodAsParameter.run();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

