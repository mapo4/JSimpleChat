package pl.mapo.jsimplechat.server;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;
import pl.mapo.jsimplechat.lib.Message;


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
        manageClients();
    }

    private void receive(){
        receive = new Thread("Receive") {

            @Override
            public void run() {

                    try {
                        String msg;

                        while ((msg = fromClient.readLine()) != null) {
                            manageMessage(msg);
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

    private void manageMessage(String message){
     if (Message.typeOf(message) == Message.Type.MESSAGE){
         sendToAll(message);
         System.out.println("Received: " + Message.unpack(message));
     } else if (Message.typeOf(message) == Message.Type.CONNECTION){
         //System.out.println(ServerMain.clientsOnline.size());
         ServerMain.clientsOnline.add(Message.unpack(message));
     } else if (Message.typeOf(message) == Message.Type.DISCONNECT){

         ServerMain.clientsOnline = ServerMain.clientsOnline.stream()
                 .filter(t -> !t.startsWith(Message.unpack(message))).collect(Collectors.toList());
     }

    }

    private void manageClients(){
        new Thread("manageClients"){

            @Override
            public void run() {
                while(true){
                    if (ServerMain.clientsOnline.size() < 0) return;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String message = "";
                    for (int i=0; i<(ServerMain.clientsOnline.size()-1);i++){
                        message += ServerMain.clientsOnline.get(i)+"/n/";
                    }
                    int lastIndex = ServerMain.clientsOnline.size()-1;
                    message += ServerMain.clientsOnline.get(lastIndex);
                    sendToAll(Message.pack(message, Message.Type.USERS));
                    System.out.println(Message.pack(message, Message.Type.USERS));

                }
            }
        }.start();

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
