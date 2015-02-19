package pl.mapo.jsimplechat.server;

import java.io.*;
import java.net.Socket;


public class Server extends Thread {

    private Socket socket;
    private Thread receive, send;
    private BufferedReader fromClient;


    public Server(Socket socket) {
        this.socket = socket;

        try {

            fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        receive();
    }

    private void receive(){
        receive = new Thread("Receive") {

            @Override
            public void run() {

                    try {
                        String msg;

                        while ((msg = fromClient.readLine()) != null) {


                            System.out.println("Received: " + msg);
                            sendToAll(msg);

                        }

                        fromClient.close();

                        socket.close();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        };
        receive.start();
    }

    public void sendToAll(String message){
        send = new Thread("Send") {
            @Override
            public void run() {
                DataOutputStream toClient;
                for (int i = 0; i<ServerMain.clients.size(); i++) {
                    Socket clientSocket = ServerMain.clients.get(i);
                    try {
                        toClient = new DataOutputStream(clientSocket.getOutputStream());
                        toClient.writeBytes(message + "\n");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        send.start();
    }

}
