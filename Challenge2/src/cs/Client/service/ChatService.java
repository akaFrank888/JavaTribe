package cs.Client.service;


import cs.Client.domain.MessageType;
import cs.Client.domain.Message_Client;
import cs.ManageThreadorFrame.ManageClientConServerThread;
import util.DataBaseCon.JDBC_util;
import view.ChatFrame;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;


// 功能：含有多种实现业务逻辑的方法---发送并接收各种类型的消息、获取时间、获取图片、获取路径。

public class ChatService {

    private ChatFrame chatFrame;

    public ChatService(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }


    // 设计三个方法  实现发送消息（普通消息，图片消息，文件消息）并保存消息
    // void改为返回message_Client对象，以便为了交互
    // 1.带参的文字消息

    public Message_Client sendMessage(String message) {
        // 获取用户名和日期
        String date = this.getDate();
        // 清空消息发送区
        ChatFrame.getSendArea().setText("");

        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset, 15);
        StyleConstants.setForeground(attrset, Color.cyan);
        String string = date + '\n' + message + '\n';

        //接收窗口显示消息
        Document docs = ChatFrame.getChatArea().getDocument();
        try {
            docs.insertString(docs.getLength(), string, attrset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

/*        // 靠右显示
        chatArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);*/


        // 3.创建一个用于交互的对象
        Message_Client message_client = new Message_Client();
        message_client.setMesType(MessageType.message_comm_mes);
        message_client.setSender(ChatFrame.getSender());
        message_client.setReceiver(ChatFrame.getReciever());
        message_client.setDate(date);
        message_client.setMessage(message);

        return message_client;
    }

    // 2.不带参的图片消息
    // void改为返回message对象，以便为了交互

    public Message_Client sendImgMessage() {
        // 改进：创建用户交互的对象
        Message_Client message_client = new Message_Client();
        // 1.获取用户名,日期,图片路径和图片
        String date = this.getDate();
        String Path = this.getPath();
        if (Path != null) {
            if (Path.endsWith(".jpg") || Path.endsWith(".gif") || Path.endsWith(".png")) {
                // 消息框中显示消息，控制台显示路径

                SimpleAttributeSet attrset = new SimpleAttributeSet();
                StyleConstants.setFontSize(attrset, 15);
                StyleConstants.setForeground(attrset, Color.cyan);

                String string = date + '\n';
                Icon icon = drawImage(Path);

                // 2.接收窗口显示消息
                Document docs = ChatFrame.getChatArea().getDocument();
                try {
                    docs.insertString(docs.getLength(), string, attrset);
                    ChatFrame.getChatArea().setCaretPosition(docs.getLength());
                    ChatFrame.getChatArea().insertIcon(icon);
                    docs.insertString(docs.getLength(), "\n", attrset);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }


                // 3.设置已经创建的用于交互的对象(对于图片消息的对象，message属性是路径)
                message_client.setMesType(MessageType.message_comm_mes);
                message_client.setSender(ChatFrame.getSender());
                message_client.setReceiver(ChatFrame.getReciever());
                message_client.setDate(date);
                message_client.setIcon(icon);
                message_client.setPath(Path);
            } else {
                // 返回的对象设置为null
                message_client = null;
                String warn = "发送的不是图片，发送失败";
                JOptionPane.showMessageDialog(null, warn);
            }
        }
        return message_client;
    }
    // 3.不带参的文件消息
    // void改为返回message对象，以便为了交互

    public Message_Client sendFileMessage() {
        // 改进：创建用户交互的对象
        Message_Client message_client = new Message_Client();
        // 1.获取用户名,日期,文件路径和文件
        String date = this.getDate();
        String path = this.getPath();
        if (path != null) {
            if (path.endsWith(".txt")) {
                // 消息框中显示消息，控制台显示路径

                SimpleAttributeSet attrset = new SimpleAttributeSet();
                StyleConstants.setFontSize(attrset, 15);
                StyleConstants.setForeground(attrset, Color.cyan);

                String string = date + '\n';

                // 2.接收窗口显示消息
                Document docs = ChatFrame.getChatArea().getDocument();
                try {
                    docs.insertString(docs.getLength(), string, attrset);
                    ChatFrame.getChatArea().setCaretPosition(docs.getLength());
                    ChatFrame.getChatArea().insertIcon(drawImage("src//dbfile//fileSymbol.jpg"));
                    // 获取文件名称并显示于图标后面
                    String[] value = path.split("\\\\");
                    String fileName = value[value.length - 1];
                    docs.insertString(docs.getLength(), "\n" + fileName + "\n", attrset);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                // 3.设置已经创建的用于交互的对象
                message_client.setMesType(MessageType.message_comm_mes);
                message_client.setSender(ChatFrame.getSender());
                message_client.setReceiver(ChatFrame.getReciever());
                message_client.setDate(date);
                message_client.setPath(path);
            } else {
                // 返回的对象设置为null
                message_client = null;
                String warn = "发送的不是txt，发送失败";
                JOptionPane.showMessageDialog(null, warn);
            }
        }
        return message_client;
    }

    // 设计三个方法， 实现接收消息（普通消息，图片消息，文件消息）

    public void showMessage(Message_Client message_client) {
        String date = message_client.getDate();
        String sender = message_client.getSender();
        String reciever = message_client.getReceiver();
        String message = message_client.getMessage();
        String path = message_client.getPath();
        Icon icon = message_client.getIcon();
        // message分为三种，需要通过message的String特点来判断
        if (icon!=null) {

            // 图片消息

            SimpleAttributeSet attrset = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset, Color.yellow);
            StyleConstants.setFontSize(attrset, 20);

            String string = date + "\n" + sender + "对" + reciever + "说:";

            // 接收窗口显示消息
            Document docs = ChatFrame.getChatArea().getDocument();
            try {
                docs.insertString(docs.getLength(), string, attrset);
                ChatFrame.getChatArea().setCaretPosition(docs.getLength());
                ChatFrame.getChatArea().insertIcon(icon);
                docs.insertString(docs.getLength(), "\n", attrset);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }

        } else if(path == null) {
            // 普通消息

            SimpleAttributeSet attrset = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset, Color.yellow);
            StyleConstants.setFontSize(attrset, 20);

            String string = date + "\n" + sender + "对" + reciever + "说:" + message + "\n";

            // 接收窗口显示消息
            Document docs = ChatFrame.getChatArea().getDocument();
            try {
                docs.insertString(docs.getLength(), string, attrset);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }else {
            // 文件消息

            String info = date + "\n" + sender + "对" + reciever + "说:";
            SimpleAttributeSet attrset = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset, Color.yellow);
            StyleConstants.setFontSize(attrset, 20);

            // 接收窗口显示消息
            Document docs = ChatFrame.getChatArea().getDocument();
            try {
                docs.insertString(docs.getLength(), info, attrset);
                ChatFrame.getChatArea().setCaretPosition(docs.getLength());
                ChatFrame.getChatArea().insertIcon(drawImage("src//dbfile//fileSymbol.jpg"));
                // 获取文件名称并显示于图标后面
                // message当作路径
                String[] value = path.split("\\\\");
                String fileName = value[value.length - 1];
                docs.insertString(docs.getLength(), fileName + "\n", attrset);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    // 设计一个方法 实现发送消息给服务器

    public static boolean sendMessageToServer(Message_Client message_client) {
        // 做个boolean标记  true是对方在线 false是对方不在线
        boolean b = false;
        // 还是用对象流,但因为这个类没有socket，所以要将socket静态化
        try {
            // 此处的socket是该用户的socket（想象一台电脑登着好几个qq客户端）
            // 先取得线程再取得socket
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConServerThread.getClientConServerThread(ChatFrame.getSender()).getSocket().getOutputStream());
            if (message_client != null) {
                // 防止返回的对象为null
                oos.writeObject(message_client);
                b = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    // 设计一个方法  通过时间获取数据库中二进制的 文件 ,并写到指定路径上 返回下载的路径

    public static String getAndDownloadFile(Message_Client message_client) {
        String filePath = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        try{

            String senderTableName = "chat_"+message_client.getSender();
            // 获得连接
            connection = JDBC_util.getConnection();
            // 写SQL语句
            String sql = "select * from "+senderTableName+" where date=?";
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 写参数
            preparedStatement.setString(1,message_client.getDate());
            // 执行
            resultset = preparedStatement.executeQuery();
            if (resultset.next()) {
                // 获得Blob对象
                Blob file = resultset.getBlob(5);
                // 每2048bytes地读
                byte[] bs = new byte[2048];
                InputStream in = file.getBinaryStream();
                int count = in.read(bs);
                // 接完之后，需要从内存（小推车bs）-->硬盘（文件） 而不需要网络传输，所以直接new字节型文件输出流；读到小推车，然后写出去
                // 获得文件名称
                String[] value = message_client.getPath().split("\\\\");
                String fileName = value[value.length - 1];
                // 先将文档下载到downLoadFile包中
                filePath = "src\\downLoadFile\\"+fileName;
                File newFile = new File(filePath);   //path写不进去txt
                FileOutputStream fileOut = new FileOutputStream(newFile);
                while (count != -1) {
                    fileOut.write(bs, 0, count);
                    fileOut.flush();
                    count = in.read(bs);
                }
                //关闭
                fileOut.close();   //注意管道流的close写法
                in.close();
                return filePath;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            JDBC_util.release(connection,preparedStatement,resultset);
        }
        return filePath;
    }


    // 设计一个方法  获取发送时间

    public String getDate() {
        // 获取并显示当前时间
        long time = System.currentTimeMillis();
        Date date1 = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 通过sdf对象将date1格式化成你描述的样子
        return sdf.format(date1);

    }


    // 设计一个方法  弹窗并获取图片（文件）路径

    private String getPath() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(chatFrame) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file.isFile()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    // 设计一个方法 用来处理图片展示

    public static ImageIcon drawImage(String path) {
        //通过给定的路径创建一个icon对象
        ImageIcon imageIcon = new ImageIcon(path);
        // 设置imageIcon对象内的image属性
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        // 将设置好的imageIcon对象返回
        return imageIcon;
    }
}
