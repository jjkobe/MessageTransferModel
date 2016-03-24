package client;

import server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Client extends JFrame{
    private JLabel jLabel1;
    private JTextField usertext;
    private JLabel jLabel2;
    private JButton loginbutton;
    private JLabel hint;
    private JTextField passtext;
    private static FileWriteThread fileWriteThread=new FileWriteThread();
    String name;
    String password;
    public Client() {
        {
            getContentPane().setLayout(null);
        }
        {
            jLabel1 = new JLabel();
            getContentPane().add(jLabel1);
            jLabel1.setText("\u7528\u6237\u540d");
            jLabel1.setBounds(39, 39, 63, 18);
        }
        {
            usertext = new JTextField();
            getContentPane().add(usertext);
            usertext.setBounds(109, 37, 156, 22);
        }
        {
            jLabel2 = new JLabel();
            getContentPane().add(jLabel2);
            jLabel2.setText("\u5bc6\u7801");
            jLabel2.setBounds(39, 77, 38, 18);
        }
        {
            passtext = new JTextField();
            getContentPane().add(passtext);
            passtext.setBounds(109, 75, 156, 22);
        }
        {
            loginbutton = new JButton();
            getContentPane().add(loginbutton);
            loginbutton.setText("\u767b\u9646");
            loginbutton.setBounds(90, 113, 91, 28);
            loginbutton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    name=usertext.getText().trim();
                    password=passtext.getText().trim();
                    if(password.equals(""))
                    {
                        password="";
                    }
                    if(name!=null&&name.length()>0)
                    {
                        hint.setText("正在验证客户端，请稍后......");
                        start();
                    }
                }

                public  void start() {

                    new Thread(new Runnable(){

                        public void run() {
                            try {
                                Socket s=new Socket("127.0.0.1", Server.SERVER_PORT);
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
                                }else{
                                    passtext.setText(null);
                                    hint.setText("用户名或密码有误，请重新输入");
                                    fileWriteThread.addContent(0);
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
            });
        }
        {
            hint = new JLabel();
            getContentPane().add(hint);
            hint.setBounds(90, 8, 172, 23);
        }
        {
            this.setSize(318, 188);
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        fileWriteThread.start();
        new Client();
    }

}
