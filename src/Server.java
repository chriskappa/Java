
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        try {
//			Checcking while its not closed
            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new user has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler); // Creating Thread
                thread.start(); // Starting the Thread
            }

        }
        catch(IOException e ) {
            System.out.println("Error"+e);
        }

    }

    public void closeServer() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }
        catch(IOException e) {
            System.out.println("Error"+e);
        }
    }

    public static void main (String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(8818);
        Server server = new Server(serverSocket);
        server.runServer();

    }
}
