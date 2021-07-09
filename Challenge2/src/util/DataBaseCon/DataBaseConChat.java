package util.DataBaseCon;


import cs.Client.domain.Message_Client;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("all")

// 功能：将聊天信息(普通消息和文件图片消息)存入数据库

public class DataBaseConChat {

    // 设计一个方法 实现存入数据库--普通消息
    public static void saveMessage(Message_Client client) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            // 1.获得连接
            connection = JDBC_util.getConnection();

                // 开启事务
            connection.setAutoCommit(false);

            // 2.写SQL   因为uid自动增加所以写null，其他的就要写?
            String tableName = "chat_"+client.getSender();
            String sql = "insert into "+tableName+" values(null,?,?,?,null,?)";
            // 3.预编译
            preparedStatement = connection.prepareStatement(sql);
            // 4.设置参数的值
            preparedStatement.setString(1,client.getSender());
            preparedStatement.setString(2,client.getReceiver());
            preparedStatement.setString(3,client.getMessage());
            preparedStatement.setString(4,client.getDate());
            // 5.执行SQL
            int num = preparedStatement.executeUpdate();
            // 6.判断
            if (num > 0) {
                System.out.println("普通消息"+client.getMessage()+"插入成功");
            } else {
                System.out.println("普通消息插入失败");
            }

                // 提交事务
            connection.commit();

        }catch(Exception e){
                // 如果出现异常，就回滚
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,preparedStatement);
        }
    }
    // 设计一个方法 实现存入数据库--图片/文件消息（用字节流）
    public static void saveImgOrFileMessage(Message_Client client) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{

            // 先通过path确定是图片还是文件，再获取字节流
            String path = client.getPath();
            String type;
            if (path.endsWith("txt")) {
                // txt
                String filePath = path;
                type = "txt";
            }else{
                // img
                String imgPath = path;
                type = "img";
            }
            // txt和img都做成流存入Blob
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            // 1.获得连接
            connection = JDBC_util.getConnection();
            // 2.写SQL
            String tableName = "chat_"+client.getSender();
            String sql = "insert into "+tableName+" values(null,?,?,?,?,?)";
            // 3.预编译
            preparedStatement = connection.prepareStatement(sql);
            // 4.设置参数的值
            preparedStatement.setString(1,client.getSender());
            preparedStatement.setString(2,client.getReceiver());
                // 把类型写进message字段里，为了以后读的时候好区分图片和文件
            preparedStatement.setString(3,type);
            preparedStatement.setBlob(4,fis);
            preparedStatement.setString(5,client.getDate());
            // 5.执行SQL
            int num = preparedStatement.executeUpdate();
            // 6.判断
            if (num > 0) {
                if ("txt".equals(type)) {
                    System.out.println("文件消息插入成功");
                }else {
                    System.out.println("图片消息插入成功");
                }
            }else{
                System.out.println("文件/图片消息插入失败");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,preparedStatement);
        }
    }
}