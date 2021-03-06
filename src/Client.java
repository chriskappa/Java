

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private static String userName;
    public Client(Socket socket,String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Getting output Streamer
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Getting input Streamer
        }
        catch(IOException e ) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
//            bufferedWriter.write("userName");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName+":"+ messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch(IOException e ) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }
                    catch(IOException e ) {
                        closeEverything(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
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


    public static void main (String[]args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat:");
        String username = scanner.next();
        userName = username;
        Socket socket = new Socket("localhost",8818);
        Client client = new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();

    }

}
