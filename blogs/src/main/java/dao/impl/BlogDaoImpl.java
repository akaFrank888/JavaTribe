package dao.impl;

import dao.BlogDao;
import domain.Blog;
import domain.UserInfo;
import util.JDBCUtils;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BlogDaoImpl implements BlogDao {



    // 设计一个方法    查询总博客数
    // 会有两种情况调用该方法：
    //            情况一、普通的展示博客内容
    //            情况二、通过搜索功能展示博客内容

    @Override
    public int findTotalCount(String content) {
        int count = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementBySearch = null;
        ResultSet resultSet = null;
        ResultSet resultSetBySearch = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "select count(*) from essay ";
            String sqlBySearch = "select count(*) from essay where content like ? ";
            if (content != null && content.length() > 0) {
                // 情况二：搜索来的
                // 3. 获得preparedStatement
                preparedStatementBySearch = conn.prepareStatement(sqlBySearch);
                // 4. 赋值
                preparedStatementBySearch.setString(1, "%" + content + "%");
                // 5. 执行
                resultSetBySearch = preparedStatementBySearch.executeQuery();
                // 将光标从当前位置向前移动一行
                resultSetBySearch.next();
                count = resultSetBySearch.getInt(1);
            } else {
                // 情况一：普通展示
                // 3. 获得preparedStatement
                preparedStatement = conn.prepareStatement(sql);
                // 4. 赋值
                // 5. 执行
                resultSet = preparedStatement.executeQuery();
                // 将光标从当前位置向前移动一行
                resultSet.next();
                count = resultSet.getInt(1);
            }
            // 输出总博客数目
            System.out.println("总博客数目为："+count+" 条");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (content != null && content.length() > 0) {
                JDBCUtils.close(conn,preparedStatementBySearch,resultSetBySearch);
            } else {
                JDBCUtils.close(conn,preparedStatement,resultSet);
            }
        }
        return count;
    }

    @Override
    public int findTotalCountOfMine(String account) {
        int count = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "select count(*) from essay where account=?";
            // 3. 获得preparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 将光标从当前位置向前移动一行
            resultSet.next();
            count = resultSet.getInt(1);
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,preparedStatement,resultSet);
        }
        return count;
    }

    // 设计一个方法    分页查询博客
    // 会有两种情况调用该方法：
    //            情况一、普通的展示博客内容
    //            情况二、通过搜索功能展示博客内容

    @Override
    public List<Blog> findByPage(int beginIndex, int pageSize,String content) {
        List<Blog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementBySearch = null;
        ResultSet resultSet = null;
        ResultSet resultSetBySearch = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            // 每五条数据一页，按时间降序排列
            String sql = "select * from essay order by date desc limit ?,?";
            String sqlBySearch = "select * from essay where content like ? order by date desc limit ?,?";

            if (content != null && content.length() > 0) {
                // 情况二：通过搜索博客，进行分页展示
                // 3. 获得preparedStatement
                preparedStatementBySearch = conn.prepareStatement(sqlBySearch);
                // 4. 赋值
                preparedStatementBySearch.setString(1, "%" + content + "%");
                preparedStatementBySearch.setInt(2, beginIndex);
                preparedStatementBySearch.setInt(3, pageSize);
                // 5. 执行
                resultSetBySearch = preparedStatementBySearch.executeQuery();
                // 6. 判断
                int i = 0;
                while (resultSetBySearch.next()) {
                    Blog blog = new Blog();
                    blog.setTitle(resultSetBySearch.getString("title"));
                    blog.setContent(resultSetBySearch.getString("content"));
                    // 将数据库中的Timestamp类型转换成Java中Blog对象中date属性的String类型
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultSetBySearch.getTimestamp("date"));
                    blog.setDate(date);
                    blog.setAccount(resultSetBySearch.getString("account"));
                    list.add(blog);
                    i++;
                }
                System.out.println("通过搜索的分页查询的while执行了多少次？"+i);
            } else {
                // 情况一：普通的分页展示
                // 3. 获得preparedStatement
                preparedStatement = conn.prepareStatement(sql);
                // 4. 赋值
                preparedStatement.setInt(1, beginIndex);
                preparedStatement.setInt(2, pageSize);
                // 5. 执行
                resultSet = preparedStatement.executeQuery();
                // 6. 判断
                int i = 0;
                while (resultSet.next()) {
                    Blog blog = new Blog();
                    blog.setTitle(resultSet.getString("title"));
                    blog.setContent(resultSet.getString("content"));
                    // 将数据库中的Timestamp类型转换成Java中Blog对象中date属性的String类型
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultSet.getTimestamp("date"));
                    blog.setDate(date);
                    blog.setAccount(resultSet.getString("account"));
                    list.add(blog);
                    i++;
                }
                System.out.println("普通展示的分页查询的while执行了多少次？"+i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (content != null && content.length() > 0) {
                JDBCUtils.close(conn, preparedStatementBySearch, resultSetBySearch);
            } else {
                JDBCUtils.close(conn, preparedStatement, resultSet);
            }
        }
        return list;
    }

    @Override
    public List<Blog> findByPageOfMine(int beginIndex, int pageSize,String account) {
        List<Blog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            // 每五条数据一页，按时间降序排列
            String sql = "select * from essay where account=? order by date desc limit ?,?";
            // 3. 获取prepareStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            preparedStatement.setInt(2, beginIndex);
            preparedStatement.setInt(3, pageSize);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            int i = 0;
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                // 将数据库中的Timestamp类型转换成Java中Blog对象中date属性的String类型
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultSet.getTimestamp("date"));
                blog.setDate(date);
                blog.setAccount(resultSet.getString("account"));
                list.add(blog);
                i++;
            }
            System.out.println("分页查询的while执行了多少次？"+i);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,preparedStatement,resultSet);
        }
        return list;
    }

    @Override
    public Blog showOneByBlog(String date) {
        Blog blog = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "select * from essay where date=?";
            // 3. 获得preparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, date);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String account = resultSet.getString("account");
                // 7.封装对象
                blog = new Blog(title, content, date, account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,preparedStatement,resultSet);
        }
        return blog;
    }

    @Override
    public UserInfo showOneByUser(String account) {
        UserInfo user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "select * from userinfo where account=?";
            // 3. 获得preparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, account);
            // 5. 执行
            resultSet = preparedStatement.executeQuery();
            // 6. 判断
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                String master = resultSet.getString("master");
                String mailbox = resultSet.getString("mailBox");
                // 7.封装对象
                user = new UserInfo();
                user.setAccount(account);
                user.setName(name);
                user.setGender(gender);
                user.setMailBox(mailbox);
                user.setMaster(master);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,preparedStatement,resultSet);
        }
        return user;
    }

    @Override
    public void save(Blog blog) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            // 1. 获得连接
            conn = JDBCUtils.getConnection();
            // 2. 定义sql
            String sql = "insert into essay values(null,?,?,?,?)";
            // 3. 获得preparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 4. 赋值
            preparedStatement.setString(1, blog.getTitle());
            preparedStatement.setString(2, blog.getContent());
            // String --->  Timestamp
            Timestamp time = Timestamp.valueOf(blog.getDate());
            preparedStatement.setTimestamp(3, time);
            preparedStatement.setString(4, blog.getAccount());
            // 5. 执行
            int i = preparedStatement.executeUpdate();
            // 6. 判断
            if (i > 0) {
                System.out.println("标题为"+blog.getTitle()+"的一条博客保存成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,preparedStatement);
        }
    }
}
