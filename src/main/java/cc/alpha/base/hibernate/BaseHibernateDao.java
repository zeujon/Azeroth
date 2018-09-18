package cc.alpha.base.hibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.velcro.base.util.GenericsUtils;

/**
 * SpringSide Hibernate Entity Dao基类
 *
 * @author calvin,ajax
 */
abstract public class BaseHibernateDao<T> extends HibernateDaoSupport {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public void setSessionFacotry(SessionFactory sessionFacotry) {
        super.setSessionFactory(sessionFacotry);
    }
    
    /**
     * Dao所管理的Entity类型
     */
    private Class<T> entityClass;

    /**
     * 在构造函数中将T赋给supportsClass
     */
    public BaseHibernateDao() {
        entityClass = GenericsUtils.getGenericClass(getClass());
    }

    protected Class getEntityClass() {
        return entityClass;
    }

    /**
     * 查看对象时不受权限控制

     * @param id
     * @return
     */
    public T get(String id) {
    	if (id == null) {
			return null;
		}
		T o = (T) getHibernateTemplate().get(getEntityClass(),id);
		return o;
    }
    
    public <T> T get(Class<T> entityClass, Serializable id){
    	if (id == null) {
			return null;
		}
        return (T) getHibernateTemplate().get(entityClass, id);
    }
    
    public List<T> getAll() {
    	Session session = this.getSession();
    	Criteria criteria = session.createCriteria(getEntityClass());
    	List list = criteria.list();
    	this.releaseSession(session);
        return list;
    }

	/**
	 * 创建对象
	 * 
	 * @param o
	 */
	public void save(Object o) {
		Session session = this.getSession();
		// session.setFlushMode(FlushMode.AUTO);
		session.saveOrUpdate(o);
		session.flush();
		this.releaseSession(session);
		// getHibernateTemplate().saveOrUpdate(o);
	}
    
