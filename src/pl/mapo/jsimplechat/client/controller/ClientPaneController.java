package pl.mapo.jsimplechat.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.mapo.jsimplechat.client.TabContent;
import pl.mapo.jsimplechat.client.model.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientPaneController implements Initializable{

    @FXML
    private MenuItem connectServerMenuItem;

    @FXML
    private TabPane tabPane;

    private Thread thread;
    private volatile boolean running;
    private BorderPane borderPane;
    private Tab tab;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureMenu();


    }




    private void configureMenu() {
        connectServerMenuItem.setOnAction(event -> {
            createConnectWindow();
            running = true;
            thread = new Thread("Menu") {
                @Override
                public void run() {
                    while (running){
                        running = ConnectPaneController.isRunning();
                    }
                    Platform.runLater(() -> {
                        createNewTab(ConnectPaneController.getTabName());
                    });
                }
            };
            thread.start();
        });
    }


    private void createConnectWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pl/mapo/jsimplechat/client/view/ConnectPane.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Connect");
            stage.setScene(new Scene(root, 300, 150));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createNewTab(String tabName){

            try {
                tab = FXMLLoader.load(getClass().getResource("/pl/mapo/jsimplechat/client/view/TabContent.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
    }




}
