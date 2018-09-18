package cc.alpha.dbhelpers.jdbcpool;

/*
 我的数据库连接池

 ***********模块说明**************

 getInstance()返回POOL唯一实例,第一次调用时将执行构造函数
 构造函数Pool()调用驱动装载loadDrivers()函数;连接池创建createPool()函数
 loadDrivers()装载驱动
 createPool()建连接池
 getConnection()返回一个连接实例
 getConnection(long time)添加时间限制
 freeConnection(Connection con)将con连接实例返回到连接池
 getnum()返回空闲连接数
 getnumActive()返回当前使用的连接数

 */

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Pool {

	static private Pool instance = null; // 定义唯一实例

	private int maxConnect = 100;// 最大连接数
	private int normalConnect = 10;// 保持连接数
    String user = "sa";
    String password = "123456";
    String dbName = "velcro6";
    String driverName = "net.sourceforge.jtds.jdbc.Driver";
    String url = "jdbc:jtds:sqlserver://192.168.2.230:1433;DatabaseName="+dbName;
	Driver driver = null;// 驱动变量
	DBConnectionPool pool = null;// 连接池实例变量

	// 返回唯一实例
	static synchronized public Pool getInstance() {
		if (instance == null) {
			instance = new Pool();
		}
		return instance;
	}

	// 将构造函数私有,不允许外界访问
	private Pool() {
		Properties prop = new Properties();
		try {
			FileInputStream inStream = new FileInputStream(Pool.class.getResource("").getPath() + "db.properties");
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		String userDaoClass = prop.getProperty("");  
		maxConnect = Integer.parseInt(prop.getProperty("maxConnect"));
		normalConnect = Integer.parseInt(prop.getProperty("normalConnect"));
	    user = prop.getProperty("user");
	    password = prop.getProperty("password");
	    driverName = prop.getProperty("driverName");
	    url = prop.getProperty("url");
		loadDrivers(driverName);
		createPool();

	}

	// 创建一个新连接
	public Connection newConnection() {
		Properties prop = new Properties();
		try {
			FileInputStream inStream = new FileInputStream(Pool.class.getResource("").getPath() + "db.properties");
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		String userDaoClass = prop.getProperty("");  
		maxConnect = Integer.parseInt(prop.getProperty("maxConnect"));
		normalConnect = Integer.parseInt(prop.getProperty("normalConnect"));
	    user = prop.getProperty("user");
	    password = prop.getProperty("password");
	    driverName = prop.getProperty("driverName");
	    url = prop.getProperty("url");
	    loadDrivers(driverName);
		Connection con = null;
		try {
			if (user == null) { // 用户,密码都为空
				con = DriverManager.getConnection(url);
			} else {
				con = DriverManager.getConnection(url, user, password);
			}
			//System.out.println("连接池创建一个新的连接");
		} catch (SQLException e) {
			System.out.println("无法创建这个URL的连接" + url);
			return null;
		}
		return con;
	}
	// 装载和注册所有JDBC驱动程序
	private void loadDrivers(String dri) {

		String driverClassName = dri;
		try {
			driver = (Driver) Class.forName(driverClassName).newInstance();
			int tout = DriverManager.getLoginTimeout();
			DriverManager.setLoginTimeout(1800);
			DriverManager.registerDriver(driver);

			//System.out.println("成功注册JDBC驱动程序" + driverClassName);
		} catch (Exception e) {
			System.out.println("无法注册JDBC驱动程序:" + driverClassName + ",错误:" + e);
		}
	}

	// 创建连接池
	private void createPool() {
		pool = new DBConnectionPool(password, url, user, normalConnect,
				maxConnect);
		if (pool != null) {
			System.out.println("创建Mysql连接池成功");
		} else {
			System.out.println("创建连接池失败");
		}
	}

	// 获得一个可用的连接,如果没有则创建一个连接,且小于最大连接限制
	public Connection getConnection() {
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	// 获得一个连接,有时间限制
	public Connection getConnection(long time) {
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	// 将连接对象返回给连接池
	public void freeConnection(Connection con) {
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	// 返回当前空闲连接数
	public int getnum() {
		return pool.getnum();
	}

	// 返回当前连接数
	public int getnumActive() {
		return pool.getnumActive();
	}

	// 关闭所有连接,撤销驱动注册
	public synchronized void release() {

		// /关闭连接
		pool.release();

		// /撤销驱动
		try {
			DriverManager.deregisterDriver(driver);
			System.out.println("撤销JDBC驱动程序 " + driver.getClass().getName());
		} catch (SQLException e) {
			System.out.println("无法撤销JDBC驱动程序的注册:" + driver.getClass().getName());
		}

	}

	public static void main(String[] args){
		Pool dbpool = Pool.getInstance();
		for(int i=0; i<20 ; i++){
			Connection con = dbpool.getConnection();
			dbpool.freeConnection(con);
		}

		int num = dbpool.getnum();
		int anum = dbpool.getnumActive();
		System.out.println(num + "  "+ anum);
	}
}