	/**
	 * 创建对象
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(final Object entity) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException {
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	/**
	 * 批量创建对象
	 * 
	 * @param list
	 */
	public void saveOrUpdateAll(final List list) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException {
				for (Object entity : list) {
					session.save(entity);
				}
				return null;
			}
		});
	}
    
    /**
     * 合并对象
     * @param o
     */
    public void merge(Object o) {
    	getHibernateTemplate().merge(o);
    }
    
    /**
     * 从数据库中删除
     * @param id
     */
    public void removeFromDB(Object o){
		try{
			getHibernateTemplate().delete(o);
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    /**
     * 从数据库中删除
     * @param id
     */
    public void removeByIdFromDB(String id){
		try{
			getHibernateTemplate().delete(this.get(id));
		}catch(Exception e){
			e.printStackTrace();
		}
    }

    public List<T> find(String hsql) {
        return (List<T>) getHibernateTemplate().find(hsql);
    }

    public List<T> find(String hsql, Object value) {
        return (List<T>) getHibernateTemplate().find(hsql, value);
    }

    public List<T> find(String hsql, Object[] values) {
        return (List<T>) getHibernateTemplate().find(hsql, values);
    }
    
    public List<T> find(Criteria criteria) {
        return criteria.list();
    }
    
    /**
     * 无参数分页查询，默认使用的jdbc支持scroll方式
     */
    public Page pagedQuery(String qryHql, int pageNo, int pageSize) {
    	boolean isscroll = true;
    	if(BaseContext.getDialectname().contains("Oracle"))
    		isscroll = false;
        return pagedQuery(qryHql, pageNo, pageSize, isscroll);
    }

    /**
     * 无参数分页查询,可以指定使用的数据库的Jdbc是否支持scroll方式
     */
    public Page pagedQuery(String qryHql, int pageNo, int pageSize, boolean isScroll) {
        return pagedQuery(qryHql, null, pageNo, pageSize, isScroll);
    }

    /**
     * 有参数的分页查询，默认使用的jdbc支持scroll方式
     */
    public Page pagedQuery(String qryHql, Object[] args, int pageNo, int pageSize) {
    	boolean isscroll = true;
    	if(BaseContext.getDialectname().contains("Oracle"))
    		isscroll = false;
        return pagedQuery(qryHql, args, pageNo, pageSize, isscroll);
    }

    /**
     * 有参数分页查询,可以指定使用的数据库的Jdbc是否支持scroll方式
     */
    public Page pagedQuery(String qryHql, Object[] args, int pageNo, int pageSize, boolean isScroll) {
        if (qryHql == null)
            throw new IllegalArgumentException("NULL is not a valid string");

        Session session = this.getSession();
        Query query = null;
        if (args == null)
            query = session.createQuery(qryHql);
        else {
            query = session.createQuery(qryHql);
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        Page page = HqlPage.getHibernatePageInstance(query, pageNo, pageSize, isScroll);
        this.releaseSession(session);
        return page;
    }

    /**
     * 使用Criteria进行分页查询，默认是jdbc支持scroll
     */
    public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
        return pagedQuery(criteria, pageNo, pageSize, true);
    }
    
    /**
     * 使用DetachedCriteria进行分页查询，默认是jdbc支持scroll,有权限控制
     */
    public Page pagedQuery(DetachedCriteria detachedcriteria, int pageNo, int pageSize) {
    	return pagedQuery(this.getCriteria(detachedcriteria), pageNo, pageSize, true);
    }

    /**
     * 转换DetachedCriteria为Criteria
     * @param detachedCriteria
     * @return
     */
	public Criteria getCriteria(final DetachedCriteria detachedCriteria){
		return  (Criteria) getHibernateTemplate().execute(new HibernateCallback<Object>() { 
			public Object doInHibernate(Session session) throws HibernateException { 
			//	DetachedCriteria detachedCriteria2 = .
				detachedCriteria.getExecutableCriteria(session); 
				return detachedCriteria.getExecutableCriteria(session); 
			} 
		}); 
	}
	  
    /**
     * 使用Criteria进行分页查询，可以指定jdbc是否支持scroll,有权限控制

     */
    public Page pagedQuery(Criteria criteria, int pageNo, int pageSize, boolean isScroll) {
    	Page pageObject = CriteriaPage.getHibernatePageInstance(criteria, pageNo, pageSize, isScroll);
    	criteria = null;
        return pageObject;
    }
    
    /**
     * 根据属性名和属性值查询对象 ，返回唯一对象
     */
    public T findBy(String name, Object value) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(getEntityClass());
		criteria.add(Restrictions.eq(name, value));
		T t = (T) criteria.uniqueResult();
		this.releaseSession(session);
		return t;
	}


    public List<T> findBy(DetachedCriteria detachedCriteria) {
        Criteria criteria = this.getCriteria(detachedCriteria);
        return this.find(criteria);
    }
    
    /**
     * 根据属性名和属性值查询对象 ，返回符合条件的对象列表
     */
    public List<T> findAllBy(String name, Object value) {
    	Session session = this.getSession();
        Criteria criteria = session.createCriteria(getEntityClass());
        criteria.add(Restrictions.eq(name, value));
        List<T> list =  criteria.list();
        this.releaseSession(session);
        return list;
    }

    /**
     * 根据属性名和属性值查询对象 ，返回符合条件的对象列表
     */
    public List<T> findAllByLike(String name, String value) {
    	Session session = this.getSession();
        Criteria criteria = session.createCriteria(getEntityClass());
        criteria.add(Restrictions.like(name, value, MatchMode.ANYWHERE));
        List<T> list =  criteria.list();
        this.releaseSession(session);
        return list;
    }

    /**
     * 执行HQL语句
     * @param hql
     */
    public int execUpdate(String hql){
		Assert.hasText(hql);
		int result = getHibernateTemplate().bulkUpdate(hql);
		return result;
    }
    
    //***********************************************点下一页时无页再次计算count
    public Page pagedQuery(Criteria criteria,int totalcount, int pageNo, int pageSize) {
    	Page pageObject = CriteriaPage.getHibernatePageInstance(criteria, totalcount,pageNo, pageSize);
    	criteria = null;
        return pageObject;
    }
  //***********************************************点下一页时无页再次计算count
}