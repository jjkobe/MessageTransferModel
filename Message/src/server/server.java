package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static final int SERVER_PORT = 5555; // server port configuration
    private static final String SERVER_IP = "127.0.0.1"; // server ip configuration
    public static final Character ACCOUNT_SPLIT_TAG = '|'; // split tag
    public static int LOGGING_INTERVAL = 60000; // logging interval

    // Server message
    public static final String ACK = "ack";
    public static final String NAK = "nak";

    // Message handler
    public static final int BUF_SIZE = 1024;
    private ByteBuffer echoBuffer;
    private static FileWriteThreadInServer fileWriteThread; // Logging object

    // Constructor
    public Server() {
        echoBuffer = ByteBuffer.allocate(BUF_SIZE);
        fileWriteThread = new FileWriteThreadInServer();

    }

    public void buildServer(){

        try {
            /** Create socket channel **/
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(SERVER_IP, Server.SERVER_PORT) );

            /** Register selector **/
            Selector selector = Selector.open();
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            /** Server side listening **/
            while(true)
            {
                int num = selector.select();
                if(num < 1) {
                    continue;
                }

                Set selectedKeys=selector.selectedKeys();
                Iterator it=selectedKeys.iterator();
                while(it.hasNext()) {

                    SelectionKey key=(SelectionKey) it.next();

                    if((key.readyOps()&SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) { // New connection
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        SelectionKey newKey = socketChannel.register(selector,SelectionKey.OP_READ);
                        it.remove();

                        System.out.print("Get connection from: " + socketChannel);

                    } else if((key.readyOps()&SelectionKey.OP_READ) == SelectionKey.OP_READ) { // Account configuration
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        int bytesEchoed = 0;
                        while((bytesEchoed=socketChannel.read(echoBuffer)) > 0) {
                            System.out.println("bytesEchoed: " + bytesEchoed);
                        }

                        echoBuffer.flip();
                        System.out.println("Limit " + echoBuffer.limit());
                        byte [] content = new byte[echoBuffer.limit()];
                        echoBuffer.get(content);

                        // Account configuration
                        String accountConf = new String(content);
                        doLoginPost(accountConf, socketChannel);
                        echoBuffer.clear();
                        it.remove();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void doLoginPost(String account, SocketChannel socketChannel) {

        boolean isOK = false;

        int index=account.indexOf(Server.ACCOUNT_SPLIT_TAG);
        if(index > 0) {

            // account info
            String userName = account.substring(0,index);
            String passwd = account.substring(index+1);

            // query
            String sql="select name,password from login where name='" + userName + "'and password='" + passwd+"'";
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                conn = SqlConn.getConnection();
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            boolean queryTag = false;
            try {
                queryTag = rs.first();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            /** Authentication **/
            if(passwd == null) {
                passwd="";
            }
            if(userName != null) {

                if(queryTag) {
                    isOK = true;
                }
                else {
                    isOK = false;
                }

                String result = "";
                if(isOK) {
                    result = Server.ACK;
                    fileWriteThread.addContent(1);
                    System.out.println(Server.ACK);
                }
                else {
                    result = Server.NAK;
                    fileWriteThread.addContent(0);
                    System.out.println(Server.NAK);
                }

                ByteBuffer byteBuffer = ByteBuffer.allocate(result.length());
                byteBuffer.put(result.getBytes());
                byteBuffer.flip();
                try {
                    socketChannel.write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteBuffer.clear();
            }

        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.fileWriteThread.start();// Logging
        server.buildServer(); // Start server
    }

}

