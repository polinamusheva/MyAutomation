package com.example.my_automation.utils;

import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

public class CustomTestNgListener extends AllureTestNg implements ISuiteListener, ITestListener {

	private static final Logger log = LoggerFactory.getLogger(CustomTestNgListener.class);
	
	@Override
	public void onStart(ISuite suite) {
		super.onStart(suite);
		log.info("Orbis-Automated-Tests --- Start suite: {}", suite.getName());
	}
	
	@Override
	public void onFinish(ISuite suite) {
		super.onFinish(suite);
		log.info("Orbis-Automated-Tests --- End suite: {}", suite.getName());
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		super.onTestFailure(result);
		final Throwable resultThrowable = result.getThrowable();
        String message = resultThrowable.getMessage() != null ? resultThrowable.getMessage() :
                resultThrowable.getCause().getMessage();
        if (message == null) {
            message = "Missing error message for falling!";
        }
        log.error("Finishing test: " + getTestMethodName(result) + " finish with failure! " + message);
        resultThrowable.printStackTrace();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		super.onTestSkipped(result);
		log.warn("Test: " + getTestMethodName(result) + " was skipped");
	}

	@Override
	public void onTestStart(ITestResult testResult) {
		super.onTestStart(testResult);
		log.info("Starting test: " + getTestMethodName(testResult));
	}

	@Override
	public void onTestSuccess(ITestResult testResult) {
		super.onTestSuccess(testResult);
		log.info("Finishing test: " + getTestMethodName(testResult) + " finish successfully");
	}
	
	@Override
	public void onStart(ITestContext context) {
		super.onStart(context);
		log.info("Starting test class: " + context.getName());
	}
	
	@Override
	public void onFinish(ITestContext context) {
		super.onFinish(context);
		log.info("Finishing test class: " + context.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		super.onTestFailedButWithinSuccessPercentage(result);
	}

    private static String getTestMethodName(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getName();
    }
}
