package com.example.my_automation.apis;

import com.example.my_automation.constants.Constants;
import com.example.my_automation.utils.RequestSpecifications;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.*;

@Component
public class AuthenticationServiceClient extends MyAppApiClient {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceClient.class);
	
	@Autowired
	private RequestSpecifications requestSpecifications;
	
	private static final String AUTHENTICATION_PATH = "/authentication?logoutIfLoggedin=true";
	private static final String LOGOUT_PATH = "/authentication/";
	
	public String login(String userName, String password) {
		log.info("Login Request to: " + baseURI + ":" + port + basePath + " Logging in with username " + userName + " and password " + password + " ...");
		
		Response response = given().spec(requestSpecifications.getApiSpecsLogin()).auth().preemptive().basic(userName, password)
				.when().post(AUTHENTICATION_PATH).then().log().ifError().statusCode(200).extract().response();
		String authToken = response.path(Constants.Headers.AUTHORIZATION_TOKEN);
		
		log.info("Login at: " + baseURI + " was successful");
		log.info("Auth Token : " + authToken);
		return authToken;
	}

	public void logout(String authToken) {
		log.info("Log out from " + baseURI + "...");
		
		given()
		.header(Constants.Headers.ACCEPT, Constants.Headers.APPLICATION_JSON)
		.header(Constants.Headers.AUTHORIZATION_TOKEN, authToken)
		.when().delete(LOGOUT_PATH + authToken).then().log().ifError().statusCode(204);
		authToken = null;
		
		log.info("Logged out successfully");
	}
}
