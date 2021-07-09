package util.DataBaseCon;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 功能：创建和管理一个连接的缓冲池C3P0，这些连接准备好被任何需要他们的线程使用

public class JDBC_util {


    // 连接池只被初始化一次

    private static final ComboPooledDataSource DATA_SOURCE = new ComboPooledDataSource();

    // 用连接池，就不用注册驱动了

    // 一、连接  （是从连接池中获得，所以要在属性里定义连接池）

    public static Connection getConnection() throws Exception {
        // 从连接池连接
        Connection connection = DATA_SOURCE.getConnection();
        return connection;
    }

    // 二、释放资源（两种，重载）

    public static void release(Connection connection, Statement statement){

        release(connection,statement,null);
    }

    public static void release(Connection connection, Statement statement, ResultSet resultSet){
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
