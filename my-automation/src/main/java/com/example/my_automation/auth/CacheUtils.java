package com.example.my_automation.auth;

import com.example.my_automation.clients.AuthenticationServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheUtils {

	@Autowired
	private AuthenticationServiceClient authenticationServiceClient;

	private final int CLEAR_CACHE_DELAY = 1000 * 60 * 5;

	private Map<String, String> userTokens = new ConcurrentHashMap<>();

	public String getAuthToken(String username, String password) {
		String token = userTokens.get(username);
		if (token == null) {
			token = authenticationServiceClient.login(username, password);
			userTokens.put(username, token);
		}
		return token;
	}

	@Scheduled(fixedDelay = CLEAR_CACHE_DELAY)
	private void clearCache() {
			userTokens.clear();
	}
}
