package util.DataBaseCon;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// 功能： 增删数据库中的list中的数据



public class DataBaseConList {

    // 设计一个方法 为一个用户添加一个好友     此处就不用判断好友是否存在了

    public static boolean addFriend(String userName, String newFriendName) {

        boolean b = false;
        Connection connection = null;
        PreparedStatement InsertPreparedStatement = null;
        try {
            // 1.获得连接
            connection = JDBC_util.getConnection();

            // 2.编写sql
            String listTableName = "list_"+userName;
            String sql = "insert into "+listTableName+" values(null,?)";
            // 3.预处理sql
            InsertPreparedStatement = connection.prepareStatement(sql);
            // 4.设置参数
            InsertPreparedStatement.setString(1,newFriendName);
            // 5.执行sql
            int num = InsertPreparedStatement.executeUpdate();
            // 6.判断
            if (num > 0) {
                System.out.println("好友" + newFriendName + "成功插入表" + listTableName);
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,InsertPreparedStatement);
        }
        return b;
    }

    // 设计一个方法 删除一个好友     此处就不用判断好友是否存在了

    public static boolean deleteFriend(String userName, String oldFriendName) {

        boolean b = false;
        Connection connection = null;
        PreparedStatement InsertPreparedStatement = null;
        try {
            // 1.获得连接
            connection = JDBC_util.getConnection();
            // 2.编写sql
            String listTableName = "list_"+userName;
            String sql = "delete from "+listTableName+" where friends=?";
            // 3.预处理sql
            InsertPreparedStatement = connection.prepareStatement(sql);
            // 4.设置参数
            InsertPreparedStatement.setString(1,oldFriendName);
            // 5.执行sql
            int num = InsertPreparedStatement.executeUpdate();
            // 6.判断
            if (num > 0) {
                System.out.println("好友" + oldFriendName + "成功从" + listTableName + "删除");
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,InsertPreparedStatement);
        }
        return b;
    }

    // 设计一个方法  从某个用户的list表中，读取所有好友 返回String用空格隔开

    public static String getFriends(String userName) {
        StringBuilder result = new StringBuilder();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1.获得连接
            connection = JDBC_util.getConnection();
            // 2.编写sql
            String tableName = "list_"+userName;
            String sql = "select * from "+tableName;
            // 3.预编译
            preparedStatement = connection.prepareStatement(sql);
            // 4.设置参数
            // 5.执行sql
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String friendName = resultSet.getString("friends");
                result.append(friendName);
                result.append(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBC_util.release(connection,preparedStatement,resultSet);
        }
        return result.toString();
    }
}
