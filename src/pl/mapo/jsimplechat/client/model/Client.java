package pl.mapo.jsimplechat.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {

    private StringProperty id, name, serverAddress,serverName, serverPort;

    public Client(String serverName){
        this.name = new SimpleStringProperty("");
        this.serverAddress = new SimpleStringProperty("");
        this.serverName = new SimpleStringProperty(serverName);
        this.serverPort = new SimpleStringProperty("");

    }

    public Client(String name, String serverName, String serverAddress, String serverPort) {
        this.name = new SimpleStringProperty(name);
        this.serverAddress = new SimpleStringProperty(serverAddress);
        this.serverName = new SimpleStringProperty(serverName);
        this.serverPort = new SimpleStringProperty(serverPort);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getServerAddress() {
        return serverAddress.get();
    }

    public StringProperty serverAddressProperty() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress.set(serverAddress);
    }

    public String getServerPort() {
        return serverPort.get();
    }

    public StringProperty serverPortProperty() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort.set(serverPort);
    }

    public String getServerName() {
        return serverName.get();
    }

    public StringProperty serverNameProperty() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName.set(serverName);
    }


}
