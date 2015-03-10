package pl.mapo.jsimplechat.lib;

public class Message {
    public enum Type {
        MESSAGE, CONNECTION, DISCONNECT, USERS, NOTHING, IS_USED, ID
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
            case USERS:
                text = "/u/"+message+"/e/";
                break;
            case DISCONNECT:
                text = "/d/"+message+"/e/";
                break;
            case IS_USED:
                text = "/o/"+message+"/e/";
                break;
            case ID:
                text = "/i/"+message+"/e/";
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
        } else if (message.startsWith("/c/")){
            return message.split("/c/|/e/")[1];
        } else if (message.startsWith("/d/")){
            return message.split("/d/|/e/")[1];
        } else if(message.startsWith("/o/")){
            return message.split("/o/|/e/")[1];
        } else if(message.startsWith("/i/")){
            return message.split("/i/|/e/")[1];
        } else {
            return "ERROR: Problem with unpack message!";
        }
    }

    public static String[] unpackOnlineUsers (String message){
        String[] usersOnline = message.substring(3).split("/n/|/e/");
        return usersOnline;
    }

    public static Type typeOf(String message){
        if (message.startsWith("/m/")){
            return Type.MESSAGE;
        } else if (message.startsWith("/u/")){
            return Type.USERS;
        } else if (message.startsWith("/c/")){
            return Type.CONNECTION;
        } else if (message.startsWith("/d/")){
            return Type.DISCONNECT;
        } else if (message.startsWith("/o/")){
            return Type.IS_USED;
        } else if (message.startsWith("/i/")){
            return Type.ID;
        } else {
            return Type.NOTHING;
        }
    }

}