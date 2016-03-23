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
    public int succNum=0;
    public int failNum=0;
    @Override
    public void run() {
        while (true) {
            File file = new File("d:", "addfileinserver.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());
            try {
                FileWriter writer = new FileWriter("d:/addfileinserver.txt", true);
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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void addContent(int con)
    {
        if(con==0)
        {
            failNum++;
        }
        else if(con==1)
        {
            succNum++;
        }
    }
//    public static void main(String[] args) {
//        File file = new File("d:", "addfileinserver.txt");
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter("d:/addfileinserver.txt",true);
//            writer.write("次失败登陆");
//            writer.write("\r\n");
//            writer.write("\r\n");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
