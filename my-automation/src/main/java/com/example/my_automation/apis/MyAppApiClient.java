package com.example.my_automation.apis;


import com.example.my_automation.auth.Authentication;
import com.example.my_automation.auth.CacheUtils;
import com.example.my_automation.config.ConsulValues;
import com.example.my_automation.constants.Constants;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;

@Component
public class MyAppApiClient {

	private static final Logger log = LoggerFactory.getLogger(MyAppApiClient.class);

	@Autowired
	@Lazy
	protected CacheUtils cacheUtils;

	@Autowired
	private ConsulValues consulProperties;

	@PostConstruct
	private void init() {
		baseURI = consulProperties.getApiUrl();
	}

	protected String getAuthToken() {

		Authentication auth = null;
		Class<?> clazz = null;
		Method method = null;
		Map<Thread, StackTraceElement[]> activeThreads = Thread.getAllStackTraces();
		for (Thread t : activeThreads.keySet()) {
			StackTraceElement[] stackTraceElements = activeThreads.get(t);
			for (StackTraceElement element : stackTraceElements) {
				if (element.getClassName().startsWith(Constants.PACKAGE)) {
					try {
						clazz = getAnnotatedClass(element);
						method = getAnnotatedMethod(element, clazz);
					} catch (ClassNotFoundException e) {
						log.error("Error when getting class with name " + element.getClassName());
						log.error(e.toString());
						break;
					} catch (NoClassDefFoundError e) {
						log.error("Could not find class with name " + element.getClassName());
						log.error(e.toString());
						break;
					}

					if (method != null) {
						auth = method.getAnnotation(Authentication.class);
					}
					if (auth != null) {
						break;
					}

					if (clazz != null) {
						auth = clazz.getAnnotation(Authentication.class);
					}
					if (auth != null) {
						break;
					}
				}
			}
			if(auth != null) {
				break;
			}
		}

		if (auth == null) {
			log.error("No credentials provided neither on method level nor on class level.");
			throw new RuntimeException("No credentials provided neither on method level nor on class level.");
		} else {
			return cacheUtils.getAuthToken(auth.username(), auth.password());
		}
	}

	private Class<?> getAnnotatedClass(StackTraceElement element) throws ClassNotFoundException {
		String className = element.getClassName();
		return Class.forName(className);
	}

	private Method getAnnotatedMethod(StackTraceElement element, Class<?> clazz) throws NoClassDefFoundError {
		String methodName = element.getMethodName();
		Method method = null;
		if (clazz != null) {
			for (Method aMethod : clazz.getMethods()) {
				if (aMethod.getName().equals(methodName)) {
					return aMethod;
				}
			}
		}
		return method;
	}
}
