package UI; /**
 * Created by Pisces on 16/3/22.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ServerLogin extends JFrame implements ActionListener{
    private JTextField jtf1;
    private JPasswordField jpf;
    private JTextField jtf2;

    public ServerLogin(){
        Container conn = this.getContentPane();//加入一个容器

        conn.setLayout(null);//绝对布局

        Font font = new Font("宋体",Font.BOLD,15);//创建一个字体以便其它调用

        this.setSize(337,300);

        Image image = new ImageIcon("green.jpeg").getImage();//窗口图像

        this.setIconImage(image);

        //设置背景


        ImageIcon image1 = new ImageIcon("green.jpeg");
        JLabel jl1 = new JLabel(image1);//也相当于一个容器


        jl1.setBounds(0,0,337,50);



        JFrame frame=new JFrame();

        //获取窗口的第二层，将label放入
        frame.getLayeredPane().add(jl1,new Integer(Integer.MIN_VALUE));

        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)frame.getContentPane();
        j.setOpaque(false);
        JPanel panel=new JPanel();
        //必须设置为透明的。否则看不到图片
        panel.setOpaque(false);

        frame.add(panel);
        frame.setSize(image1.getIconWidth(), image1.getIconHeight());
        frame.setVisible(true);

        conn.add(jl1);

        //在容器里面设置背景
        //conn.setBackground(Color.getHSBColor(30, 20, 120));


        JLabel jl2 = new JLabel("Server Address:");

        jl2.setBounds(40,80,120,20);

        conn.add(jl2);

        //账号的文本框

        jtf1 = new JTextField();

        jtf1.setBounds(170,80,150,20);

        jtf1.setFont(font);

        conn.add(jtf1);




        JButton jb1 = new JButton("登录");

        jb1.setBounds(140,220,80,20);


        conn.add(jb1);

        jb1.addActionListener(this);

        JButton jb2 = new JButton("退出");

        jb2.setBounds(230,220,80,20);

        conn.add(jb2);

        jb2.addActionListener(this);


        this.setTitle("Server Login");

        this.setResizable(false);//设置尺寸不可改变

        this.setLocationRelativeTo(null);//设置居中

        this.setDefaultCloseOperation(3);//当点击关闭时进行关闭

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String getCommand = e.getActionCommand();

        if("登录".equals(getCommand)){

            String getName = this.jtf1.getText();




            JOptionPane.showMessageDialog(null,"Login Success!");

        }
        else if("退出".equals(getCommand)){

            this.dispose();

        }
    }
//   public static void main(String[] args) {
//
//        new ServerLogin();
//    }

}




