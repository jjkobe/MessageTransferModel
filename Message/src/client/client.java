package client;

import UI.SendMsg;
import java.awt.*;
import server.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;


public class Client extends JFrame implements ActionListener{
    private JLabel jLabel1;
    private JTextField usertext;
    private JPasswordField passtext;
    private JTextField ipadddr;
    private JLabel hint;
    public static Client cli;
    private static FileWriteThread fileWriteThread=new FileWriteThread();
    String name;
    String password;

    public Client() {
        Container conn = this.getContentPane();//加入一个容器

        conn.setLayout(null);//绝对布局

        Font font = new Font("宋体",Font.BOLD,15);//创建一个字体以便其它调用

        this.setSize(337,300);


        //在容器里面设置背景
        conn.setBackground(Color.getHSBColor(30, 20, 120));


        JLabel jl2 = new JLabel("User Name:");

        jl2.setBounds(40,80,90,20);

        conn.add(jl2);

        //账号的文本框

        usertext = new JTextField();

        usertext.setBounds(150,80,150,20);

        usertext.setFont(font);

        conn.add(usertext);

        JLabel jl3 = new JLabel("Password:");

        jl3.setBounds(40,120,90,20);

        jl3.setForeground(Color.red);//字体颜色

        conn.add(jl3);

        passtext = new JPasswordField();

        passtext.setBounds(150,120,150,20);

        passtext.setFont(font);

        conn.add(passtext);

        JLabel jls = new JLabel("Server Address:");

        jls.setBounds(40,160,100,20);

        conn.add(jls);

        //账号的文本框

        ipadddr = new JTextField();

        ipadddr.setBounds(150,160,150,20);

        ipadddr.setFont(font);

        conn.add(ipadddr);
        //状态

        JButton jb1 = new JButton("Login");

        jb1.setBounds(140,220,80,20);


        conn.add(jb1);

        jb1.addActionListener(this);

        JButton jb2 = new JButton("Exit");

        jb2.setBounds(230,220,80,20);

        conn.add(jb2);

        jb2.addActionListener(this);

        hint = new JLabel();
        getContentPane().add(hint);
        hint.setBounds(140, 12, 172, 23);

        this.setTitle("Login");

        this.setResizable(false);//设置尺寸不可改变

        this.setLocationRelativeTo(null);//设置居中

        this.setDefaultCloseOperation(3);//当点击关闭时进行关闭

        this.setVisible(true);

    }

    public static void main(String[] args) {
        fileWriteThread.start();
        cli=new Client();
        cli.acMsg();
    }

    //接收服务端信息
    public void acMsg(){
        try {
            Socket s = new Socket("127.0.0.1", Server.SERVER_PORT);
            InputStream is;
            while (true) {
                is = s.getInputStream();
                int len = is.available();
               // System.out.println("length: " + len);
                byte[] bytes = new byte[len];
                is.read(bytes);
                String result = new String(bytes);
                System.out.println("result: " + result);//输出到界面？？？？？？？？？？？？
               //response
                OutputStream osRe;
                osRe=s.getOutputStream();
                osRe.write("Received......".getBytes());
                osRe.flush();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String getCommand = e.getActionCommand();

        if("Login".equals(getCommand)) {

            name = usertext.getText().trim();
            password = passtext.getText().trim();
            if (password.equals("")) {
                password = "";
            }
            if (name != null && name.length() > 0) {
                hint.setText("正在验证客户端，请稍后......");
                start();
            }
        }
        else if("Exit".equals(getCommand)){

            this.dispose();

        }
    }

    public  void start() {

        new Thread(new Runnable(){

            public void run() {
                try {
                    Socket s=new Socket("127.0.0.1",Server.SERVER_PORT);
                    OutputStream os;

                    InputStream is;
                    os=s.getOutputStream();
                    os.write(name.getBytes());
                    os.write(Server.ACCOUNT_SPLIT_TAG);
                    os.write(password.getBytes());
                    os.flush();
                    Thread.sleep(1000);
                    is=s.getInputStream();
                    int len=is.available();
                    System.out.println("length: "+len);
                    byte[] bytes=new byte[len];
                    is.read(bytes);
                    String result=new String(bytes);
                    System.out.println("result: "+result);
                    if(result.equals(Server.ACK))
                    {
                        hint.setText("验证成功，欢迎光临");
                        fileWriteThread.addContent(1);
                        cli.dispose();//跳到聊天页面，马奥宇注意，这里应该把我登录时保持的连接（就是一些变量）作为参数传给下一个类，应该是这样吧。
                        new SendMsg();
                    }else{
                        passtext.setText(null);
                        hint.setText("用户名或密码有误，请重新输入");
                        fileWriteThread.addContent(0);
                        new SendMsg();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    //send message to server
    public static  void sendMessage(String message){
        new Thread(new Runnable(){

            public void run() {
                try {
                    Socket s=new Socket("127.0.0.1",Server.SERVER_PORT);

                    OutputStream os;
                    os=s.getOutputStream();
                    os.write(message.getBytes());
                    os.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
