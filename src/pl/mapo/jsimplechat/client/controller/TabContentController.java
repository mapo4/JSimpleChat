package pl.mapo.jsimplechat.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.mapo.jsimplechat.client.Network;
import pl.mapo.jsimplechat.client.model.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class TabContentController implements Initializable{

    @FXML
    private TextArea messageHistoryTextArea;

    @FXML
    private TextField messageTextField;

    @FXML
    private Tab tab;

    private Network network;
    private Client client;
    private Thread listen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = ConnectPaneController.getClient();

        configureTab();
        configureMessageTextField();
        establishConnection();
        listen();

    }




    private void establishConnection(){
        String serverAddress = client.getServerAddress();
        int serverPort = client.getServerPort();
        network = new Network(serverAddress, serverPort);

        boolean connection = network.openConnection();

        if (connection){
            addNewClientMessage("Connection is already.");
        } else {
            addNewClientMessage("Connection is failed.");
        }

    }


    private void configureTab(){
        tab.setText(client.getName());
        tab.setOnClosed(event -> {
            network.close();

        });
    }


    private void listen(){
        listen = new Thread("Listen"){
            @Override
            public void run() {
                while (true){
                    String message = network.receive();
                    Platform.runLater(() -> addNewClientMessage(message));
                }
            }
        };
        listen.start();
    }


    private void configureMessageTextField(){
        messageTextField.setOnAction(event -> {
            String message = messageTextField.getText();
            network.send(message);
            messageTextField.clear();
        });
    }

    public void addNewClientMessage(String message){

        messageHistoryTextArea.appendText(message+"\n");

    }
}
