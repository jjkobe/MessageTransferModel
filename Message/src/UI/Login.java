package UI; /**
 * Created by Pisces on 16/3/22.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Login extends JFrame implements ActionListener{
    private JTextField jtf1;
    private JPasswordField jpf;
    private JTextField jtf2;

    public Login(){
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

        jtf1 = new JTextField();

        jtf1.setBounds(150,80,150,20);

        jtf1.setFont(font);

        conn.add(jtf1);

        JLabel jl3 = new JLabel("Password:");

        jl3.setBounds(40,120,90,20);

        jl3.setForeground(Color.red);//字体颜色

        conn.add(jl3);

        jpf = new JPasswordField();

        jpf.setBounds(150,120,150,20);

        jpf.setFont(font);

        conn.add(jpf);

        JLabel jls = new JLabel("Server Address:");

        jls.setBounds(40,160,100,20);

        conn.add(jls);

        //账号的文本框

        jtf2 = new JTextField();

        jtf2.setBounds(150,160,150,20);

        jtf2.setFont(font);

        conn.add(jtf2);
        //状态






        JButton jb1 = new JButton("Login");

        jb1.setBounds(140,220,80,20);


        conn.add(jb1);

        jb1.addActionListener(this);

        JButton jb2 = new JButton("Exit");

        jb2.setBounds(230,220,80,20);

        conn.add(jb2);

        jb2.addActionListener(this);


        this.setTitle("Login");

        this.setResizable(false);//设置尺寸不可改变

        this.setLocationRelativeTo(null);//设置居中

        this.setDefaultCloseOperation(3);//当点击关闭时进行关闭

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String getCommand = e.getActionCommand();

        if("Login".equals(getCommand)){

            String getName = this.jtf1.getText();

            String getPwd = this.jpf.getText();

            String getAdd = this.jtf2.getText();


            JOptionPane.showMessageDialog(null,"Login Success!");



        }
        else if("Exit".equals(getCommand)){

            this.dispose();

        }
    }
    public static void main(String[] args) {

        new Login();
   }

}

