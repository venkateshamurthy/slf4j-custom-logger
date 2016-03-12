package com.github.venkateshamurthy.util.tostring.examples;

import java.util.Date;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.venkateshamurthy.util.tostring.xtend.Employee;
import com.github.venkateshamurthy.util.tostring.xtend.Employer;

@Test(groups = "common")
public class TestLoggingLevelledToString {

	private static final String CONFIG_FILE_KEY = "log4j.configuration";
	private final CustomAppender customAppender = new CustomAppender();
	org.apache.log4j.Logger root;

	@BeforeMethod
	public void setUp() throws Exception {
		System.setProperty(CONFIG_FILE_KEY,
				"custom-tostring-logger-test-log4j.properties");
		customAppender.setThreshold(Level.DEBUG);
		root = org.apache.log4j.Logger.getRootLogger();
		root.addAppender(customAppender);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		customAppender.close();
		root.removeAppender(customAppender);
		root.getLoggerRepository().resetConfiguration();
	}

	public void testManager() {
		Logger log = LoggerFactory.getLogger(this.getClass());
		Employee a = new Employee(new Date(70, 6, 24), "Aemployee", 2500000d);
		Employee b = new Employee(new Date(75, 9, 20), "Bemployee", 2500000d);
		Employer m = new Employer(new Date(65, 1, 1), "Manager", 5000000d, a, b);
		log.debug("Manager: {}", m);
		String msg="Manager: [Manager Mon Feb 01 00:00:00 IST 1965 5000000.0 ] "
				+ "Aemployee Fri Jul 24 00:00:00 IST 1970 2500000.0  "
				+ "Bemployee Mon Oct 20 00:00:00 IST 1975 2500000.0   ";
		Assert.assertEquals(customAppender.eventsByLevel(Level.DEBUG).size(),1);
		Assert.assertEquals(customAppender.topEventByLevel(Level.DEBUG).getMessage(), msg);

	}
}
