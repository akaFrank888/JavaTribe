package util.DataBaseCon;


import cs.Client.domain.Message_Record;
import cs.Client.service.RecordService;
import util.others.Message_RecordSorter;
import view.RecordFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import static cs.Client.service.ChatService.drawImage;

// 功能： 从数据库中拿出消息，实现单个用户发，收消息的历史记录回显

public class DataBaseConRecord {


    // 存储最终的关于该用户的消息message

    private static ArrayList<Message_Record> messageBox = new ArrayList<>();
    public static ArrayList<Message_Record> getMessageBox() {
        return messageBox;
    }

    // 用标识区分一个用户是发送者还是接收者

    public static final int AS_SENDER = 0;
    public static final int AS_RECEIVER = 1;


    // 设计一个 大方法：通过传senderTableName的参数 返回message的集合(按时间排好序的)

    public static ArrayList<Message_Record> obtainMessageBox(String senderTableName) {
        // 先清除messageBox存储的原来的记录
        messageBox.clear();
        // 作为发送者的消息
        DataBaseConRecord.getSenderRecord(senderTableName);
        // 获得其他可能会有该用户作为接收者的全部表
        ArrayList<String> tables = DataBaseConRecord.getTablesBox(senderTableName);
        // 找到作为接收者的消息然后存入message集合，在进行排序
        DataBaseConRecord.searchReceiver(tables, senderTableName);
        ArrayList<Message_Record> messageBox = DataBaseConRecord.sortMessageBox(DataBaseConRecord.getMessageBox());
        // 调用赋值downloadPath方法
        DataBaseConRecord.addDownloadPath(messageBox);
        return messageBox;
    }


    // 设计一个 大方法：通过if判断，显示到record界面

    public static void showRecord(String senderName, ArrayList<Message_Record> messageBox) {
        for (int i = 0; i < messageBox.size(); i++) {

            // 情况一：本人发的消息
            if (messageBox.get(i).getSender().equals(senderName)) {

                if (messageBox.get(i).getImgOrFile() == null) {
                    // a.普通消息
                    showNormalRecord(messageBox.get(i),AS_SENDER);
                } else if (messageBox.get(i).getMessage().equals("txt")) {
                    // b.文件消息
                    showFileRecord(messageBox.get(i),AS_SENDER);
                }else{
                    // c.图片消息
                    showImgRecord(messageBox.get(i),AS_SENDER);
                }
            } else if (messageBox.get(i).getReceiver().equals(senderName)) {
                if (messageBox.get(i).getImgOrFile() == null) {
                    // a.普通消息
                    showNormalRecord(messageBox.get(i),AS_RECEIVER);
                } else if (messageBox.get(i).getMessage().equals("txt")) {
                    // b.文件消息
                    showFileRecord(messageBox.get(i),AS_RECEIVER);
                }else{
                    // c.图片消息
                    showImgRecord(messageBox.get(i),AS_RECEIVER);
                }
            }
        }
    }

    //=====================================================================================

    // 第一个大方法用到的小方法们
    // 一.将该用户主动发出的消息先保存在message集合中

