package server;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * Created by Chris on 3/27/16.
 */

public class MessageProcessThread extends Thread{
    public static int LISTENING_INTERVAL = 1000;
    public static int TOTAL_SEND_LIMIT = 100;
    public static int PS_SEND_LIMIT = 5;


    private Queue<String> messageQueue;
    private boolean loginStatus;
    private int currentSendCount;
    private int totalSendCount;

    private SelectionKey currentKey;
    private Selector currentSelector;

    //Constructor
    public MessageProcessThread(SelectionKey key , Selector selector) {
        loginStatus = false;
        currentSendCount = 0;
        totalSendCount = 0;

        this.currentKey = key;
        this.currentSelector = selector;

    }

    // Login status modifier
    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;

    }
    public boolean getLoginStatus() {
        return this.loginStatus;
    }

    // Message processing
    public synchronized void sendMessage(String content) {
        if( !loginStatus || currentSendCount > PS_SEND_LIMIT) {
            return;
        } else if(totalSendCount > TOTAL_SEND_LIMIT) {
            this.setLoginStatus(false);
            return;
        } else  {
            messageQueue.offer(content);
            totalSendCount += 1;
            currentSendCount += 1;

            return;
        }
    }

    // Message broadcast
    public synchronized void doBroadCast(Selector selector , String content) throws IOException {
        //广播数据到所有的SocketChannel中
        for(SelectionKey key : selector.keys())
        {
            Channel targetchannel = key.channel();
            //如果except不为空，不回发给发送此内容的客户端
            if(targetchannel instanceof SocketChannel && targetchannel != this.currentKey)
            {
                SocketChannel dest = (SocketChannel)targetchannel;
                dest.write(Server.CHARSET.encode(content));
            }
        }
    }


    @Override
    public void run() {
        while (loginStatus) {
            for(String content : this.messageQueue) {
                try {
                    this.doBroadCast(this.currentSelector, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(LISTENING_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.messageQueue.clear();
            this.currentSendCount = 0;
        }

    }
}
