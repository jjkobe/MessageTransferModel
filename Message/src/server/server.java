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
public class server {
    public static final int serverport =5555;
    public static final String isack="ack";
    public static final String isnak="nak";
    private  ByteBuffer echobuffer=ByteBuffer.allocate(1024);
    private static FileWriteThreadInServer fileWriteThread=new FileWriteThreadInServer();
    public server() {

    }
    public static void main(String[] args) {
        fileWriteThread.start();
        new server().buildserver();
    }

    public void buildserver(){
        try {
            ServerSocketChannel ssc=ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss=ssc.socket();
            ss.bind(new InetSocketAddress("10.0.1.6",serverport));
            Selector selector=Selector.open();
            SelectionKey skey=ssc.register(selector, SelectionKey.OP_ACCEPT);
            while(true)
            {
                int num=selector.select();
                if(num<1)
                {
                    continue;
                }
                Set selectedKeys=selector.selectedKeys();
                Iterator it=selectedKeys.iterator();
                while(it.hasNext())
                {
                    SelectionKey key=(SelectionKey) it.next();
                    if((key.readyOps()&SelectionKey.OP_ACCEPT)==SelectionKey.OP_ACCEPT)
                    {
                        ServerSocketChannel serverchannel=(ServerSocketChannel) key.channel();
                        SocketChannel sc=serverchannel.accept();
                        sc.configureBlocking(false);
                        SelectionKey newKey=sc.register(selector,SelectionKey.OP_READ);
                        it.remove();
                        System.out.print("get connection from"+sc);

                    }
                    else{
                        if((key.readyOps()&SelectionKey.OP_READ)==SelectionKey.OP_READ)
                        {
                            SocketChannel sc=(SocketChannel) key.channel();
                            int bytesEchoed=0;
                            while((bytesEchoed=sc.read(echobuffer))>0)
                            {
                                System.out.println("bytesEchoed"+bytesEchoed);
                            }
                            echobuffer.flip();
                            System.out.println("limet "+echobuffer.limit());
                            byte [] content =new byte[echobuffer.limit()];
                            echobuffer.get(content);
                            String result=new String(content);
                            doPost(result,sc);
                            echobuffer.clear();
                            it.remove();
                        }
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public  void doPost(String str, SocketChannel sc) {
        boolean isok=false;
        int index=str.indexOf('|');
        if(index>0)
        {
            String name=str.substring(0,index);
            String pswd=str.substring(index+1);
            String sql="select name,password from login where name='"+name+"'and password='"+pswd+"'";
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = SqlConn.getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stmt = conn.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ResultSet rs = null;
            try {
                rs = stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            boolean f=false;
            try {
                f=rs.first();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if(pswd==null)
            {
                pswd="";
            }if(name!=null)
        {
            if(f)
            {
                isok=true;

            }
            else{
                isok=false;

            }
            String result="";
            if(isok)
            {
                result="ack";
                fileWriteThread.addContent(1);
                System.out.println("ack");
            }
            else{
                result="nak";
                fileWriteThread.addContent(0);
                System.out.println("nak");
            }
            ByteBuffer bb=ByteBuffer.allocate(result.length());
            bb.put(result.getBytes());
            bb.flip();
            try {
                sc.write(bb);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            bb.clear();
        }

        }

    }
}

