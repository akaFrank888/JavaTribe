package cs.Client.thread;

import cs.Client.domain.MessageType;
import cs.Client.domain.Message_Client;
import cs.ManageThreadorFrame.ManageChatFrame;
import cs.ManageThreadorFrame.ManageUserFrame;
import cs.Client.service.ChatService;
import cs.Client.service.RecordService;
import util.others.AllMouseListener;
import view.ChatFrame;
import view.FileOpenOrNot;
import view.UserFrame;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static cs.Client.domain.MessageType.message_ret_addFriend;
import static cs.Client.domain.MessageType.message_ret_offLineFriend;


// 功能：一台电脑等多个不同账号的多线程类

public class ClientConServerThread extends Thread{


    // 属性+构造函数

    private Socket socket;
    private int x = 0;
    public Socket getSocket(){
        return this.socket;
    }

    public ClientConServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            // 一.不停地读取服务器发来的两种消息（普通消息和好友在线消息）
            try {
                if (socket != null) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Message_Client message_client =(Message_Client) ois.readObject();
                    if (message_client.getMesType().equals(MessageType.message_comm_mes)){
                        // 一.聊天消息
                        // 把读来的消息显示到receiver的界面上
                        // 第一个参数先是receiver再是sender（有点绕，没理解！）
                        ChatFrame chatFrame = ManageChatFrame.getQqChat(message_client.getReceiver() + " " + message_client.getSender());
                        // 文字图片文件消息三合一的方法：
                        new ChatService(chatFrame).showMessage(message_client);
                        // 如果是发送的是文件，就new一个弹窗，选择是否打开 该文件
                        if(message_client.getPath()!=null) {
                            // 两个if是将存入数据库的文件二进制的形式）读出来，下载到downLoadFile包里
                            if (message_client.getPath().endsWith(".txt")) {
                                // b.文件消息
                                // 1.从数据库中读出该文件并下载到设置好的路径(downLoadFile包中)，然后跳出弹框选择是否打开
                                String downloadPath = ChatService.getAndDownloadFile(message_client);

                                RecordService.writeDateAndDownloadPath(message_client.getDate(),downloadPath);

                                // 2.弹出提示框，判断是否打开该文件
                                if (message_client.getPath().endsWith(".txt")) {
                                    new FileOpenOrNot("提示框", downloadPath);
                                }
                            } else if (message_client.getIcon() != null) {
                                // c.图片消息
                                String downloadPath = RecordService.getAndDownloadImg(message_client);
                                RecordService.writeDateAndDownloadPath(message_client.getDate(),downloadPath);
                            }
                        }
                    } else if (message_client.getMesType().equals(MessageType.message_ret_onLineFriend)) {
                        // 在一个用户登录后，这一部分不可避免地会执行两次
                        // 一次为通知所有用户该用户上线（包括自己）  另一次为得到所有在线用户

                        // 二.好友在线的包的消息
                        String getter = message_client.getReceiver();
                        // 更新在线好友.
                        // 因为这里只能取到自己的userFrame，所以自己得到了自己登录时所有的在线的好友
                        UserFrame userFrame = ManageUserFrame.getQqFriendList(getter);
                        if (userFrame != null) {
                            userFrame.updateOnLineFriend(message_client);
                        }
                    } else if (message_client.getMesType().equals(message_ret_offLineFriend)){
                        // 返回的是message_ret_offLineFriend  下线好友
                        // 三.好友下线的包的消息
                        String getter = message_client.getReceiver();
                        // 修改相应的好友列表
                        // 不同用户的UserFrame不同，hashMap也不同。虽然是static但取也只能取到自己的
                        // 因为随着客户端线程的不同而hashMap开启了不同的内存

                        UserFrame userFrame = ManageUserFrame.getQqFriendList(getter);
                        if (userFrame != null) {
                            userFrame.updateOfflineFriend(message_client);
                        }
                    } else if (message_client.getMesType().equals(message_ret_addFriend)) {
                        // 四.返回的是 加好友 的消息
                        String sender = message_client.getSender();
                        UserFrame userFrame = ManageUserFrame.getQqFriendList(sender);
                        if (!message_client.getIfFriend()) {

                            String senderName = message_client.getSender();
                            String newFriendName = message_client.getMessage();
                            JLabel newLabel = userFrame.addFriend(newFriendName);
                            String receiverName = newLabel.getText();
                            // 并添加监听
                            AllMouseListener mouseListener = new AllMouseListener(senderName, receiverName, newLabel);
                            newLabel.addMouseListener(mouseListener);
                            // 如果对方刚好在线，就显示在线

                            if (message_client.getIfOnline()) {
                                userFrame.showOneOnline(newLabel);
                                userFrame.showWarning(userFrame, "添加成功");
                            }
                        } else {
                            userFrame.showWarning(userFrame, "已经为好友，无法重复添加");
                        }
                    } else {
                        // 五.返回的是 删好友 的消息
                        String sender = message_client.getSender();
                        UserFrame userFrame = ManageUserFrame.getQqFriendList(sender);
                        if (message_client.getIfFriend()) {

                            String oldFriendName = message_client.getMessage();
                            userFrame.deleteFriend(oldFriendName);
                        } else {
                            userFrame.showWarning(userFrame, "对方不是好友，无法删除");
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
