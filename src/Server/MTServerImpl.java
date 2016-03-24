package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Chris on 3/22/16.
 */
public class MTServerImpl {
    public static void main(String[] args) throws IOException {
        /** Create a server socket **/
        ServerSocket serverSocket = new ServerSocket(2016); // Create a server process listening on port 2016

        while(true) {
            // Listen for connections connect to the server
            Socket socket = serverSocket.accept();

            /** Fetch messages from the client**/
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String results = bufferedReader.readLine();
            System.out.println("Client Say:" + results);

            /** Send messages to client **/
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.print("Hello Client , I am server.");
            printWriter.flush();

            /** Close the server **/
            printWriter.close();
            bufferedReader.close();
            socket.close();
        }
    }
}
