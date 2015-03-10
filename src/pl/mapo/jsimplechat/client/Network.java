package pl.mapo.jsimplechat.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


public class Network{

    private String address;
    private InetAddress serverAddress;
    private int serverPort;
    private Socket clientSocket;
    private Thread send;
    private BufferedReader fromServer;
    private DataOutputStream toServer;
    private String message;


    public Network(String address, int serverPort) {

        this.address = address;
        this.serverPort = serverPort;

    }


    public boolean openConnection(){

        try {
            serverAddress = InetAddress.getByName(address);
            clientSocket = new Socket(serverAddress, serverPort);
            toServer = new DataOutputStream(clientSocket.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Server disconnected.");
            return false;
        }
        return true;

    }


    public void send(String message){
        send = new Thread("Send") {


            @Override
            public void run() {

                try {
                    toServer.writeBytes(message + "\n");
                    toServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    return;
                }
            }
        };
        send.start();
    }
    
    public String receive() {

        try {
            if ((message = fromServer.readLine()) != null) {
                return message;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void close(){
        System.out.println("Tab closed.");
/*
        try {

            fromServer.close();
            toServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

}
