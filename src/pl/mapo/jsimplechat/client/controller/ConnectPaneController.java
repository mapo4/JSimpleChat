package pl.mapo.jsimplechat.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import pl.mapo.jsimplechat.client.model.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectPaneController implements Initializable {

    @FXML
    private TextField portTextField;

    @FXML
    private Button newServerButton;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button removeServerButton;

    @FXML
    private Button changeNameServerButton;

    @FXML
    private Button cancelButoon;

    @FXML
    private ListView<Client> serverListView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button connectButton;

    @FXML
    private Label portLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label usernameLabel;

    private ObservableList<Client> clientProperties;

    private static Client client;
    private static boolean running;
    private static boolean canceled;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientProperties = FXCollections.observableArrayList();
        clientProperties.add(new Client("mapo","My server", "localhost","8195"));

        running = true;
        canceled = false;

        configureListView();
        configureButton();
        configureTextFields();
        configureLabel();
    }

    private void configureLabel(){
        portLabel.setTextFill(Color.web("#DB2B2B"));
    }

    private void configureListView(){
        serverListView.setItems(clientProperties);
        serverListView.setEditable(true);

        serverListView.getSelectionModel().selectedItemProperty().addListener(event -> {
            showServerDetails();
        });

        serverListView.setCellFactory(list -> {
            return new ListCell<Client>(){
                private TextField textField;
                @Override
                public void startEdit() {
                    super.startEdit();
                    createCellTextField();
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }


                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                    if (getSelectedItem() != null) {
                        textProperty().bind(getSelectedItem().serverNameProperty());
                    }
                }

                @Override
                protected void updateItem(Client item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        textProperty().bind(item.serverNameProperty());
                    }
                }

                private void createCellTextField() {
                    textField = new TextField(getSelectedItem().getServerName());

                    textField.setOnAction(event -> {
                        getSelectedItem().setServerName(textField.getText());
                        cancelEdit();
                    });
                }
            };
        });
    }

    private Client getSelectedItem(){
        return serverListView.getSelectionModel().getSelectedItem();
    }


    private void configureTextFields(){
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            getSelectedItem().setName(newValue);
        });
        portTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            newValue.replaceAll("\\s","");
            if (newValue.matches("\\d*")) {
                getSelectedItem().setServerPort(newValue);
                portLabel.setText("");
            }
            else {
                portLabel.setText("Incorrect data!");
                getSelectedItem().setServerPort(oldValue);
            }
        });
        addressTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            newValue.replaceAll("\\s","");
            getSelectedItem().setServerAddress(newValue);
        });
    }

    private void configureButton(){

        changeNameServerButton.setOnAction(event -> {
            Client clientProperty = serverListView.getSelectionModel().getSelectedItem();
            clientProperty.setServerName("My favorite");

        });

        connectButton.setOnAction(event ->{

            client = getSelectedItem();

            running = false;
            ((Node)(event.getSource())).getScene().getWindow().hide();

        });

        cancelButoon.setOnAction(event -> {
            canceled = true;
            running = false;
            ((Node)(event.getSource())).getScene().getWindow().hide();

        });

        newServerButton.setOnAction(event -> {
            clientProperties.add(new Client("New server"));
        });
    }

    private void showServerDetails(){


        addressTextField.setText(getSelectedItem().getServerAddress());

        usernameTextField.setText(getSelectedItem().getName());

        portTextField.setText(getSelectedItem().getServerPort());


    }

    public static boolean isRunning() {
        return running;
    }

    public static Client getClient() {
        return client;
    }

    public static boolean isCanceled() {
        return canceled;
    }
}