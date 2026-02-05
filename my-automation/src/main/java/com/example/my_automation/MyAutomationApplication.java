package com.example.my_automation;


import com.example.my_automation.utils.ListClasses;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.util.List;

@SpringBootApplication
public class MyAutomationApplication {

	private final static String SUITE_NAME = "My automation suite";
	private static final Logger log = LoggerFactory.getLogger(MyAutomationApplication.class);
	public static void main(String[] args) {
		if (args.length == 0) {
			log.error("No suite name provided!");
			log.error("Usage: java -jar my-automation.jar <SuiteName>");
			System.exit(1);
		}
		String suite = args[0];
		log.info("Executing test suite: " + suite);

		XmlSuite xmlSuite = new XmlSuite();
		xmlSuite.setName(suite);
		xmlSuite.setGroupByInstances(true);

		// Create test and add test classes
		XmlTest xmlTest = new XmlTest(xmlSuite);
		xmlTest.setName(suite);
		xmlTest.setXmlClasses(ListClasses.getTestClasses(suite));

		// Run suite
		TestNG testngRunner = new TestNG();
		testngRunner.setXmlSuites(List.of(xmlSuite));
		testngRunner.run();

		if (testngRunner.hasFailure()) {
			//Jenkins, Docker, or a shell script can detect test failures automatically.
			System.exit(1);
		} else {
			log.info("Test execution finished SUCCESSFULLY.");
			//Jenkins, Docker, or a shell script know that the tests finished with success (Test step in Jenkins will pass).
			System.exit(0);
		}
	}

}
