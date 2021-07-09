package view;

import cs.Client.domain.Message_Client;
import cs.Client.thread.ClientConServerThread;
import cs.ManageThreadorFrame.ManageClientConServerThread;
import util.others.AllKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static cs.Client.domain.MessageType.message_ret_deleteFriend;

public class DeleteFriendFrame extends BaseFrame {

    // Frame统一的构造方法

    public DeleteFriendFrame(){
        this.init();
    }
    public DeleteFriendFrame(String title){
        // super要写在this上面
        super(title);
        this.init();
    }

    private JPanel mainPanel = new JPanel();
    private JLabel idLabel = new JLabel("ID:");
    private JTextField idField = new JTextField();
    private JButton deleteButton = new JButton("删除");
    private JButton exitButton = new JButton("退出");

    @Override
    protected void setFontAndSoOn() {
        // 设置panel的布局管理为自定义方式
        mainPanel.setLayout(null);
        // 设置idLabel
        idLabel.setBounds(100,100,100,40);
        idLabel.setFont(new Font("黑体",Font.BOLD,34));
        // 设置ID栏
        idField.setBounds(200,100,140,40);
        // 设置按钮
        deleteButton.setBounds(80,150,80,40);
        deleteButton.setFont(new Font("黑体",Font.BOLD,22));
        exitButton.setBounds(200,150,80,40);
        exitButton.setFont(new Font("黑体",Font.BOLD,22));
    }

    @Override
    protected void addElement() {
        mainPanel.add(idLabel);
        mainPanel.add(idField);
        mainPanel.add(deleteButton);
        mainPanel.add(exitButton);
        this.add(mainPanel);
    }

    @Override
    protected void addListener() {
        // 1.监听删除按钮
        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendName = idField.getText();

                // 获得ClientConSer流
                String sender = DeleteFriendFrame.this.getTitle().split("删")[0];
                ClientConServerThread thread = ManageClientConServerThread.getClientConServerThread(sender);
                // 再包装个message
                Message_Client message = new Message_Client();
                message.setSender(sender);
                message.setMessage(friendName);
                message.setMesType(message_ret_deleteFriend);
                // 获得socket发给它
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 自动关闭界面
                DeleteFriendFrame.this.setVisible(false);
            }
        };
        deleteButton.addActionListener(deleteListener);

        // 2.监听退出按钮
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteFriendFrame.this.dispose();
            }
        };
        exitButton.addActionListener(exitListener);

        // 3.监听回车快捷键
        AllKeyListener keyListener = new AllKeyListener(deleteButton);
        idField.addKeyListener(keyListener);
    }



    @Override
    protected void setFrameSelf() {
        // 设置窗体起始位置和大小
        this.setBounds(600,280,400,300);
        // 设置窗体大小可拖拽
        this.setResizable(false);
        // 设置窗体显示状态
        this.setVisible(true);
    }
}
