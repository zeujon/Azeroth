package cc.alpha.base.mybatis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.velcro.base.page.PageWeb;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * MyBatis基类Dao 
 * @Copyright 巨人网络-2014
 * @author 刘平
 * @date 2014-9-28
 * =================Modify Record=================
 * @Modifier			@date			@Content
 * 刘平				2014-09-28		新增
 * 刘平				2014-10-14		新增基于自定义的sql语句分页方法
 */
public abstract class MybatisBaseDao<T extends Object> extends SqlSessionDaoSupport
{
	/** 接口泛型T对应的Class字节码 */
	private Class<T> clazz;
	
	
	
	/**
	 * 基类Dao接口实现构造函数，主要用于获取接口泛型T对应的Class字节码
	 */
	@SuppressWarnings("unchecked")
	public MybatisBaseDao() 
	{
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass(); 
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		clazz = (Class<T>) typeArguments[0];
		
	}

	/**
	 * 
	 * 查询单个接口泛型对象T
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param	parameter		参数
	 * @return	T				接口泛型对象T
	 */
	public T selectOne(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return (T) super.getSqlSession().selectOne(statement, parameter);
		return (T) super.getSqlSession().selectOne(statement);
	}
	
	/**
	 * 
	 * 查询单个非接口泛型对象
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param 	parameter		参数
	 * @return	Object			单个非接口泛型对象
	 */
	public Object selectOneByParameter(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().selectOne(statement, parameter);
		return super.getSqlSession().selectOne(statement);
	}
	
	/**
	 * 查询接口泛型对象T列表
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param	parameter		参数
	 * @return	List<T>			接口泛型对象T列表
	 */
	public List<T> selectList(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().selectList(statement, parameter);

		return super.getSqlSession().selectList(statement);
	}
	
	/**
	 * 查询非接口泛型对象T列表
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param	parameter		参数
	 * @return	List<?>			非接口泛型对象T列表
	 */
	public List<?> selectListByParameter(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().selectList(statement, parameter);

		return super.getSqlSession().selectList(statement);
	}
	
	/**
	 * 分页查询(基于方言的分页)
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param	sqlId		SQL语句id
	 * @param 	page		分页对象参数
	 * @return	Page<T>		查询后返回的分页对象
	 */
//	public Page<T> selectForPage(String sqlId, Page<T> page)
//	{
//		String statement = constructStatement(sqlId);
//		//设置分页统计标识为true，让分页插件执行分页统计
//		Map<String, Object> paramMap = page.getParamMap();
//		paramMap.put("pageCountFlag", true);
//		List<T> resultList = super.getSqlSession().selectList(statement, page);
//		page.setResultList(resultList);
//
//		return page;
//	}
//
	/**
	 * 分页查询(基于自定义的sql语句分页)
	 * @author 刘平
	 * @date 2014-10-14
	 * @param 	pageCountSqlId		分页统计SQL语句id
	 * @param 	pageSqlid			分页查询SQL语句id
	 * @param 	page				查询后返回的分页对象
	 * @return	Page<T>				查询后返回的分页对象
	 */
	public PageWeb selectForPage(String pageCountSqlId, String pageSqlid, PageWeb page)
	{
		//1、查询总记录数、设置总页数
		String statement = constructStatement(pageCountSqlId);
		int totalCount = (int) super.getSqlSession().selectOne(statement, page);
		if(totalCount == 0){
			return page;
		}
		page.setTotalCount(totalCount);
		long totalPage = page.computeTotalPage(totalCount, page.getPageSize());
		page.setTotalPage(totalPage);

		statement = constructStatement(pageSqlid);

		List<T> resultList = super.getSqlSession().selectList(statement, page,
				new RowBounds((page.getPageNo() - 1) * page.getPageSize(), page.getPageSize()));

		page.setResultList(resultList);

		return page;
	}
	
	
	/**
	 * 根据接口泛型对象T插入
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId		SQL语句id
	 * @param 	T			接口泛型对象T
	 * @return	int			插入动作受影响的行数	
	 */
	public int insert(String sqlId, T T)
	{
		String statement = constructStatement(sqlId);
		if(T == null)
			throw new IllegalArgumentException("接口泛型对象T不能为空！");
		
		return super.getSqlSession().insert(statement, T);
	}
	
	/**
	 * 根据非接口泛型对象参数插入
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param 	parameter		非接口泛型对象参数
	 * @return	int				插入动作受影响的行数
	 */
	public int insertObject(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().insert(statement, parameter);

		return super.getSqlSession().insert(statement);
	}
	
	/**
	 * 根据接口泛型对象T更新
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param	sqlId		SQL语句id
	 * @param 	T			接口泛型对象T
	 * @return	int			更新动作受影响的行数
	 */
	public int update(String sqlId, T T)
	{
		String statement = constructStatement(sqlId);
		if(T == null)
			throw new IllegalArgumentException("接口泛型对象T不能为空！");
		
		return super.getSqlSession().update(statement, T);
	}
	
	/**
	 * 根据非接口泛型对象参数更新
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param 	parameter		非接口泛型对象参数
	 * @return	int				更新动作受影响的行数
	 */
	public int updateByParameter(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().update(statement, parameter);

		return super.getSqlSession().update(statement);
	}
	
	/**
	 * 根据接口泛型对象T删除
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param	sqlId		SQL语句id
	 * @param 	T			接口泛型对象T
	 * @return	int			删除动作受影响的行数
	 */
	public int delete(String sqlId, T T)
	{
		String statement = constructStatement(sqlId);
		if(T == null)
			throw new IllegalArgumentException("接口泛型对象T不能为空！");
		
		return super.getSqlSession().delete(statement, T);
	}
	
	/**
	 * 根据非接口泛型对象参数删除
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId			SQL语句id
	 * @param 	parameter		非接口泛型对象参数
	 * @return	int				删除动作受影响的行数
	 */
	public int deleteByParameter(String sqlId, Object parameter)
	{
		String statement = constructStatement(sqlId);
		if(parameter != null)
			return super.getSqlSession().delete(statement, parameter);

		return super.getSqlSession().delete(statement);
	}
	
	/**
	 * 构造SQL语句表达式
	 * @author 刘平 
	 * @date 2014-9-28 
	 * @param 	sqlId		SQL语句id
	 * @return	String		SQL语句表达式
	 */
	private String constructStatement(String sqlId)
	{
		if(StringUtils.isBlank(sqlId))
			throw new IllegalArgumentException("SQL语句id不能为空！");
		
		return clazz.getName() + "." + sqlId;
	}

}
