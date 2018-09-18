package com.velcro.base;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.velcro.base.util.StringHelper;

public class JdbcPage extends Page {
	
	public static final JdbcPage EMPTY_PAGE = new JdbcPage();

	protected final static Log logger = LogFactory.getLog(JdbcPage.class);
	
    private JdbcPage() {
        super();
    }

    public JdbcPage(int start, int avaCount, int totalSize, int pageSize, Object data) {
        super(start, avaCount, totalSize, pageSize, data);
    }

    public  static JdbcPage getJdbcPageInstance(JdbcTemplate jdbcTemplate,String sql, int pageNo, int pageSize) {
    	int totalCount = calculateTotalElementsByList(jdbcTemplate,sql);
        return determineElements(jdbcTemplate,sql, totalCount, pageNo, pageSize);
    }
    
    public  static JdbcPage getJdbcPageInstance(JdbcTemplate jdbcTemplate,String sql,int totalCount, int pageNo, int pageSize) {
        return determineElements(jdbcTemplate,sql, totalCount, pageNo, pageSize);
    }

    public  static JdbcPage getJdbcPageInstance(JdbcTemplate jdbcTemplate,SQLObject sqlobj) {
    	int totalCount = sqlobj.getTotalCount();
    	if(totalCount==0){
    		totalCount = calculateTotalElementsByList(jdbcTemplate,sqlobj);
    	}
        return determineElements(jdbcTemplate,totalCount,sqlobj);
    }
    
    private static JdbcPage determineElements(JdbcTemplate jdbcTemplate,String sql, int totalCount, int pageNo, int pageSize) {
        if (totalCount < 1) {
            return JdbcPage.EMPTY_PAGE;
        }
        if (pageNo < 1) pageNo = 1;
        int startIndex = JdbcPage.getStartOfAnyPage(pageNo, pageSize);
        String sqlstr = getQueryStr(sql,pageSize*(pageNo-1),pageSize);
        logger.info("JdbcPagegetQueryStr:" + sqlstr);
        //在些添加权限控件
    	List list = jdbcTemplate.queryForList(sqlstr.toString());
    	int avaCount = list == null ? 0 : list.size();
        return new JdbcPage(startIndex, avaCount, totalCount, pageSize, list);
    }
    

    private static JdbcPage determineElements(JdbcTemplate jdbcTemplate,int totalCount,SQLObject sqlobj) {
        if (totalCount < 1) {
            return JdbcPage.EMPTY_PAGE;
        }
        int pageNo = sqlobj.getPageNo();
        int pageSize = sqlobj.getPageSize();
        if (pageNo < 1) pageNo = 1;
        
        int startIndex = JdbcPage.getStartOfAnyPage(pageNo, pageSize);
        String sqlstr = DBSqlUtil.getSqlServerPageSql(sqlobj);
        //在些添加权限控件
    	List list = jdbcTemplate.queryForList(sqlstr.toString());
    	int avaCount = list == null ? 0 : list.size();
        return new JdbcPage(startIndex, avaCount, totalCount, pageSize, list);
    }
    
    /**
     * 返回总记录数(mjb)
     * @param jdbcTemplate
     * @param sql
     * @return
     */
    private static int calculateTotalElementsByList_bak(JdbcTemplate jdbcTemplate,String sql) {
    	String lower = sql.substring(sql.indexOf("from"));
    	//sql = StringUtils.replace(lower,StringUtils.substringBetween(lower,"select","from")," count(*) as totalnum "); 
    	String selected = StringUtils.substringBetween(sql,"select","from");
    	if(selected.contains(",")){
    		selected = selected.substring(0,selected.indexOf(","));
    	}
    	if(sql.indexOf(" group by ")!=-1){
    		
    		String groupby = "";
    		if(sql.indexOf(" order ") != -1){
    			groupby = sql.substring(sql.indexOf(" group by ")+10,sql.indexOf(" order "));
    		}else{
    			groupby = sql.substring(sql.indexOf(" group by ")+10);
    		}
    		if(groupby.contains(",")){
    			groupby = groupby.substring(0,groupby.indexOf(","));
    		}
    		
    		sql = "select count( distinct " + groupby + ") as totalnum " + lower;
    		sql = sql.substring(0,sql.indexOf(" group by "));
    	}else if(sql.indexOf(" order ") != -1){
    		sql = "select count("+ selected +") as totalnum " + lower;
    		sql = sql.substring(0,sql.indexOf(" order "));
    	}else{
    		sql = "select count("+ selected +") as totalnum " + lower;
    	}
    	logger.info("jdbcPage.sql:" +  sql);
    	int allcount = jdbcTemplate.queryForInt(sql); 
    	return allcount;
 
    }
    
    private static int calculateTotalElementsByList(JdbcTemplate jdbcTemplate,String sql) {
    	StringBuffer pagesql = new StringBuffer();
		pagesql.append("select count(*) from (").append(sql.replaceAll("\\sorder\\s.*", "")).append(") T ");
		int allcount = jdbcTemplate.queryForInt(pagesql.toString());
		return allcount;
    }

    /**
     * 返回总记录数(mjb)
     * @param jdbcTemplate
     * @param sql
     * @return
     */
    private static int calculateTotalElementsByList(JdbcTemplate jdbcTemplate,SQLObject sqlobj) {
		String pk = sqlobj.getPk();
		String distin = sqlobj.getDistinct();
		String tables = sqlobj.getTables();
		String showcolumns = sqlobj.getShowcolumns();
		String wherestr = sqlobj.getWherestr();
		String groupbystr = sqlobj.getGroupbystr();
		String orderbystr = sqlobj.getOrderbystr();
		int curpage = sqlobj.getCurpage();
		int spage = sqlobj.getStart();
		int pageSize = sqlobj.getPageSize();

		StringBuffer sql = new StringBuffer();
		sql.append("select count(").append(pk).append(") as totalnum from ").append(tables).append(" where ").append(wherestr);
    	int allcount = jdbcTemplate.queryForInt(sql.toString()); 
    	return allcount;
 
    }
    
    /**
     * 获得对应数据库的分页查询语句（mjb）
     * @param sql
     * @return
     */
    private static String getQueryStr(String sql,int start, int end){
		String dialectname = StringHelper.null2String(BaseContext.getDialectname());
        String sqlstr = "";
        if(dialectname.contains("SQLServer")){
        	sqlstr = DBSqlUtil.getSqlServer2005PageSql(sql,start,end);
//        }else if(dialectname.contains("SQLServer")){
//        	sqlstr = DBSqlUtil.getSqlServerPageSql(sql,start,end);
        }else if(dialectname.contains("Oracle")){
        	sqlstr = DBSqlUtil.getOraclePagedSql(sql,start,end);
        }else if(dialectname.contains("MySQL")){
        	sqlstr = DBSqlUtil.getMysqlPagedSql(sql,start,end);
        }else if(dialectname.contains("DB2")){
        	sqlstr = DBSqlUtil.getDB2PagedSql(sql,start,end);
        }
        return sqlstr;
    }
    
    public static List test(JdbcTemplate jdbcTemplate,String sql){
        String sqlstr = getQueryStr(sql,5,10);
    	List list = jdbcTemplate.queryForList(sqlstr.toString());
    	return list;
    }
}
