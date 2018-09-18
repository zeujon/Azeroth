package cc.alpha.dbhelpers.jdbcpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.velcro.base.DBSqlUtil;
import com.velcro.base.JdbcPage;
import com.velcro.base.Page;
import com.velcro.base.util.NumberHelper;

public class DBHelper {
	private Connection conn = null;
	public List executeSqlForList(String sql){
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Map dataMap = null;
		List list = new ArrayList();
		Pool dbpool = Pool.getInstance();
		conn = dbpool.getConnection();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			
			int cols = rsmd.getColumnCount();
			while (rs.next()){
				dataMap = new HashMap();
				for(int i=0;i<cols;i++){
					Object oTemp = rs.getObject(i+1);
					if(oTemp==null){
						dataMap.put(rsmd.getColumnName(i+1).toLowerCase(),"");
					}else{
						dataMap.put(rsmd.getColumnName(i+1).toLowerCase(),oTemp);
					}	//end if
				}	//end for
				list.add(dataMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
				if(rsmd != null){
					rsmd = null;
				}
				dbpool.freeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public Map executeSqlForMap(String sql){
		List list = this.executeSqlForList(sql);
		if(list.size() == 0){
			return new HashMap();
		}
		return (Map)list.get(0);
	}
	
	public Object getValue(String sql,String key){
		Map map = this.executeSqlForMap(sql);
		return map.get(key);
	}
	
    public int executeSql(String sql) {
		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		int returnValue = -1;
		Map dataMap = null;
        Pool dbpool = Pool.getInstance();
        conn = dbpool.getConnection();
        try {
            stmt = conn.createStatement();
            returnValue = stmt.executeUpdate(sql);
        }  catch(SQLException e)  {
            System.out.println(e.getMessage());
        } finally{
			try {
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
				if(rsmd != null){
					rsmd = null;
				}
				dbpool.freeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        return returnValue;
    }
    
    public String executeAdSql(String sql) {
		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		String returnValue = "";
		Map dataMap = null;
        Pool dbpool = Pool.getInstance();
        conn = dbpool.getConnection();
        try {
            stmt = conn.createStatement();
            returnValue = stmt.executeUpdate(sql) + "";
        }  catch(SQLException e)  {
            //System.out.println(e.getMessage());
            returnValue = e.getMessage();
        } finally{
			try {
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
				if(rsmd != null){
					rsmd = null;
				}
				dbpool.freeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        return returnValue;
    }
    
    public void executeSql(String sql,Object[] values) {
    	PreparedStatement stmt = null;
		ResultSetMetaData rsmd = null;
		int index = 0;
        Pool dbpool = Pool.getInstance();
        conn = dbpool.getConnection();
        try {
        	stmt = conn.prepareStatement(sql.toString());
            for(Object obj:values){
            	stmt.setObject(++index, obj);
            }
            stmt.executeUpdate();
        }  catch(SQLException e)  {
            e.printStackTrace();
        } finally{
			try {
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
				if(rsmd != null){
					rsmd = null;
				}
				dbpool.freeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public int[] executeBatchSql(List<String> sqllist) {
    	Statement stmt = null;
        Pool dbpool = Pool.getInstance();
        conn = dbpool.getConnection();
        try {
        	stmt = conn.createStatement();
        	for(String sql:sqllist){
        		stmt.addBatch(sql);
        	}
        	return stmt.executeBatch();
        }  catch(SQLException e)  {
            e.printStackTrace();
        } finally{
			try {
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
				dbpool.freeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
    
    public Page pagedQuery(String sql,int pageNo,int pageSize){
    	int totalCount = NumberHelper.string2Int(this.executeSqlForMap("SELECT COUNT(*)  mycount FROM (" + sql + ")").get("mycount"),0);
        if (totalCount < 1) {
            return JdbcPage.EMPTY_PAGE;
        }
        if (pageNo < 1) pageNo = 1;
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) startIndex = 1;
        String sqlstr = DBSqlUtil.getMysqlPagedSql(sql,pageSize*(pageNo-1),pageSize);
        //在些添加权限控件
    	List list = this.executeSqlForList(sqlstr);
    	int avaCount = list == null ? 0 : list.size();
        return new Page(startIndex, avaCount, totalCount, pageSize, list);
    }
    
    public boolean checkValidConnection() {
        Pool dbpool = Pool.getInstance();
        conn = dbpool.getConnection();
        if(conn == null){
        	return false;
        }else{
        	dbpool.freeConnection(conn);
        	return true;
        }
    }
    
	/**
	 * 从tomcat连接池获取数据库连接
	 * @return
	 */
	public Connection poolConnect() {
		String velcrods = "velcrods";
		Connection conn = null;
		try {
			Context env = new InitialContext();
			Context envctx = (Context) env.lookup("java:comp/env");
			DataSource pool = (DataSource) envctx.lookup("jdbc/" + velcrods);
			if (pool == null) {
				throw new Exception("jdbc/" + velcrods
						+ " is an unknown DataSource");
			} else {
				conn = pool.getConnection();
			}
		} catch (Exception e) {
			System.err.println("poolConnect:" + velcrods + ". "
					+ e.getMessage());
		}
		return conn;
	}
	
    public static void main(String args[])
    {
        DBHelper dbhelper = new DBHelper();
//        //数据插入示例
//        String sql = "insert into docbase (id,subject) values('402882f32601c0b3012601c1a881aaaa','aaaaaaa')  ";
//        dbhelper.executeSql(sql);
//        //数据查询示例
//        String sql2 = "select top 1 * from docbase";
//        List list = dbhelper.executeSqlForList(sql);
//        Map map = (Map)list.get(0);
//        System.out.println((String)map.get("subject"));

        dbhelper.checkValidConnection();
    }
}
