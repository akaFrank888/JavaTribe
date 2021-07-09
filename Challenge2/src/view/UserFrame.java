package view;

import cs.Client.domain.MessageType;
import cs.Client.domain.Message_Client;
import cs.Client.thread.ClientConServerThread;
import cs.ManageThreadorFrame.ManageClientConServerThread;
import util.DataBaseCon.DataBaseConList;
import util.others.AllMouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class UserFrame extends BaseFrame {

    public UserFrame() {
        this.init();
    }
    public UserFrame(String title) {
        // super要写在this上面
        super(title);
        this.init();
    }


    private String userName = this.getTitle().split("#")[0];

    // 三个JPanel  涉及到两种布局管理器

    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel midPanel = new JPanel(new GridLayout(10,1,4,4));
    private JPanel bottomPanel = new JPanel(new GridLayout(1,3));

    // 滚动轮（里面可以放JPanel）

    private JScrollPane scrollPane;

    // 按钮

    private JButton addFriendButton = new JButton("添加好友");
    private JButton deleteFriendButton = new JButton("删除好友");
    private JButton exitButton = new JButton("安全退出");


    // JLabel（我的和好友的）

    private JLabel meLabel;
    private JLabel[] labels;
    @Override
    protected void setFontAndSoOn() {

        // 1.设置我的JLabel
        ImageIcon meimageIcon = new ImageIcon("src//dbfile//user.jpg");
        meimageIcon.setImage(meimageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        meLabel = new JLabel(userName,meimageIcon,JLabel.LEFT);

        // 2.设置好友的JLabel
        String value = DataBaseConList.getFriends(userName);
        String[] friends = value.split(" ");
        labels = new JLabel[friends.length];
        for (int i = 0; i < labels.length; i++) {
            // 好友头像
            ImageIcon imageIcon = new ImageIcon("src//dbfile//user.jpg");
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

            // JLabel里添加东西的方式是通过构造方法的方式
            labels[i] = new JLabel(friends[i],imageIcon,JLabel.LEFT);
            // 设置除了本人，其他都不在线
            labels[i].setEnabled(false);
            if (labels[i].getText().equals(userName)) {
                labels[i].setEnabled(true);
            }
            // 给每一个好友JLabel设置一个监听，实现鼠标扫过时的颜色变化
            // 取得好友的名字 (getText()方法应该取不到imageIcon吧)
            String receiverName = labels[i].getText();
            int finalI = i;
            // 添加鼠标监听
            AllMouseListener mouseListener = new AllMouseListener(userName, receiverName, labels[finalI]);
            labels[i].addMouseListener(mouseListener);
            midPanel.add(labels[i]);
        }

        scrollPane = new JScrollPane(midPanel);

    }

    @Override
    protected void addListener() {

        // 1.监听添加好友按钮
        ActionListener addFriendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddFriendFrame(userName+"添加好友");
            }
        };
        addFriendButton.addActionListener(addFriendListener);


        // 2.监听删除好友按钮
        ActionListener deleteFriendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteFriendFrame(userName+"删除好友");
            }
        };
        deleteFriendButton.addActionListener(deleteFriendListener);


        // 3.监听退出按钮
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 取客户端的线程
                ClientConServerThread thread = ManageClientConServerThread.getClientConServerThread(userName);

                // 做一个 Message  指明这个qq号要下线了
                Message_Client m = new Message_Client();
                m.setMesType(MessageType.message_ret_offLineFriend);
                m.setSender(userName);

                // 因为线程已经打通所以可以通过那个管理线程的类来得到socket
                // 分解流程： 管理线程的类得到线程-->线程得到socket-->socket得到输出流
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(m);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // 关闭界面

                System.exit(0);

            }
        };
        exitButton.addActionListener(exitListener);

        // 4.监听窗体
        WindowListener closeWindowListener = new WindowListener() {
            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(UserFrame.this,"建议您下次点击安全退出按钮完成退出");
                exitButton.doClick();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        };
        this.addWindowListener(closeWindowListener);

    }

    @Override
    protected void addElement() {
        bottomPanel.add(addFriendButton);
        bottomPanel.add(deleteFriendButton);
        bottomPanel.add(exitButton);
        mainPanel.add(meLabel,"North");
        mainPanel.add(scrollPane,"Center");
        mainPanel.add(bottomPanel, "South");

        this.add(mainPanel);
    }

    @Override
    protected void setFrameSelf() {
        //设置窗体起始位置和大小
        this.setBounds(600, 280, 400, 600);
        //设置窗体显示状态
        this.setVisible(true);
    }

    // 设计一个方法  更新在线的好友情况

    public void updateOnLineFriend(Message_Client message_client) {

        String onLineFriend[] = message_client.getMessage().split(" ");

        // 增强for循环
        for (String s : onLineFriend) {
            for (JLabel label : labels) {
                if (label.getText().equals(s)) {
                    label.setEnabled(true);
                    break;
                }
            }
        }
    }

    // 设计一个方法  让离线的好友头像变黑

    public void updateOfflineFriend(Message_Client message_client) {
        String offLineFriend = message_client.getMessage();
        for (JLabel label : labels) {
            if (label.getText().equals(offLineFriend)) {
                label.setEnabled(false);
            }
        }
    }

    // 设计一个方法  添加好友

    public JLabel addFriend(String newFiendName) {
        midPanel.repaint();
        // 好友头像
        ImageIcon imageIcon = new ImageIcon("src//dbfile//user.jpg");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        // JLabel里添加东西的方式是通过构造方法的方式
        JLabel newLabel = new JLabel(newFiendName,imageIcon,JLabel.LEFT);
        newLabel.setEnabled(false);
        // 不要忘了将每个label加到midPanel（下面这句）
        midPanel.add(newLabel);
        midPanel.revalidate();
        return newLabel;
    }
    // 设计一个方法  删除好友

    public void deleteFriend(String oldFiendName) {

        // 找到目标JLabel
        for (int i = 0; i < labels.length; i++) {
            System.out.println(labels[i].getText());
            if (labels[i].getText().equals(oldFiendName)) {
                midPanel.repaint();
                midPanel.remove(labels[i]);
                midPanel.revalidate();
                break;
            }
        }
    }

    // 设计一个方法  让某个好友显示在线（用于添加好友时该好友刚好在线）

    public void showOneOnline(JLabel newLabel) {
        newLabel.setEnabled(true);
    }

    // 设计一个方法  弹出警示框

    public void showWarning(UserFrame userFrame,String words) {
        JOptionPane.showMessageDialog(userFrame,words);
    }

}
