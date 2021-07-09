package dao.impl;

import dao.UserDao;
import domain.User;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {


    // 设计一个方法  根据account查询用户，并返回

    @Override
    public User findByAccount(String account) {
        User user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
           conn = JDBCUtils.getConnection();
            // 2. 定义sql语句
            String sql = "select * from user where account=?";
            // 3. 获得preparedStatement对象
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            if (resultSet.next()) {
                // 用户存在
                String password = resultSet.getString("password");
                String nickname = resultSet.getString("nickname");
                user = new User(account, password, nickname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn, preparedStatement, resultSet);
        }
        return user;
    }

    // 设计一个方法  保存一条用户信息

    @Override
    public void save(User user) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 编写sql
            String sql = "insert into user values(null,?,?,?)";
            // 3. 获得preparedStatement对象
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, user.getAccount());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getNickname());
            // 5. 执行
            int num = preparedStatement.executeUpdate();
            // 6. 判断
            if (num > 0) {
                System.out.println("账户为 "+user.getAccount()+" 的一条记录插入成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn, preparedStatement);
        }
    }

    // 设计一个方法   根据用户名和密码查查询用户，并返回

    @Override
    public User findByAccountAndPassword(String account, String password) {
        User user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql语句
            String sql = "select * from user where account=? and password=?";
            // 3. 获得preparedStatement对象
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, password);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            if (resultSet.next()) {
                // 用户存在
                String nickname = resultSet.getString("nickname");
                user = new User(account, password, nickname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn, preparedStatement, resultSet);
        }
        return user;
    }
}
