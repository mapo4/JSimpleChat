package pl.mapo.jsimplechat.server;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;
import pl.mapo.jsimplechat.lib.Message;


public class Server extends Thread {

    private Socket socket;
    private Thread receive, send, listen;
    private volatile boolean running;
    private BufferedReader fromClient;
    private String username, message;


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
        running = true;
        listen();
        manageClients();
    }

    private void listen(){
        listen = new Thread("Listen"){
            @Override
            public void run() {
                while (running){
                    String message = receive();

                    if (message != null) manageMessage(message);
                }
            }
        };
        listen.start();
    }

    private String receive(){

        try {
            if ((message = fromClient.readLine()) != null) {
                return message;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void manageMessage(String message){
     if (Message.typeOf(message) == Message.Type.MESSAGE){
         sendToAll(message);
         System.out.println("Received: " + Message.unpack(message));
     } else if (Message.typeOf(message) == Message.Type.CONNECTION){
         //System.out.println(ServerMain.clientsOnline.size());
         ServerMain.clientsOnline.add(Message.unpack(message));
         username = Message.unpack(message);
     } else if (Message.typeOf(message) == Message.Type.DISCONNECT){
         disconnect(message);
         running = false;
     }

    }

    private void manageClients(){
        new Thread("manageClients"){

            @Override
            public void run() {
                while(running){
                    if (ServerMain.clientsOnline.size() < 0) return;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String message = "";

                    try {
                        for (int i=0; i<(ServerMain.clientsOnline.size()-1);i++){
                            message += ServerMain.clientsOnline.get(i)+"/n/";
                        }
                        int lastIndex = ServerMain.clientsOnline.size()-1;
                        message += ServerMain.clientsOnline.get(lastIndex);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return;
                    }
                    sendToAll(Message.pack(message, Message.Type.USERS));
                    System.out.println(Message.pack(message, Message.Type.USERS)+" : "+ServerMain.clients.size());

                }
            }
        }.start();

    }

    private void disconnect(String username){
        int indexOfUser = ServerMain.clientsOnline.indexOf(Message.unpack(username));
        ServerMain.clientsOnline = ServerMain.clientsOnline.stream()
                .filter(t -> !t.startsWith(Message.unpack(username))).collect(Collectors.toList());
        ServerMain.clients.remove(indexOfUser);

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
                        return;
                    }
                }
            }
        };
        send.start();
    }

}
