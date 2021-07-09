package cs.Server.thread;
import cs.Client.domain.MessageType;
import cs.Client.domain.Message_Client;
import cs.ManageThreadorFrame.ManageSerConClientThread;
import util.exception.CancelSendException;
import util.exception.UserNotOnlineException;
import util.DataBaseCon.DataBaseConChat;
import util.DataBaseCon.DataBaseConList;
import util.exception.UserOffLineException;
import view.ServerFrame;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import static cs.Client.domain.MessageType.message_ret_addFriend;
import static cs.Client.domain.MessageType.message_ret_offLineFriend;


// 功能：实现服务器与某一客户端保持通信的线程（替代服务器）


public class SerConClientThread extends Thread {

    // 属性+构造方法--->为了获得服务器和客户端的socket

    private Socket socket;
    // serverFrame属性-----已赋值
    public static ServerFrame serverFrame;

    public Socket getSocket() {
        return this.socket;
    }

    public static void setServerFrame(ServerFrame serverFrame) {
        SerConClientThread.serverFrame = serverFrame;
    }

    public SerConClientThread(Socket socket) {
        this.socket = socket;
    }

    // 设计一个标记  当某个客户端退出后，服务器不会持续报错
    // true---在线  false---离线

    public boolean beOnline = false ;


    @Override
    public void run() {

        // 不断读取从客户端发来的消息（分两种），然后实现转发
        while (beOnline) {

            // 一.先接受
            try {
                if(socket!=null){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message_Client message_client = (Message_Client) ois.readObject();
                // 二.再转发
                    if (message_client.getMesType() != null) {

                        switch (message_client.getMesType()) {
                            case MessageType.message_comm_mes:

                                // 一.聊天消息
                                try {
                                    // 1.判断对方是否在线    取得接收人的通信线程，并写出去
                                    SerConClientThread sc = ManageSerConClientThread.getSerConClientThread(message_client.getReceiver());
                                    if (sc == null) {
                                        throw new UserNotOnlineException("因为对方不在线，所以没有连接服务器，以致于消息虽可显示，但无法接收与储存");
                                    }
                                    // 2.把message_client写给另一个客户端
                                    ObjectOutputStream oos = new ObjectOutputStream(sc.socket.getOutputStream());
                                    oos.writeObject(message_client);
                                    // 3.再存入数据库
                                    if (message_client.getPath() != null) {
                                        // 若是文件或图片
                                        if (message_client.getPath().endsWith(".txt") || message_client.getIcon() != null) {
                                            DataBaseConChat.saveImgOrFileMessage(message_client);
                                            System.out.println(message_client.getSender() + "给" + message_client.getReceiver() + "发的是文件或图片");
                                        }
                                    } else {
                                        // 若是文字消息
                                        DataBaseConChat.saveMessage(message_client);
                                        System.out.println(message_client.getSender() + "给" + message_client.getReceiver() + "说:" + message_client.getMessage());
                                    }
                                } catch (UserNotOnlineException e) {
                                    // 如果对方不在线就抛出自定义的异常
                                    e.printStackTrace();
                                }
                                break;
                            case MessageType.message_get_onLineFriend: {

                                // 二.显示好友在线的包的消息
                                System.out.println(message_client.getSender() + "要他的好友");
                                // value是所有在线好友的id，以空格间隔
                                String value = ManageSerConClientThread.getAllOnLineUserId();
                                // 服务器界面显示在线用户(在这里卡了整整一天，纪念一下啊啊啊啊啊啊啊啊啊啊)
                                serverFrame.showAndUpdateOnlineUser(value);

                                // 包装好一个message之后再写出去
                                Message_Client message = new Message_Client();

                                message.setMesType(MessageType.message_ret_onLineFriend);
                                message.setMessage(value);
                                message.setReceiver(message_client.getSender());

                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                oos.writeObject(message);
                                break;
                            }
                            case message_ret_offLineFriend:

                                // 三.下线好友的消息
                                ServerFrame.removeOffLineUser(message_client.getSender());
                                this.notifyOtherOff(message_client.getSender());
                                // 结束while循环，同时也避免了ServerFrame的Connection reset报错
                                this.beOnline = false;
                                throw new UserOffLineException("用户：" + message_client.getSender() + "已下线，其与服务器连接已断开");
                            case message_ret_addFriend: {

                                // 四.添加好友的消息

                                // 判断是否已经为好友，如果为好友判断对方刚好在线  并标记
                                if (!DataBaseConList.getFriends(message_client.getSender()).contains(message_client.getMessage())) {
                                    message_client.setIfFriend(false);
                                    // 把新好友写入数据库
                                    DataBaseConList.addFriend(message_client.getSender(), message_client.getMessage());
                                    if (ManageSerConClientThread.getSerConClientThread(message_client.getMessage()) != null) {
                                        message_client.setIfOnline(true);
                                    }
                                } else {
                                    message_client.setIfFriend(true);
                                }

                                // 直接返回给原客户端
                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                oos.writeObject(message_client);

                                break;
                            }
                            default: {

                                // 五.删除好友的信息

                                // 判断是否为好友
                                if (DataBaseConList.getFriends(message_client.getSender()).contains(message_client.getMessage())) {
                                    message_client.setIfFriend(true);
                                    // 把好友从数据库删除
                                    DataBaseConList.deleteFriend(message_client.getSender(), message_client.getMessage());
                                } else {
                                    message_client.setIfFriend(false);
                                }

                                // 直接返回给原客户端
                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                oos.writeObject(message_client);

                                break;
                            }
                        }
                    }
                } else {
                    throw new CancelSendException("您取消了图片/文件的发送");
                }
            } catch (IOException | ClassNotFoundException | CancelSendException | UserOffLineException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // 让该线程去通知其它用户 iam 上线了
    // 这个方法能取到所有在线的人的线程

    public void notifyOtherOn(String iam)
    {
        // 得到所有在线的人的线程
        ConcurrentHashMap hm= ManageSerConClientThread.hashMap;

        Iterator it=hm.keySet().iterator();

        while(it.hasNext())
        {
            Message_Client m=new Message_Client();
            m.setMessage(iam+" ");
            m.setMesType(MessageType.message_ret_onLineFriend);
            //取出在线人的 id
            String onLineUserId=it.next().toString();

            try {
                ObjectOutputStream  oos=new
                        ObjectOutputStream(ManageSerConClientThread.getSerConClientThread(onLineUserId).socket.getOutputStream());
                m.setReceiver(onLineUserId);
                oos.writeObject(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 让该线程去通知其它用户 iam 下线了


    public void notifyOtherOff(String iam)
    {
        //得到所有在线的人的线程
        ConcurrentHashMap hm= ManageSerConClientThread.hashMap;
        Iterator it=hm.keySet().iterator();

        while(it.hasNext())
        {
            Message_Client m=new Message_Client();
            m.setMessage(iam);
            m.setMesType(message_ret_offLineFriend);
            //取出在线人的 id
            String onLineUserId=it.next().toString();

            try {
                ObjectOutputStream  oos=new
                        ObjectOutputStream(ManageSerConClientThread.getSerConClientThread(onLineUserId).socket.getOutputStream());
                m.setReceiver(onLineUserId);
                oos.writeObject(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
