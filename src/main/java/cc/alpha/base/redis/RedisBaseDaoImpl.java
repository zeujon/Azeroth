package cc.alpha.base.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


/**
 * Redis基类Dao接口实现类
 * 
 * @Copyright 巨人网络-2014
 * @author 刘平
 * @date 2014-12-27 =================Modify Record=================
 * @Modifier @date @Content 刘平 2014-09-28 新增
 */
public class RedisBaseDaoImpl implements RedisBaseDao {
	@SuppressWarnings("rawtypes")
	// 主缓存
	private RedisTemplate redisTemplate;

	/** ===============Redis-String数据结构接口START=============== */
	/**
	 * 设置字符串
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param value
	 *            字符串值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(String key, String value) {
		try {
			ValueOperations<String, String> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, value);
		} catch (Exception e) {
        e.printStackTrace();
		}
	}

	/**
	 * 设置字符串(含过期时间)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param value
	 *            字符串值
	 * @param timeout
	 *            过期时间，单位：秒
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(String key, String value, long timeout) {
		try {
			ValueOperations<String, String> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, value, timeout, TimeUnit.SECONDS);

		} catch (Exception e) {
			  e.printStackTrace();
		}
	}

	/**
	 * 设置多个字符串
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param paramMap
	 *            字符串Map
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void mset(Map<String, String> paramMap) {
		try {
			ValueOperations<String, String> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.multiSet(paramMap);

		} catch (Exception e) {

		}
	}

	/**
	 * 获取字符串
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @return String key对应的字符串值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String get(String key) {
		try{
		ValueOperations<String, String> valueOperations = redisTemplate
				.opsForValue();
		return valueOperations.get(key);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return null;
	}

	/**
	 * 根据key列表获取字符串列表
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param keys
	 *            key列表
	 * @return List<String> key列表对应的字符串列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> mget(List<String> keys) {
		ValueOperations<String, String> valueOperations = redisTemplate
				.opsForValue();
		return valueOperations.multiGet(keys);
	}

	/**
	 * 设置对象(采用Redis的String存储)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param T
	 *            对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> void setObject(String key, T T) {
		try {
			ValueOperations<String, T> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, T);
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}

	/**
	 * 设置对象(采用Redis的String存储，含过期时间)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param T
	 *            对象
	 * @param timeout
	 *            过期时间，单位：秒
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> void setObject(String key, T T, long timeout) {
		try {
			ValueOperations<String, T> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, T, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	/**
	 * 设置多key的同一类型对象(采用Redis的String存储)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param paramMap
	 *            参数Map
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> void msetObject(Map<String, T> paramMap) {
		try {
			ValueOperations<String, T> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.multiSet(paramMap);
		} catch (Exception e) {

		}
	}

	/**
	 * 根据key获取对象(对象采用Redis的String存储)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @return T 对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T getObject(String key) {
 
		try{
		ValueOperations<String, T> valueOperations = redisTemplate
				.opsForValue();
		return valueOperations.get(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * 根据key列表获取同一对象列表(列表中的对象采用Redis的String存储)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param keys
	 *            keys列表
	 * @return List<T> 对象列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> List<T> mgetObject(List<String> keys) {
		ValueOperations<String, T> valueOperations = redisTemplate
				.opsForValue();
		return valueOperations.multiGet(keys);
	}

	/** ===============Redis-String数据结构接口END=============== */

