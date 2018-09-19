package cc.alpha.base.utils;

public class SQLObject {
	private String pk = "";
	private String distinct = "";
	private String tables = "";
	private String showcolumns = "";
	private String wherestr = "";
	private String groupbystr = "";
	private String orderbystr = "";
	private int curpage = 0;
	private int pageNo = 0;
	private int pageSize = 0;
	private int totalCount = 0;
	
	public int getCurpage() {
		return curpage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public String getGroupbystr() {
		return groupbystr;
	}
	public void setGroupbystr(String groupbystr) {
		this.groupbystr = groupbystr;
	}
	public String getOrderbystr() {
		return orderbystr;
	}
	public void setOrderbystr(String orderbystr) {
		this.orderbystr = orderbystr;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getShowcolumns() {
		return showcolumns;
	}
	public void setShowcolumns(String showcolumns) {
		this.showcolumns = showcolumns;
	}
	public String getTables() {
		return tables;
	}
	public void setTables(String tables) {
		this.tables = tables;
	}
	public String getWherestr() {
		return wherestr;
	}
	public void setWherestr(String wherestr) {
		this.wherestr = wherestr;
	}
	
	public int getStart(){
		int startIndex = pageSize*(pageNo-1);
        return startIndex;
	}
	public String getDistinct() {
		return distinct;
	}
	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}
}
