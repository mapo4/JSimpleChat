package pl.mapo.jsimplechat.server;

public class Message {
    public enum Type {
        MESSAGE, CONNECTION
    }

    public static String pack(String message, Type typeOfMessage){
        String text;
        switch (typeOfMessage){
            case MESSAGE:
                text = "/m/"+message+"/e/";
                break;
            case CONNECTION:
                text = "/c/"+message+"/e/";
                break;
            default:
                text="";
                System.out.println("Invalid type of message!");
                break;
        }
        return text;
    }

    public static String unpack(String message){
        if (message.startsWith("/m/")){
            message = message.substring(3);
            message = message.split("/e/")[0];
            return message;
        } else {
            return "ERROR: Problem with unpack message!";
        }
    }

}