	/** ===============Redis-HASH数据结构接口START=============== */
	/**
	 * Hash设置,可用于保存对象或者保存对象的单个field
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Hash表的key
	 * @param field
	 *            Hash表中的域field
	 * @param T
	 *            对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> void hset(String key, String field, T T) {

		try {
			HashOperations<String, String, T> hashOperations = redisTemplate
					.opsForHash();
			hashOperations.put(key, field, T);
		} catch (Exception e) {

		}

	}

	/**
	 * Hash批量设置,可用于保存多个对象或者保存单个对象的多个field
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Hash表的key
	 * @param paramMap
	 *            Hash表的field和Value组成的Map
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> void hmset(String key,
			Map<String, T> paramMap) {
		try {
			HashOperations<String, String, T> hashOperations = redisTemplate
					.opsForHash();
			hashOperations.putAll(key, paramMap);
		} catch (Exception e) {

		}

	}

	/**
	 * 根据Hash表的key和域Field获取对应的Value(可用于获取对象或者获取对象的单个field)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Hash表的key
	 * @param field
	 *            Hash表中的域field
	 * @return T Hash的key和域Field获取对应的Value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T hget(String key, String field) {
		HashOperations<String, String, T> hashOperations = redisTemplate
				.opsForHash();
		return hashOperations.get(key, field);
	}

	/**
	 * 根据Hash表的key和域Field列表获取对应的Value列表(可用于获取多个对象或者对象的多个属性)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Hash表的key
	 * @param fields
	 *            域Field列表
	 * @return List<T> Hash的key和域Field列表获取对应的Value列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Collection<T> hmget(String key,
			List<String> fields) {
		HashOperations<String, String, T> hashOperations = redisTemplate
				.opsForHash();
		return hashOperations.multiGet(key, fields); 
	}

	/**
	 * 根据Hash表的key获取对应的所有对象(可用于获取多个对象)
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Hash表的key
	 * @return Map<String, T> Hash表的key对应的所有对象Map
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Map<String, T> hgetAll(String key) {
		HashOperations<String, String, T> hashOperations = redisTemplate
				.opsForHash();
		return hashOperations.entries(key);
	}

	/**
	 * 根据Hash表的key和域Field进行删除
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param <T>
	 * @param key
	 *            Hash表的key
	 * @param fields
	 *            Hash表对应域Field数组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void hDel(String key, Object... fields) {
		HashOperations<String, String, T> hashOperations = redisTemplate
				.opsForHash();
		try {
			hashOperations.delete(key, fields);

		} catch (Exception e) {

		}
	}

	/** ===============Redis-HASH数据结构接口END=============== */

	/** ===============Redis接口START=============== */
	/**
	 * 设置key在多少秒后过期
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param timeout
	 *            过期时间
	 * @return boolean 是否设置成功
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean expire(String key, long timeout) {
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置key在固定的某个时刻后过期
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            key
	 * @param date
	 *            固定的某个时刻
	 * @return boolean 是否设置成功
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * 根据key删除单个对象
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param key
	 *            Redis中存储的key
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(String key) {
		try {
			redisTemplate.delete(key);

		} catch (Exception e) {

		}
	}

	/**
	 * 根据key列表删除多个对象
	 * 
	 * @author 刘平
	 * @date 2014-12-27
	 * @param keys
	 *            keys列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteAll(List<String> keys) {
		try {
			redisTemplate.delete(keys);
		} catch (Exception e) {

		}

	}

	/** ===============Redis接口END=============== */

	@SuppressWarnings("rawtypes")
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	@SuppressWarnings("rawtypes")
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public <T extends Serializable> void setObject(String key, List<T> list) {
		// ValueOperations<String, T> valueOperations =
		// redisTemplate.opsForValue();
		// Map map=new HashedMap<String, List>();
		// map.put(key, list);
		// valueOperations.multiSet(map);

		try {
			@SuppressWarnings("unchecked")
			ValueOperations<String, List<T>> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, list);
		} catch (Exception e) {

		}
	}

	public <T extends Serializable> void setObject(String key, List<T> list,
			long timeout) {
		// ValueOperations<String, T> valueOperations =
		// redisTemplate.opsForValue();
		// Map map=new HashedMap<String, List>();
		// map.put(key, list);
		// valueOperations.multiSet(map,timeout, TimeUnit.SECONDS);

		try {
			@SuppressWarnings("unchecked")
			ValueOperations<String, List<T>> valueOperations = redisTemplate
					.opsForValue();
			valueOperations.set(key, list, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("rawtypes")
	public void setredisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
