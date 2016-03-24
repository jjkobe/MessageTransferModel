package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by lenovo on 2016/3/16.
 */
public class FileWriteThreadInServer extends Thread {

    // login log configurations
    public int succNum = 0; // success login
    public int failNum = 0; // invalid login

    private String logFileName = "ServerLog";

    @Override
    public void run() {

        // System log update
        while (true) {
            File logFile = new File(logFileName);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());

            try {
                FileWriter writer = new FileWriter(logFile , true);
                writer.write(date);
                writer.write("\r\n");
                writer.write("有" + succNum + "次成功登陆    " + "有" + failNum + "次失败登陆");
                writer.write("\r\n");
                writer.close();
                succNum = 0;
                failNum = 0;

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Heartbeat
            try {
                Thread.sleep(Server.LOGGING_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addContent(int con) {
        if(0 == con) {
            ++failNum; // login failed
        } else if(1 == con) {
            ++succNum; // login success
        }
    }

}