    public static void getSenderRecord(String senderTableName) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //从工具类（连接池） 获得连接
            connection = JDBC_util.getConnection();
            // 编写sql
            String sql = "select * from " + senderTableName;
            // 预编译sql
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            // 执行sql
            resultSet = preparedStatement.executeQuery();
            // 通过遍历，包装发送者和接收者的message，一并存入集合
            while (resultSet.next()) {
                // outer是该用户
                String outer = resultSet.getString("sender");
                String inner = resultSet.getString("receiver");
                String message = resultSet.getString("message");
                Blob imgOrFile = resultSet.getBlob("imgOrFile");
                String date = resultSet.getString("date");
                messageBox.add(new Message_Record(outer, inner, message, imgOrFile, date,null));
            }
            // 不需要排序
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC_util.release(connection, preparedStatement, resultSet);
        }
    }

    // 二.将该用户被动接受的消息继续存在集合中
    // 1.先设计一个方法---获取challenge2中所有其他的table（除了该用户的和登录用的）

    public static ArrayList<String> getTablesBox(String senderTableName) {

        // 放所有table的集合
        ArrayList<String> tablesBox = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //从工具类（连接池） 获得连接
            connection = JDBC_util.getConnection();
            // 编写sql
            String sql = "show tables from challenge2";
            // 预编译sql
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            // 执行sql
            resultSet = preparedStatement.executeQuery();
            // 判断
            while (resultSet.next()) {
                tablesBox.add(resultSet.getString(1));
            }

            // 从集合中删除不需要的元素（表）,只剩下可能被动接受消息的表
            tablesBox.remove("userinfo");
            tablesBox.remove(senderTableName);
            // 还要删除存好友列表的表
            // 此处特别注意：ArrayList集合循环remove的易错面试题   因为size随着remove是变的，而i一直在加
            // 先把原size赋给一个变量
            for (int i = 0; i < tablesBox.size(); i++) {
                if (tablesBox.get(i).startsWith("list_")) {
                    tablesBox.remove(i);
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC_util.release(connection, preparedStatement, resultSet);
        }
        return tablesBox;
    }


    // 2.通过遍历其他表，找到该用户被动接受的消息，将该消息对象添加进集合

    public static void searchReceiver(ArrayList<String> tablesBox, String senderTableName) {

        // 通过截取，获得senderName
        String senderName = senderTableName.split("_")[1];

        for (int i = 0; i < tablesBox.size(); i++) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                //从工具类（连接池） 获得连接
                connection = JDBC_util.getConnection();
                // 编写sql
                // 注意！！！table的名称不能用？代替
                String sql = "select * from " + tablesBox.get(i) + " where receiver=?";
                // 预编译sql
                preparedStatement = connection.prepareStatement(sql);
                // 设置参数
                preparedStatement.setString(1, senderName);
                // 执行sql
                resultSet = preparedStatement.executeQuery();
                // 判断
                while (resultSet.next()) {
                    String outer = resultSet.getString("sender");
                    // inner是该用户
                    String inner = resultSet.getString("receiver");
                    String message = resultSet.getString("message");
                    Blob imgOrFile = resultSet.getBlob("imgOrFile");
                    String date = resultSet.getString("date");
                    messageBox.add(new Message_Record(outer, inner, message, imgOrFile, date,null));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                JDBC_util.release(connection, preparedStatement, resultSet);
            }
        }
    }

    // 三.得到了根据时间顺序存储的消息

    public static ArrayList<Message_Record> sortMessageBox(ArrayList<Message_Record> messageBox) {
        Message_RecordSorter sorter = new Message_RecordSorter(messageBox);
        ArrayList<Message_Record> sortedMessageBox = sorter.arrayListSorter();
        return sortedMessageBox;
    }


    // 四.设计一个方法 给Message_Record里同一个date的文件和图片消息的downloadPath赋值( 通过遍历集合 )
    // 效果为 文件和图片消息都有downloadPath

    public static void addDownloadPath(ArrayList<Message_Record> messageBox) {

        // 先调用方法，把dateToDownload.txt读到缓存中
        HashMap<String,String> dateToPath_File = RecordService.readToMap();
        for (int i = 0; i < messageBox.size(); i++) {

            // 两个if一个样!!
            if (messageBox.get(i).getMessage().equals("img")) {
                // 挑选出img类型的记录，给它赋值downloadPath
                // 此方法就是根据date返回的
                String downloadPath = dateToPath_File.get(messageBox.get(i).getDate());
                messageBox.get(i).setDownloadPath(downloadPath);
            } else if (messageBox.get(i).getMessage().equals("txt")) {
                // 也赋值了txt的downloadPath
                String downloadPath = dateToPath_File.get(messageBox.get(i).getDate());
                messageBox.get(i).setDownloadPath(downloadPath);
            }
        }
    }

    // 第二个大方法用到的小方法们

    // 设计三个方法：分别用来显示普通消息、图片消息、文件消息
    // 1.普通消息

    public static void showNormalRecord(Message_Record message_record,int symbol) {
        String date = message_record.getDate();
        String sender = message_record.getSender();
        String receiver = message_record.getReceiver();
        String message = message_record.getMessage();

        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset, 15);
        StyleConstants.setForeground(attrset, Color.cyan);
        String string1 = date + '\n' + message + '\n';
        String string2 = date + "\n" + sender + "对" + receiver + "说:" + message + "\n";

        //接收窗口显示消息
        Document docs = RecordFrame.getRecordArea().getDocument();
        try {
            if (symbol == 0) {
                docs.insertString(docs.getLength(), string1, attrset);
            } else {
                docs.insertString(docs.getLength(), string2, attrset);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // 2.图片消息

    public static void showImgRecord(Message_Record message_record,int symbol) {

        String sender = message_record.getSender();
        String receiver = message_record.getReceiver();
        String date = message_record.getDate();
        String path = message_record.getDownloadPath();

        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrSet, 15);
        StyleConstants.setForeground(attrSet, Color.cyan);

        String string1 = date + '\n';
        String string2 = date + "\n" + sender + "对" + receiver + "说:";

        // 2.接收窗口显示消息
        Document docs = RecordFrame.getRecordArea().getDocument();
        try {
            if (symbol == 0) {
                docs.insertString(docs.getLength(), string1, attrSet);
            } else {
                docs.insertString(docs.getLength(), string2, attrSet);
            }
            RecordFrame.getRecordArea().setCaretPosition(docs.getLength());
            System.out.println("试试这里的"+path);
            RecordFrame.getRecordArea().insertIcon(drawImage(path));
            docs.insertString(docs.getLength(), "\n", attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // 3.文件消息

    public static void showFileRecord(Message_Record message_record,int symbol) {

        String sender = message_record.getSender();
        String receiver = message_record.getReceiver();
        String date = message_record.getDate();
        String path = message_record.getDownloadPath();
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrSet, 15);
        StyleConstants.setForeground(attrSet, Color.cyan);

        String string1 = date + "\n";
        String string2 = date + "\n" + sender + "对" + receiver + "说:";

        // 2.接收窗口显示消息
        Document docs = RecordFrame.getRecordArea().getDocument();
        try {
            if (symbol == 0) {
                docs.insertString(docs.getLength(), string1, attrSet);
            } else {
                docs.insertString(docs.getLength(), string2, attrSet);
            }
            RecordFrame.getRecordArea().setCaretPosition(docs.getLength());
            RecordFrame.getRecordArea().insertIcon(drawImage("src//dbfile//fileSymbol.jpg"));
            // 获取文件名称并显示于图标后面
            String[] value = path.split("\\\\");
            String fileName = value[value.length - 1];
            docs.insertString(docs.getLength(), "\n" + fileName + "\n", attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

