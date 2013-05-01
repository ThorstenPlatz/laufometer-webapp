package de.tp82.laufometer.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Thorsten Platz
 */
@Service
public class BeanProvider implements ApplicationContextAware {
	private static BeanProvider instance = null;

	private ApplicationContext applicationContext;

	private BeanProvider() {
		BeanProvider.instance = this;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private static BeanProvider getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	private <T> T getBeanFromApplicationContext(Class<T> requiredType, Object ... args) {
		String[] beanNames = applicationContext.getBeanNamesForType(requiredType);
		if(beanNames.length != 1)
			throw new IllegalStateException("Not exactly one bean definition found for requested type: "
					+ requiredType.getName());


		return (T) applicationContext.getBean(beanNames[0], args);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return getInstance().getBeanFromApplicationContext(requiredType);
	}
}
