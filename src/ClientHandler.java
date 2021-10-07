

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class ClientHandler extends Thread {


    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    public String userName;




    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Getting output Streamer
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Getting input Streamer
            setUsername(bufferedReader.readLine()); // Getting Users Username
//            System.out.println("The username is "+this.userName);
            clientHandlers.add(this); // Adding the user to the arraylist
            System.out.println(clientHandlers.size());
            broadcastMessage("SERVER: "+userName+" has entered the chat!");
        }
        catch(IOException e) {
            closeConnections(socket,bufferedReader,bufferedWriter);
        }
    }




    public void run() {

        String messageFromClient;
        // We are connected to client
        while(socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            }
            catch(IOException e) {
                closeConnections(socket,bufferedReader,bufferedWriter);
                break;
            }

        }

    }

    public void broadcastMessage(String messageToSend) {

        for(ClientHandler clientHandler : clientHandlers) {
            try {
                // Checking if the current client is not the same person who sends the message
                if(clientHandler.userName.equals(userName) ) {
                    System.out.println("Sending message to "+clientHandler.userName);
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine(); // Getting to new line and buffer is not waiting for any new data
                    clientHandler.bufferedWriter.flush(); // Flushiing buffer before it gets full
                }


            }
            catch(IOException e) {
                closeConnections(socket,bufferedReader,bufferedWriter);
            }

        }
    }


    public void removeClientHanlder() {
        clientHandlers.remove(this);
        broadcastMessage("Server: "+userName+" user Has Left The Chat!");

    }

    public void closeConnections(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
        removeClientHanlder(); // Removing ClientHandler
        try {
            // Closing BufferedReader
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            // Closing BufferedWriter
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            // Closing Socket
            if(socket != null) {
                socket.close();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username){
        this.userName = username;
    }

}
