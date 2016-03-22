package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Chris on 3/22/16.
 */
public class MTClientImpl {
    public static void main(String[] args) throws IOException {
        /** Create socket **/
        Socket socket = new Socket("127.0.0.1",2016);
        socket.setSoTimeout(60000);// 60 seconds time out

        /** Send messages to server **/
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream() , true);
        BufferedReader sys_in = new BufferedReader(new InputStreamReader(System.in));
        printWriter.println(sys_in.readLine());
        printWriter.flush();

        /** Receive messages from server **/
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String results = bufferedReader.readLine();
        System.out.println("Server Say:" + results);

        /** Close socket **/
        printWriter.close();
        bufferedReader.close();
        socket.close();
    }
}
