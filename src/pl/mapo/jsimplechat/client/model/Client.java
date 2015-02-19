package pl.mapo.jsimplechat.client.model;

import java.net.InetAddress;

public class Client {

    private String id, name, serverAddress;
    private int serverPort;


    public Client(String name, String serverAddress, int serverPort) {
        this.name = name;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }


}
