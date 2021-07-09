package util.DataBaseCon;

import cs.Client.domain.User_Client;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("all")

// 功能：管理用户信息，连接缓存和数据库
public class DataBaseConUser {

    // 创建一个集合来当缓存,static是为了保证缓存是唯一的（类似单例模式）
    // userBox 存着 数据库中的userInfo
    private static HashMap<String, User_Client> userBox = new HashMap<String, User_Client>();

    // 设计一个方法 给account就能得user，static是为了类名直接.调用就好
    public static User_Client getUser(String account) {
        return userBox.get(account);
    }

    // 设计一个方法 给account 和 user 就能再创建一个
    public static void addUser(String account, User_Client user) {
        userBox.put(account, user);
    }

    // 数据库-->缓存（查）
    static{
        // C3P0可以使用 属性文件 或者 xml文件
        // 获得连接
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //从工具类（连接池） 获得连接
            connection = JDBC_util.getConnection();
            // 编写sql
            String sql = "select * from userinfo";
            // 预编译sql
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            // 执行sql
            resultSet = preparedStatement.executeQuery();
            // 判断
            while(resultSet.next()){
                String id = resultSet.getString("id");
                String password = resultSet.getString("password");
                // 存入缓存集合
                userBox.put(id, new User_Client(id, password));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,preparedStatement,resultSet);
        }
    }

    //设计一个方法 将用户名和密码从缓存中存入数据库
    //账号密码-->数据库(增)
    public static void saveInfo(String account,String password){
        // C3P0可以使用 属性文件 或者 xml文件
        // 获得连接
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            //从工具类（连接池） 获得连接
            connection = JDBC_util.getConnection();
            // 编写sql (注意sql语句中有参数的写法)
            String sql = "insert into userinfo values(null,?,?)";
            // 预编译sql
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1,account);
            preparedStatement.setString(2,password);
            // 执行sql
            int num = preparedStatement.executeUpdate();
            // 判断
            if (num > 0) {
                System.out.println("账号密码分别为"+account+"和"+password+"的信息插入成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,preparedStatement);
        }
    }
}