package util.DataBaseCon;

import java.sql.*;



// 功能： 创建challenge2库和userInfo表 ， 且每当新用户注册成功便建他自己的list_和chat_表

public class InitDataBase {

    // 设计一个方法  创建一个新库userInfo

    public static void creatDataBase() throws ClassNotFoundException, SQLException {

        // 1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获得connection连接，先写一个已经存在的数据库，来创建一个新的库
        String old_url = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false";
        Connection oldConnection = DriverManager.getConnection(old_url, "root", "mysql1234");

        // 3.执行SQL语句

        String checkDataBase = "show databases like \"challenge2\" ";
        String createDataBase = "create database challenge2";

        Statement oldStatement = oldConnection.createStatement();
        ResultSet resultSet = oldStatement.executeQuery(checkDataBase);

        if (resultSet.next()) {
            // 数据库存在
            System.out.println("challenge2 exists!");

        } else {
            // 创建challenge2库
            // 对于execute的增删改（返回boolean的）  成功时返回false！
            boolean result = oldStatement.execute(createDataBase);
            if (!result) {
                System.out.println("成功创建challenge2库");
            }
            // 4.释放资源
            oldConnection.close();
            oldStatement.close();
        }
    }

    // 设计一个方法 创建存储账号和密码的表单userInfo

    public static void createUserTable() throws ClassNotFoundException, SQLException {

        // 1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获得connection连接，打开新的库
        String url = "jdbc:mysql://localhost:3306/challenge2?useUnicode=true&serverTimezone=GMT&characterEncoding=utf-8&useSSL=false";
        Connection connection = DriverManager.getConnection(url, "root", "mysql1234");
        // 3.执行SQL语句
        Statement statement = connection.createStatement();
        // Windows下的表名全是小写
        String checkTable = "show tables like \"userinfo\"";
        String createTable = "create table userinfo(num int primary key auto_increment ,id varchar(20), password varchar(20))";
        ResultSet resultSet = statement.executeQuery(checkTable);

        if (resultSet.next()) {
            // 表存在
            System.out.println("userinfo exists!");

        } else {
            boolean result2 = statement.execute(createTable);
            if (!result2) {
                System.out.println("成功创建userinfo表");
            }
            // 4.释放资源
            statement.close();
            connection.close();
        }
    }

    // 设计一个方法 LogInFrame里一登录成功就创建属于每个用户的list_和chat_表格

    public static void createEveryUser_ListAndChatTable(String ListTableName, String chatTableName) throws ClassNotFoundException, SQLException {
        // 1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获得connection连接，打开新的库
        String url = "jdbc:mysql://localhost:3306/challenge2?useUnicode=true&serverTimezone=GMT&characterEncoding=utf-8&useSSL=false";
        Connection connection = DriverManager.getConnection(url, "root", "mysql1234");
        // 3.执行SQL语句
        // 两个sql语句必须用两个statement！！
        Statement statement1 = connection.createStatement();
        Statement statement2 = connection.createStatement();
        // 下面是sql中带被传的参数有两种写法
        String checkListTable = "show tables like '" + ListTableName + "'";
        String checkChatTable = "show tables like '" + chatTableName + "'";
        String createListTable = "create table " + ListTableName + "(num int primary key auto_increment ,friends varchar(20))";
        String createChatTable = "create table " + chatTableName + "(num int primary key auto_increment ,sender varchar(20), receiver varchar(20), message varchar(200), imgOrFile blob(1000000), date varchar(20))";

        ResultSet listResultSet = statement1.executeQuery(checkListTable);
        ResultSet chatResultSet = statement2.executeQuery(checkChatTable);


        if (listResultSet.next()) {
            // List表存在
            System.out.println(ListTableName + " exists!");

        } else {
            boolean result1 = statement1.execute(createListTable);
            if (!result1) {
                System.out.println("成功创建" + ListTableName + "表");
            }
        }

        if (chatResultSet.next()) {
            // chat表存在
            System.out.println(chatTableName + " exists!");

        } else {
            boolean result2 = statement2.execute(createChatTable);
            if (!result2) {
                System.out.println("成功创建" + chatTableName + "表");
            }
        }
        // 4.释放资源
        statement1.close();
        statement2.close();
        connection.close();
    }
}
