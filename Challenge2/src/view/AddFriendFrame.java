package view;

import cs.Client.domain.Message_Client;
import cs.Client.thread.ClientConServerThread;
import cs.ManageThreadorFrame.ManageClientConServerThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import static cs.Client.domain.MessageType.message_ret_addFriend;


public class AddFriendFrame extends BaseFrame {

    // Frame统一的构造方法

    public AddFriendFrame(){
        this.init();
    }
    public AddFriendFrame(String title){
        // super要写在this上面
        super(title);
        this.init();
    }

    private JPanel mainPanel = new JPanel();
    private JLabel idLabel = new JLabel("ID:");
    private JTextField idField = new JTextField();
    private JButton addButton = new JButton("添加");
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
        addButton.setBounds(80,150,80,40);
        addButton.setFont(new Font("黑体",Font.BOLD,22));
        exitButton.setBounds(200,150,80,40);
        exitButton.setFont(new Font("黑体",Font.BOLD,22));
    }

    @Override
    protected void addElement() {
        mainPanel.add(idLabel);
        mainPanel.add(idField);
        mainPanel.add(addButton);
        mainPanel.add(exitButton);
        this.add(mainPanel);
    }

    @Override
    protected void addListener() {
        // 1.监听添加按钮
        ActionListener addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendName = idField.getText();

                // 获得ClientConSer流
                String sender = AddFriendFrame.this.getTitle().split("添")[0];
                ClientConServerThread thread = ManageClientConServerThread.getClientConServerThread(sender);

                // 再包装个message
                Message_Client message = new Message_Client();
                message.setSender(sender);
                message.setMessage(friendName);
                message.setMesType(message_ret_addFriend);
                // 获得socket发给它
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 自动关闭界面
                AddFriendFrame.this.setVisible(false);
            }
        };
        addButton.addActionListener(addListener);

        // 2.监听退出按钮
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddFriendFrame.this.dispose();
            }
        };
        exitButton.addActionListener(exitListener);


        // 3.监听enter快捷键实现添加
        KeyListener addKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    addButton.doClick();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        idField.addKeyListener(addKeyListener);
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
