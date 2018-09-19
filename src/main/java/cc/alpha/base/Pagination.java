package cc.alpha.base;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class Pagination {
	public static final int NUMBERS_PER_PAGE = 10;
	private String strTableKeyID = "ID";

	protected final Log logger = LogFactory.getLog(getClass());

	

	private JdbcTemplate jTemplate;
	private int numPerPage;
	private String sql;
	private int currentPage;

	public Pagination(String sql, int currentPage) {
		if (jTemplate == null) {
			logger.error("jTemplate is null,please initial it first. ");
		} else if (sql.equals("")) {
			logger.debug("sql is empty,please initial it first. ");
		}
		new Pagination(sql, currentPage, NUMBERS_PER_PAGE, jTemplate);
	}

	public Pagination(String sql, int currentPage, int numPerPage,
			JdbcTemplate jTemplate) {
		if (jTemplate == null) {
			logger.error("jTemplate is null,please initial it first. ");
		} else if (sql.equals("")) {
			logger.debug("sql is empty,please initial it first. ");
		}
		this.sql = sql;
		this.currentPage = currentPage;
		this.numPerPage = numPerPage;
		this.jTemplate = jTemplate;
		

	}

	public List getResultList() {
		StringBuffer paginationSQL = new StringBuffer(" SELECT TOP ");
		paginationSQL.append(numPerPage);
		paginationSQL.append(" * FROM ( ");
		paginationSQL.append(sql);
		paginationSQL.append(" ) TOTALTABLE WHERE TOTALTABLE.");
		paginationSQL.append(strTableKeyID);
		paginationSQL.append(" NOT IN ( ");		
		paginationSQL.append(" SELECT TOP ");
		paginationSQL.append((numPerPage*(currentPage-1)));
		paginationSQL.append(" TOTALTABLE.");
		paginationSQL.append(strTableKeyID);
		paginationSQL.append(" FROM ( ");
		paginationSQL.append(sql);
		paginationSQL.append(" ) TOTALTABLE )");
		
		logger.info(new StringBuffer("Pageination SQL = ").append(paginationSQL).toString());

		return jTemplate.queryForList(paginationSQL.toString());
		
	}

	public void setStrTableKeyID(String strTableKeyID) {
		this.strTableKeyID = strTableKeyID;
	}

}
