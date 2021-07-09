package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 	  Druid连接池的工具类
 */

public class JDBCUtils {

	// 声明静态 数据源 成员变量

	private static DataSource ds;

	// 创建连接池对象
	static {
		// 加载配置文件中的数据   ---  先得到类加载器，再得到流
		InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
		// Properties类是用来操作properties文件的
		Properties props = new Properties();
		try {
			// 加载进来，一行一行的读取
			props.load(is);
			// 创建连接池，使用配置文件中的参数
			ds = DruidDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设计一个方法    得到数据源

	public static DataSource getDataSource() {
		return ds;
	}

	// 设计一个方法    获得连接

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	// 设计一个方法    关闭连接

	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 设计一个方法    重载关闭方法

	public static void close(Connection conn, Statement stmt) {
		close(conn, stmt, null);
	}
}
