package pl.mapo.jsimplechat.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServerMain {

    public static List<Socket> clients;
    public static List<String> clientsOnline;
    private static final int PORT = 8195;


    public static void main(String[] args) {
        clients = new ArrayList<>();
        clientsOnline = new LinkedList<>();
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running "+PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }



        while(true){
            try {
                socket = serverSocket.accept();
                clients.add(socket);
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }

            new Server(socket).start();
        }

    }

    /*private static String usersOnlineToMessage(){
        String message
    }*/
}
