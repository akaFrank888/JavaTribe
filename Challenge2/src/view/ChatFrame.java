//这部分是最难的

package view;

import cs.Client.domain.Message_Client;
import cs.Client.domain.Message_Record;
import cs.Client.service.ChatService;
import util.DataBaseCon.DataBaseConRecord;
import util.others.AllKeyListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

@SuppressWarnings("all")

public class ChatFrame extends BaseFrame {

    // 添加属性 存储发送者和接收者
    private static String sender;
    private static String reciever;


    public ChatFrame() {
        this.init();
    }
    public ChatFrame(String title) {
        super(title);
        sender = title.split("正在与")[0];
        // 从字符串中截取receiver
        int index1 = title.indexOf("正在与");
        int index2 = title.indexOf("好友聊天");
        reciever = title.substring(index1 + 3, index2);
        this.init();
    }

    // 创建一个主面板，三个副面板
    private JPanel mainPanel = new JPanel();
    private JPanel firstPanel = new JPanel();
    private JPanel secondPanel = new JPanel();
    private JPanel thirdPanel = new JPanel();
    // 创建一个聊天记录窗口（文本框+滚动条）
    private static JTextPane chatArea = new JTextPane();// 聊天文本域
    private JScrollPane scrollPane = new JScrollPane(chatArea);// 滚动条
    // 创建功能栏（有表情，文件，消息记录的按钮）
    private JButton imgButton = new JButton("图片");
    private JButton fileButton = new JButton("文件");
    private JButton recordButton = new JButton("记录");
    // 创建一个发送消息区和发送按钮
    private static JTextPane sendArea = new JTextPane();//发送消息文本域
    private JButton sendButton = new JButton("发送");

    @Override
    protected void setFontAndSoOn() {
        // 设置Panel布局管理--自定义
        mainPanel.setLayout(null);
        firstPanel.setLayout(null);
        secondPanel.setLayout(null);
        thirdPanel.setLayout(null);
        // 设置Panel背景颜色
        firstPanel.setBackground(Color.WHITE);
        secondPanel.setBackground(Color.gray);
        thirdPanel.setBackground(Color.WHITE);
        // 设置Panel位置
        mainPanel.setBounds(0, 0, 500, 600);
        firstPanel.setBounds(0, 0, 500, 400);
        secondPanel.setBounds(0, 400, 500, 100);
        thirdPanel.setBounds(0, 500, 500, 100);
        // 聊天信息的位置 字体
        scrollPane.setBounds(0, 0, 500, 400);
        chatArea.setFont(new Font("黑体", Font.BOLD, 30));
        chatArea.setText("");
        chatArea.setEnabled(false);// 文本域中的文字不能编辑
        // 设置功能按钮的位置 大小
        imgButton.setBounds(10, 10, 80, 80);
        imgButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fileButton.setBounds(100, 10, 80, 80);
        fileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        recordButton.setBounds(190, 10, 80, 80);
        recordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // 设置按钮上的字的字体
        imgButton.setFont(new Font("宋体", Font.BOLD, 16));
        fileButton.setFont(new Font("宋体", Font.BOLD, 16));
        recordButton.setFont(new Font("宋体", Font.BOLD, 16));
        sendButton.setFont(new Font("宋体", Font.BOLD, 16));
        // 设置发送消息区和发送按钮的位置
        sendArea.setBounds(0, 0, 400, 100);
        sendArea.setFont(new Font("黑体", Font.BOLD, 20));
        sendButton.setBounds(400, 0, 100, 100);
        recordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void addElement() {
        firstPanel.add(scrollPane);

        secondPanel.add(imgButton);
        secondPanel.add(fileButton);
        secondPanel.add(recordButton);

        thirdPanel.add(sendArea);
        thirdPanel.add(sendButton);

        mainPanel.add(firstPanel);
        mainPanel.add(secondPanel);
        mainPanel.add(thirdPanel);

        this.add(mainPanel);
    }

    // 绑定事件监听器
    @Override
    protected void addListener() {

        // （1）图片按钮的监听器
        ActionListener imgListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1.弹出目录路径对话框并获取图片（文件）的路径，实现发送（包括存入txt）
                ChatService service = new ChatService(ChatFrame.this);
                Message_Client message_client = service.sendImgMessage();

                // 2.先发给服务器（因为要判断对方是否在线）
                ChatService.sendMessageToServer(message_client);

                // 3.再存入数据库(此步骤在SerConClientThread线程中)
            }
        };
        imgButton.addActionListener(imgListener);//观察者模式

        // （2）文件按钮的监听器
        ActionListener fileListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1.弹出目录路径对话框并获取文件的路径，实现发送（包括存入txt）
                ChatService service = new ChatService(ChatFrame.this);
                Message_Client message_client = service.sendFileMessage();

                // 2.先发给服务器（因为要判断对方是否在线）
                boolean b = ChatService.sendMessageToServer(message_client);

                // 3.再存入数据库(此步骤在SerConClientThread线程中)
            }
        };
        fileButton.addActionListener(fileListener);//观察者模式

        // （3）消息记录按钮的监听器
        ActionListener recordListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出新的消息记录界面
                new RecordFrame("消息记录");
                // 因为每次查看消息记录都是重新从数据库里读，所以点击之后先清空jtp
                RecordFrame.getRecordArea().setText("");
                String senderTableName = "chat_"+sender;
                // 下面先后用到了RecordConDataBase里四合一的大方法和用于显示的小方法
                ArrayList<Message_Record> messageBox = DataBaseConRecord.obtainMessageBox(senderTableName);
                DataBaseConRecord.showRecord(sender,messageBox);

            }
        };
        recordButton.addActionListener(recordListener);//观察者模式

        //  （4）发送按钮的监听器
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1.本页面显示
                //  从发送消息区获取文字
                String message = sendArea.getText();

                ChatService service = new ChatService(ChatFrame.this);
                // 不管对方是否在线，先在上方显示发送的消息（此处逻辑不通）
                // （应该先发给服务器，判断玩之后再发回来显示。不然如果服务器没有实现转发而你这边先显示了，纯属自嗨）
                Message_Client message_client = service.sendMessage(message);

                // 2.先发给服务器线程（因为要判断对方是否在线）
                ChatService.sendMessageToServer(message_client);
                // 3.再存入数据库(此步骤在SerConClientThread线程中)
            }
        };
        sendButton.addActionListener(listener);// 观察者模式

        // (5)enter快捷键发送消息  键盘事件keyListener

        AllKeyListener keyListener = new AllKeyListener(sendButton);
        sendArea.addKeyListener(keyListener);
    }

    @Override
    protected void setFrameSelf() {
        // 设置窗体起始位置和大小
        this.setBounds(600, 300, 500, 600);
/*        // 设置点击关闭退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
        // 设置窗体大小不可拖拽
        this.setResizable(false);
        // 设置窗体显示状态
        this.setVisible(true);
    }
    // 设计一个方法  获取用户名和好友
    public static String getReciever() {
        return reciever;
    }
    public static String getSender() {
        return sender;
    }
    // 设计一个方法  获取chatArea  因为要在ClientConServerThread里用chatArea
    public static JTextPane getChatArea() {
        return chatArea;
    }
    // 设计一个方法   获取sendArea
    public static JTextPane getSendArea() {
        return sendArea;
    }
}