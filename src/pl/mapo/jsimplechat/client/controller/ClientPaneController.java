package pl.mapo.jsimplechat.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

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
    private Tab tab;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureMenu();


    }


    private void configureMenu() {
        connectServerMenuItem.setOnAction(event -> {
            createConnectWindow();

            thread = new Thread("Menu") {
                @Override
                public void run() {
                    running = true;

                    while (running){
                        running = ConnectPaneController.isRunning();

                    }
                    Platform.runLater(() -> {
                        createNewTab();
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
            stage.setResizable(false);
            stage.setScene(new Scene(root, 300, 170));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createNewTab(){

            try {
                tab = FXMLLoader.load(getClass().getResource("/pl/mapo/jsimplechat/client/view/TabContent.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
    }




}
