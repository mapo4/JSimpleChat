package pl.mapo.jsimplechat.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.mapo.jsimplechat.client.Network;
import pl.mapo.jsimplechat.client.model.Client;
import pl.mapo.jsimplechat.lib.Message;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TabContentController implements Initializable{

    @FXML
    private TextArea messageHistoryTextArea;

    @FXML
    private TextField messageTextField;

    @FXML
    private Tab tab;

    @FXML
    private ListView<String> clientListView;

    private Network network;
    private Client client;
    private Thread listen;
    private volatile boolean running;
    private ObservableList<String> clients;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = ConnectPaneController.getClient();
        running = true;
        ClientPaneController.nameClientsFormTabs.add(client.getName());

        configureTab();
        configureMessageTextField();
        establishConnection();
        listen();

    }


    private void establishConnection(){
        String serverAddress = client.getServerAddress();
        int serverPort = Integer.parseInt(client.getServerPort());
        network = new Network(serverAddress, serverPort);

        boolean connection = network.openConnection();

        if (connection){
            network.send(Message.pack(client.getName(), Message.Type.CONNECTION));
            addNewClientMessage("Connection is already.");
        } else {
            addNewClientMessage("Connection is failed.");
        }

    }


    private void configureTab(){
        tab.setText(client.getServerName());
        tab.setOnClosed(event -> {
            network.send(Message.pack(client.getName(), Message.Type.DISCONNECT));
            ClientPaneController.nameClientsFormTabs = ClientPaneController.nameClientsFormTabs.stream()
                    .filter(t -> !t.startsWith(Message.unpack(client.getName()))).collect(Collectors.toList());
            running = false;
            network.close();
        });
    }


    private void listen(){
        listen = new Thread("Listen"){
            @Override
            public void run() {
                try {
                    while (running){
                        String message = network.receive();
                        if (Message.typeOf(message) == Message.Type.MESSAGE){
                            Platform.runLater(() -> addNewClientMessage(Message.unpack(message)));
                        } else if (Message.typeOf(message) == Message.Type.USERS){
                            String[] users = Message.unpackOnlineUsers(message);
                            clients = FXCollections.observableArrayList(Arrays.asList(users));
                            Platform.runLater(() -> clientListView.setItems(clients));
                        }

                    }
                } catch (Exception e) {
                    return;
                }
            }
        };
        listen.start();
    }


    private void configureMessageTextField(){
        messageTextField.setOnAction(event -> {
            String message = client.getName()+": "+messageTextField.getText();
            network.send(Message.pack(message, Message.Type.MESSAGE));
            messageTextField.clear();
        });
    }

    public void addNewClientMessage(String message){

        messageHistoryTextArea.appendText(message+"\n");

    }
}

