package com.example.my_automation.utils;

import com.example.my_automation.constants.Constants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class RequestSpecifications {
	public RequestSpecification getApiSpecsLogin() {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeader(Constants.Headers.ACCEPT, Constants.Headers.APPLICATION_JSON);
		return builder.build();
	}

	public RequestSpecification getApiSpecsSearch(String authToken) {
		RequestSpecification requestSpecification = getApiSpecsBase();
		requestSpecification.header(Constants.Headers.AUTHORIZATION, Constants.Headers.BEARER + authToken);
		return requestSpecification;
	}

	private RequestSpecification getApiSpecsBase() {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeader(Constants.Headers.CACHE_CONTROL, Constants.Headers.NO_CACHE);
		builder.addHeader(Constants.Headers.ACCEPT, Constants.Headers.APPLICATION_JSON);
		builder.addHeader(Constants.Headers.CONTENT_TYPE, Constants.Headers.APPLICATION_JSON);
		return builder.build();
	}

	public RequestSpecification getBasePath(String URI, int port, String basePath) {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeader(Constants.Headers.ACCEPT, Constants.Headers.APPLICATION_JSON);
		builder.setBaseUri(URI);
		builder.setPort(port);
		builder.setBasePath(basePath);
		return builder.build();
	}
	public RequestSpecification getImageSpec() {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setPort(443);
		builder.setUrlEncodingEnabled(false);
		RequestSpecification requestSpecification = builder.build();
		return requestSpecification;
	}
}
