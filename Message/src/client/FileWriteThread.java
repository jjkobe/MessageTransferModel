package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by lenovo on 2016/3/16.
 */
public class FileWriteThread extends Thread {

    // login log configurations
    public int succNum=0;
    public int failNum=0;

    private String logFileName = "clientLog";

    public static final int CLIENT_LOG_INTERVAL = 60000;
    @Override
    public void run() {
        while (true) {
            File logFile = new File(logFileName);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
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

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(CLIENT_LOG_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addContent(int con)
    {
        if(0 == con) {
            ++failNum;
        } else if(1 == con) {
            ++succNum;
        }
    }

}
