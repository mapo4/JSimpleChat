package pl.mapo.jsimplechat.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import pl.mapo.jsimplechat.client.model.Client;

public class TabContent {

    private Tab tab;
    private TextField msgTextField;
    private TextArea msgHistoryTextArea;
    private BorderPane borderPane;
    private Network network;

    private String address;
    private int port;

    public TabContent(String tabName, String address, int port) {
        tab = new Tab();
        msgHistoryTextArea = new TextArea();
        msgTextField = new TextField();
        borderPane = new BorderPane();
        this.address = address;
        this.port = port;


        configureMsgHistoryTextArea();
        configureMsgTextField();
        configureBorderPane();
        configureTab(tabName);
        configureCloseTab();

        establishConnection();

    }


    private void establishConnection(){
        /*Client client = new Client("mapo","localhost", 8195);
        String serverAddress = client.getServerAddress();
        int serverPort = client.getServerPort();*/


        network = new Network(address, port);

        boolean connection = network.openConnection();

        if (connection){
            newClientMessage("Connection is already.");
        } else {
            newClientMessage("Connection is failed.");
        }
    }

    private void configureBorderPane(){
        borderPane.setCenter(msgHistoryTextArea);
        borderPane.setBottom(msgTextField);
        borderPane.setPadding(new Insets(8,8,8,8));
    }

    private void configureTab(String nameTab){
        tab.setText(nameTab);
        tab.setContent(borderPane);
    }

    private void configureMsgHistoryTextArea(){
        msgHistoryTextArea.setEditable(false);
    }

    private void configureMsgTextField(){
        msgTextField.setOnAction(event -> {
            String message ="Id:"+tab.getTabPane().getTabs().indexOf(tab)+" - "+ msgTextField.getText();
            newClientMessage(message);
            network.send(message);
            msgTextField.clear();
        });
    }

    private void newClientMessage(String message){
        msgHistoryTextArea.appendText(message + "\n");
    }

    private void configureCloseTab(){
        tab.setOnClosed(e -> {
            network.close();
            System.out.println("Tab closed.");
        });
    }

    public Tab getTab() {
        return tab;
    }
}
