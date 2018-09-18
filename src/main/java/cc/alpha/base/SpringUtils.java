package com.velcro.base;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext AC;
	
	public static final <T> T getBean(Class<T> requiredType) {
		return AC.getBean(requiredType);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T getBeanByName(String name) {
		return (T) AC.getBean(name);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AC = applicationContext;
	}

}
