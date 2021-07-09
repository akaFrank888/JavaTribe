package dao.impl;

import dao.UserInfoDao;
import domain.UserInfo;
import util.JDBCUtils;
import java.sql.*;

public class UserInfoDaoImpl implements UserInfoDao {

    // 设计一个方法   用来实现如果用户填过资料卡，则自动显示

    @Override
    public UserInfo findByAccount(String account) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserInfo userInfo = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "select * from userInfo where account=?";
            // 3. 获得preparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String stuID = resultSet.getString("stuID");
                String master = resultSet.getString("master");
                String mainBox = resultSet.getString("mailBox");
                Blob headImg = resultSet.getBlob("headImg");
                // 封装对象
                userInfo = new UserInfo(name, gender, age, stuID, master, mainBox, headImg,account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn, preparedStatement, resultSet);
        }
        return userInfo;
    }


    // 设计一个方法   填写好资料卡后保存

    @Override
    public void save(UserInfo userInfo) {

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 编写sql
            String sql = "insert into userInfo values(null,?,?,?,?,?,?,?,?)";
            // 3. 获得preparedStatement对象
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, userInfo.getName());
            preparedStatement.setString(2, userInfo.getGender());
            preparedStatement.setInt(3, userInfo.getAge());
            preparedStatement.setString(4,userInfo.getStuID());
            preparedStatement.setString(5,userInfo.getMaster());
            preparedStatement.setString(6,userInfo.getMailBox());
            preparedStatement.setBlob(7, userInfo.getHeadImg());
            preparedStatement.setString(8,userInfo.getAccount());
            // 5. 执行
            int num = preparedStatement.executeUpdate();
            // 6. 判断
            if (num > 0) {
                System.out.println("用户的资料卡插入成功："+userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn, preparedStatement);
        }
    }
}
