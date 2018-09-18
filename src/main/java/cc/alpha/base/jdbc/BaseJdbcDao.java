package cc.alpha.base.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.velcro.base.security.model.Permissiondetail;
import com.velcro.base.security.model.Permissionrule;
import com.velcro.workflow.workflow.model.Workflowoperators;


public class BaseJdbcDao extends JdbcDaoSupport {

	public List executeSqlForList(String sql){
		return this.getJdbcTemplate().queryForList(sql);
	}
	
	public Map executeForMap(String sql) {
		return getJdbcTemplate().queryForMap(sql);
	}
	
	public Page pagedQuery(String sql, int pageNo, int pageSize) {
        if (sql == null)
            throw new IllegalArgumentException("NULL is not a valid string");

        return JdbcPage.getJdbcPageInstance(getJdbcTemplate(),sql,pageNo,pageSize);
    }
	
	public Page pagedQuery(String sql,int totalCount, int pageNo, int pageSize) {
        if (sql == null)
            throw new IllegalArgumentException("NULL is not a valid string");

        return JdbcPage.getJdbcPageInstance(getJdbcTemplate(),sql,totalCount,pageNo,pageSize);
    }
	
	public Page pagedQuery(SQLObject sqlobj) {
        return JdbcPage.getJdbcPageInstance(getJdbcTemplate(),sqlobj);
    }
	
	public int update(String sql){
		return getJdbcTemplate().update(sql);
	}
	
	public int update(String sql,Object...objects){
		return getJdbcTemplate().update(sql,objects);
	}
	
	public int[] updateBatch(String[] sql){
		return getJdbcTemplate().batchUpdate(sql);
	}
	
	public boolean updateBatchWfopt(List<Workflowoperators> objs) throws SQLException{
		if(objs == null || objs.size() == 0){
			return true;
		}
		StringBuffer wfoperatorsql = new StringBuffer("insert into workflowoperators (id,workflowid,ruleid,userid,istodo,nodeid,operated,createtime,operatelevel,operatetype) values (?,?,?,?,?,?,?,?,?,?)");
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = this.getConnection();
			statement = conn.prepareStatement(wfoperatorsql.toString());
			
			for(Workflowoperators wfopt:objs){
				int currentIndex = 0;
				statement.setString(++currentIndex, IDGernerator.getUnquieID());
				statement.setString(++currentIndex, wfopt.getWorkflowid());
				statement.setString(++currentIndex, wfopt.getRuleid());
				statement.setString(++currentIndex, wfopt.getUserid());
				statement.setInt(++currentIndex, wfopt.getIstodo());
				statement.setString(++currentIndex, wfopt.getNodeid());
				statement.setInt(++currentIndex, wfopt.getOperated());
				statement.setString(++currentIndex, wfopt.getCreatetime());
				statement.setInt(++currentIndex, wfopt.getOperatelevel());
				statement.setInt(++currentIndex, wfopt.getOperatetype());
				
				statement.addBatch();
			}
			
			statement.executeBatch();
			return true;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
//				if (conn != null) {
//					conn.close();
//				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException se) {
				System.out.println("SQL Exception while closing " + "Statement : \n" + se);
			}
		}
	}
	
	public boolean updateBatchPermitRule(String objtable ,List<Permissionrule> objs) throws SQLException{
		String permitrulesql = "insert into permissionrule"+ objtable +" (id,objid,istype,objtable,sharetype,userobjtype,userids,opttype) values(?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = this.getConnection();
			statement = conn.prepareStatement(permitrulesql);
			
			for(Permissionrule prule:objs){
				int currentIndex = 0;
				statement.setString(++currentIndex, prule.getId());
				statement.setString(++currentIndex, prule.getObjid());
				statement.setInt(++currentIndex, 0);
				statement.setString(++currentIndex, objtable);
				statement.setString(++currentIndex, prule.getSharetype());
				statement.setString(++currentIndex, prule.getUserobjtype());
				statement.setString(++currentIndex, prule.getUserids());
				statement.setInt(++currentIndex, prule.getOpttype());
				statement.addBatch();
			}
			
			statement.executeBatch();
			return true;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
//				if (conn != null) {
//					conn.close();
//				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException se) {
				System.out.println("SQL Exception while closing " + "Statement : \n" + se);
			}
		}
	}
	
	public boolean updateBatchPermitDetail(String objtable ,List<Permissiondetail> objs) throws SQLException{
		String permitdetailsql = "insert into permissiondetail"+ objtable +" (id,ruleid,objid,objtable,userid,opttype,minseclevel,maxseclevel) values(?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = this.getConnection();
			statement = conn.prepareStatement(permitdetailsql);
			
			for(Permissiondetail pdetail:objs){
				int currentIndex = 0;
				statement.setString(++currentIndex, pdetail.getId());
				statement.setString(++currentIndex, pdetail.getRuleid());
				statement.setString(++currentIndex, pdetail.getObjid());
				statement.setString(++currentIndex, objtable);
				statement.setString(++currentIndex, pdetail.getUserid());
				statement.setInt(++currentIndex, pdetail.getOpttype());
				statement.setInt(++currentIndex, pdetail.getMinseclevel());
				statement.setInt(++currentIndex, pdetail.getMaxseclevel());
				statement.addBatch();
			}
			
			statement.executeBatch();
			return true;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
//				if (conn != null) {
//					conn.close();
//				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException se) {
				System.out.println("SQL Exception while closing " + "Statement : \n" + se);
			}
		}
	}
}
