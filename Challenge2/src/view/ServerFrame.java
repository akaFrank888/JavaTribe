package view;

import cs.Server.thread.SerConClientThread;
import cs.Server.thread.StartServerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerFrame extends BaseFrame {

    public ServerFrame() {
        this.init();
    }
    public ServerFrame(String title) {
        // super要写在this上面
        super(title);
        this.init();
    }

    private JPanel mainPanel = new JPanel();

    // 先初始化10个label的位置

    public static final int INIT_SIZE = 10;
    private static JPanel midPanel = new JPanel(new GridLayout(INIT_SIZE,1,4,4));
    private JButton startButton = new JButton("启动服务器");
    private JButton closeButton = new JButton("关闭服务器");
    private JScrollPane scrollPane = new JScrollPane(midPanel) ;
    private JLabel[] labels;


    @Override
    protected void setFontAndSoOn() {

        mainPanel.setLayout(null);
        startButton.setBounds(70, 50, 100, 70);
        startButton.setFont(new Font("黑体", Font.BOLD, 12));
        closeButton.setBounds(230, 50, 100, 70);
        closeButton.setFont(new Font("黑体", Font.BOLD, 12));
        scrollPane.setBounds(105,120,200,380);

    }

    @Override
    protected void addElement() {
        mainPanel.add(startButton);
        mainPanel.add(closeButton);
        mainPanel.add(scrollPane);
        this.add(mainPanel);
    }

    @Override
    protected void addListener() {

        ActionListener startListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 开一个线程，让它去开启服务器  -----  避免了点完之后一直在等待连接而退出按钮失效
                // （先建立线程对象）

                StartServerThread sst = new StartServerThread();
                sst.start();

            }
        };
        startButton.addActionListener(startListener);

        ActionListener closeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        closeButton.addActionListener(closeListener);
    }

    @Override
    protected void setFrameSelf() {
        //设置窗体起始位置和大小
        this.setBounds(600, 280, 400, 600);
        //设置点击关闭退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体大小不可拖拽
        this.setResizable(false);
        //设置窗体显示状态
        this.setVisible(true);
    }

    public void showAndUpdateOnlineUser(String value) {

        // 标准流程：1.removeAll 清除所有组件 2.repaint 重绘面板 3.add  添加组件 4.revalidate 刷新

        midPanel.removeAll();
        midPanel.repaint();

        // 每次调用方法就清空JPanel重新建label（待改进）

        String[] users = value.split(" ");
        labels = new JLabel[users.length];

        for (int i = 0; i < labels.length; i++) {
            // 好友头像
            ImageIcon imageIcon = new ImageIcon("src//dbfile//user.jpg");
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            // JLabel里添加东西的方式是通过构造方法的方式
            labels[i] = new JLabel(users[i],imageIcon,JLabel.LEFT);
            // 不要忘了将每个label加到midPanel（下面这句）
            midPanel.add(labels[i]);
            midPanel.revalidate();
        }
    }

    // 设计一个方法  获得midPanel里的所有组件（所有在线好友label），并清除某个已经下线的好友

    public static void removeOffLineUser(String offLineUserName) {
        int count = midPanel.getComponentCount();
        for (int i = 0; i < count; i++) {
            JLabel label = (JLabel) midPanel.getComponent(i);
            if (label.getText().equals(offLineUserName)) {
                // 如果想要在已有的初始界面下进行改动的话
                // 需要如下的三步骤 ---  repaint重绘，remove/add改动，revalidate刷新
                midPanel.repaint();
                midPanel.remove(label);
                midPanel.revalidate();
            }
        }
    }

    public static void main(String[] args) {
        ServerFrame serverFrame = new ServerFrame();
        SerConClientThread.setServerFrame(serverFrame);
    }
}
