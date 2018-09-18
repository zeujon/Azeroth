package cc.alpha.base.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;

import com.velcro.base.ServletInterceptor;
import com.velcro.base.util.DateHelper;
import com.velcro.base.util.ITransctVoField;
import com.velcro.base.util.SimpleDateFormatAnnotations;
import com.velcro.ztgame.bidding.domain.BidForm;

public class RmVoHelper {

	/**
	 * 功能: 从map中往vo注值
	 *
	 * @param vo
	 * @param mValue
	 * @return
	 */
	public static boolean populate(Object vo, final Map mValue) {
		boolean rtValue = true;
		accessVo(vo, new ITransctVoField() {
			public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
				if (!pd.getName().equals("class")) {
					if (mValue.get(pd.getName()) == null) {
						return 0;
					} else {
						Object targetValue = mValue.get(pd.getName());
						if (pd.getWriteMethod() != null) {
							Class<?>[] s = pd.getWriteMethod().getParameterTypes();
							if (s[0].equals(String.class)) {
								bw.setPropertyValue(pd.getName(), targetValue);
							} else if (s[0].equals(Integer.class)) {
								if (!targetValue.toString().equals("")) {
									Integer temp = Integer.valueOf(targetValue.toString());
									bw.setPropertyValue(pd.getName(), temp);
								}
							} else if (s[0].equals(Date.class)) {
								if (pd.getWriteMethod().isAnnotationPresent(SimpleDateFormatAnnotations.class)) {// 有日期格式
									SimpleDateFormatAnnotations strFormat = pd.getWriteMethod().getAnnotation(SimpleDateFormatAnnotations.class);
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat.value());
									Date date = new Date();
									try {
										date = simpleDateFormat.parse(targetValue.toString());
										bw.setPropertyValue(pd.getName(), date);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						return 1;
					}
				} else {
					return 0;
				}
			}
		});
		return rtValue;
	}

	/**
	 * 借助BeanWrapper循环Vo
	 * 
	 * @param obj
	 *            输入一个VO
	 * @return 被替换的值个数
	 */
	public static int accessVo(Object obj, ITransctVoField transctVoField) {
		int returnCount = 0;
		try {
			BeanWrapper bw = new BeanWrapperImpl(obj);
			PropertyDescriptor pd[] = bw.getPropertyDescriptors();
			for (int i = 0; i < pd.length; i++) {
				try {
					returnCount += transctVoField.transctVo(bw, pd[i]);
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				} catch (NotReadablePropertyException e) {
					e.printStackTrace();
					continue;
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnCount;
	}

	/**
	 * 从request中往vo注值
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T buildVoFromRequest(HttpServletRequest request, Class<T> clazz) {
		return buildVoFromObject(request, clazz);
	}
	
	/**
	 * 从map中往vo注值
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T buildVoFromMap(Map map, Class<T> clazz) {
		return buildVoFromObject(map, clazz);
	}
	
	/**
	 * 从object中往vo注值
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T buildVoFromObject(Object object, Class<T> clazz) {
		T vo = null;
		try {
			vo = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Object value = getValueFromObject(object, field.getName());
				field.setAccessible(true);
				if (value != null) {
					value = value.toString().trim();
					if (field.getType().equals(Long.class)) {
						field.set(vo, Long.valueOf(value.toString()));
					} else if (field.getType().equals(Integer.class)) {
						field.set(vo, Integer.valueOf(value.toString()));
					} else if (field.getType().equals(String.class)) {
						field.set(vo, value);
					} else if (field.getType().equals(Date.class)) {
						Date d = DateHelper.stringtoDate(value.toString());
						field.set(vo, d);
					} else {
						field.set(vo, value);
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		return vo;
	}
	
	/**
	 * 从不同类型的Object中取出key为fieldname的值
	 * @param object
	 * @param fieldname
	 * @return
	 */
	public static Object getValueFromObject(Object object, String fieldname){
		if(fieldname==null || fieldname.trim()=="" || object == null){
			return null;
		}
		if( object instanceof Map){//object是一个map
			return ((Map) object).get(fieldname);
		} else if (object instanceof ServletRequest) {//object是一个Request
			return ((ServletRequest) object).getParameter(fieldname);
		} else {
			return null;
		}
	}
	
	
	public static <T> T populate(Class clazz){
		Map<String,Object> parameterMap = ServletInterceptor.getRequest().getParameterMap();
		Object obj = null;
		try {
			Constructor ctor = clazz.getConstructor();
			obj = ctor.newInstance();
			BeanUtils.populate(obj, parameterMap);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return (T) obj;
	}
	
	public static void main(String args[]) {
		Map map = new java.util.HashMap<String, String>();
		map.put("startTime", "2018-05-01");
		map.put("purpose", "这次招标是针对游戏广告的");
		map.put("type", "5");
		//示例
		BidForm bidForm = RmVoHelper.buildVoFromMap(map, BidForm.class);
		System.out.println(bidForm.getPurpose());
		
		BidForm bidForm1 = new BidForm();
		RmVoHelper.populate(bidForm1, map);
		System.out.println(bidForm1.getPurpose());
		
		//示例
		HttpServletRequest request = null;
		BidForm bidForm2 = RmVoHelper.buildVoFromRequest(request,BidForm.class);
		System.out.println(bidForm2.getPurpose());
		
		

	}

}
