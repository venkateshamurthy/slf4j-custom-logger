package com.github.venkateshamurthy.util.logging;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import net.sf.oval.internal.util.ArrayUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

/**
 * Test {@link CustomFormattingLog4jLoggerAdapter}.
 */
@Test(groups = "common")
public class TestCustomFormattingLog4jLoggerAdapter {
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

	@Test
	public void testTrace() {
		customAppender.setThreshold(Level.TRACE);
		root.setLevel(Level.TRACE);
		Logger logger = LoggerFactory.getLogger("testTrace");
		logger.trace("Hello {} {}", "my", "world.");
		assertEquals(1, customAppender.size());
		assertEquals("Hello my world.",
				customAppender.topEventByLevel(Level.TRACE).getMessage());
		logger.trace("Hello {} {}", new Object[] { "my", "world." });
		assertEquals(2, customAppender.size());
		assertEquals("Hello my world.",
				customAppender.topEventByLevel(Level.TRACE).getMessage());
		logger.trace("Hello {} {}", new Object[] { "my", "world." },
				"your world");
		assertEquals(3, customAppender.size());
		assertEquals("Hello [my, world.] your world", customAppender
				.topEventByLevel(Level.TRACE).getMessage());
	}

	@Test
	public void test1() {
		Logger logger = LoggerFactory.getLogger("test1");
		logger.debug("Hello world.");
		logger.trace("Hello my world.");
		assertEquals(1, customAppender.size());
		assertEquals("Hello world.", customAppender.getList().get(0)
				.getMessage());
	}

	@Test
	public void test2() {
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Exception e = new Exception("This is a test exception.");
		Logger logger = LoggerFactory.getLogger("test2");

		logger.trace("Hello trace.");
		assertTrue(customAppender.getMap().isEmpty());
		assertTrue(customAppender.getList().isEmpty());

		logger.debug("Hello world 1.");
		logger.debug("Hello world {}", i1);
		assertEquals("Hello world 1",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("val={} val={}", i1, i2);
		assertEquals("val=1 val=2", customAppender.topEventByLevel(Level.DEBUG)
				.getMessage());
		logger.debug("val={} val={} val={}", new Object[] { i1, i2, i3 });
		assertEquals("val=1 val=2 val=3",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("val={} val={} val={}", i1, i2, i3);
		assertEquals("val=1 val=2 val=3",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("Hello world 2", e);
		logger.info("Hello world 2.");

		logger.warn("Hello world 3.");
		logger.warn("Hello world 3", e);
		assertEquals("Hello world 3", customAppender
				.topEventByLevel(Level.WARN).getMessage());
		assertEquals(e, customAppender.topEventByLevel(Level.WARN)
				.getThrowableInformation().getThrowable());
		logger.error("Hello world 4.");
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		logger.error("Hello world {}.", new Integer(4));
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		logger.error("Hello world 4.", e);
		assertEquals(e, customAppender.topEventByLevel(Level.ERROR)
				.getThrowableInformation().getThrowable());
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		assertEquals(12, customAppender.size());
	}

	@Test
	public void test2_Primitives() {
		byte i1 = 1;
		byte i2 = 2;
		byte i3 = 3;
		Exception e = new Exception("This is a test exception.");
		Logger logger = LoggerFactory.getLogger("test2");

		logger.trace("Hello trace.");
		assertTrue(customAppender.getMap().isEmpty());
		assertTrue(customAppender.getList().isEmpty());

		logger.debug("Hello world 1.");
		logger.debug("Hello world {}", i1);
		assertEquals("Hello world 1",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("val={} val={}", i1, i2);
		assertEquals("val=1 val=2", customAppender.topEventByLevel(Level.DEBUG)
				.getMessage());
		logger.debug("val={} val={} val={}", new byte[] { i1, i2, i3 });
		assertEquals("val=1 val=2 val=3",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("val={} val={} val={}", i1, i2, i3);
		assertEquals("val=1 val=2 val=3",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
		logger.debug("Hello world 2", e);
		logger.info("Hello world 2.");

		logger.warn("Hello world 3.");
		logger.warn("Hello world 3", e);
		assertEquals("Hello world 3", customAppender
				.topEventByLevel(Level.WARN).getMessage());
		assertEquals(e, customAppender.topEventByLevel(Level.WARN)
				.getThrowableInformation().getThrowable());
		logger.error("Hello world 4.");
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		logger.error("Hello world {}.", 4);
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		logger.error("Hello world 4.", e);
		assertEquals(e, customAppender.topEventByLevel(Level.ERROR)
				.getThrowableInformation().getThrowable());
		assertEquals("Hello world 4.",
				customAppender.topEventByLevel(Level.ERROR).getMessage());
		assertEquals(12, customAppender.size());
	}

	@Test
	public void testEscapes() {
		Exception e = new Exception("This is a test exception.");
		Logger logger = LoggerFactory.getLogger("testEscapes");
		logger.debug("Hello \\{}", "world");
		assertEquals("Hello {}", customAppender.topEventByLevel(Level.DEBUG)
				.getMessage());
		logger.debug("Hello c:\\\\{}", "home");
		assertEquals("Hello c:\\home",
				customAppender.topEventByLevel(Level.DEBUG).getMessage());
	}

	@Test
	public void testNull() {
		Logger logger = LoggerFactory.getLogger("testNull");
		logger.trace(null);
		logger.debug(null);
		logger.info(null);
		logger.warn(null);
		logger.error(null);

		Exception e = new Exception("This is a test exception.");
		logger.debug(null, e);
		logger.info(null, e);
		logger.warn(null, e);
		logger.error(null, e);
		assertEquals(8, customAppender.size());
	}

	@Test
	public void testNullParameter() {
		Logger logger = LoggerFactory.getLogger("testNullParameter");
		String[] parameters = null;
		String msg = "hello {}";
		String expectedMsg = "hello {}";
		logger.debug(msg, (Object[]) parameters);

		assertEquals(1, customAppender.size());
		LoggingEvent event = customAppender.getList().get(0);
		assertEquals(expectedMsg, event.getMessage());
	}

	@Test
	public void testMarker() {
		Logger logger = LoggerFactory.getLogger("testMarker");
		Marker blue = MarkerFactory.getMarker("BLUE");
		logger.trace(blue, "hello");
		assertTrue(customAppender.size() == 0);
		logger.debug(blue, "hello");
		assertEquals("hello", customAppender.topEventByLevel(Level.DEBUG)
				.getMessage());
		assertEquals(1, customAppender.eventsByLevel(Level.DEBUG).size());
		logger.info(blue, "hello");
		assertEquals(1, customAppender.eventsByLevel(Level.INFO).size());
		logger.warn(blue, "hello");
		assertEquals(1, customAppender.eventsByLevel(Level.WARN).size());
		logger.error(blue, "hello");
		assertEquals(1, customAppender.eventsByLevel(Level.ERROR).size());

		logger.debug(blue, "hello {}", "world");
		assertEquals("hello world", customAppender.topEventByLevel(Level.DEBUG)
				.getMessage());
		assertEquals(2, customAppender.eventsByLevel(Level.DEBUG).size());
		logger.info(blue, "hello {}", "world");
		assertEquals("hello world", customAppender.topEventByLevel(Level.INFO)
				.getMessage());
		assertEquals(2, customAppender.eventsByLevel(Level.INFO).size());
		logger.warn(blue, "hello {}", "world");
		assertEquals("hello world", customAppender.topEventByLevel(Level.WARN)
				.getMessage());
		assertEquals(2, customAppender.eventsByLevel(Level.WARN).size());
		logger.error(blue, "hello {}", "world");

		logger.debug(blue, "hello {} and {} ", "world", "universe");
		assertEquals("hello world and universe ", customAppender
				.topEventByLevel(Level.DEBUG).getMessage());
		assertEquals(3, customAppender.eventsByLevel(Level.DEBUG).size());
		logger.info(blue, "hello {} and {} ", "world", "universe");
		assertEquals("hello world and universe ", customAppender
				.topEventByLevel(Level.INFO).getMessage());
		assertEquals(3, customAppender.eventsByLevel(Level.INFO).size());
		logger.warn(blue, "hello {} and {} ", "world", "universe");
		assertEquals("hello world and universe ", customAppender
				.topEventByLevel(Level.WARN).getMessage());
		assertEquals(3, customAppender.eventsByLevel(Level.WARN).size());
		logger.error(blue, "hello {} and {} ", "world", "universe");
		assertEquals("hello world and universe ", customAppender
				.topEventByLevel(Level.ERROR).getMessage());
		assertEquals(3, customAppender.eventsByLevel(Level.ERROR).size());
		assertEquals(12, customAppender.size());
	}

	@Test
	public void testMDC() {
		MDC.put("k", "v");
		assertNotNull(MDC.get("k"));
		assertEquals("v", MDC.get("k"));

		MDC.remove("k");
		assertNull(MDC.get("k"));

		MDC.put("k1", "v1");
		assertEquals("v1", MDC.get("k1"));
		MDC.clear();
		assertNull(MDC.get("k1"));

		try {
			MDC.put(null, "x");
			Assert.fail("null keys are invalid");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testMDCContextMapValues() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ka", "va");
		map.put("kb", "vb");

		MDC.put("k", "v");
		assertEquals("v", MDC.get("k"));
		MDC.setContextMap(map);
		assertNull(MDC.get("k"));
		assertEquals("va", MDC.get("ka"));
		assertEquals("vb", MDC.get("kb"));
	}

	@Test
	public void testLevelledToString() {
		Logger log = LoggerFactory.getLogger("testLevelledToString");
		ClassA[] aArray = new ClassA[] { new ClassA(1, 2f, "a1"),
				new ClassA(2, 4f, "a2") };
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		calendar.set(1976,01,01,00,00,00);
		Set<ClassA> aSet = Sets.newLinkedHashSet(ArrayUtils.asList(aArray));
		ClassB bWithAArray = new ClassB(3,4,"SomeString","bWithAArray", calendar.getTime(), aArray);
		ClassC cWithASet = new ClassC("cWithASet  ", calendar.getTime(), aSet);
		log.info("{}",bWithAArray);
		Assert.assertEquals(bWithAArray.toString(LevelOfDetail.BRIEF),"[bWithAArray,[a=1],[a=2]]");
		Assert.assertEquals(customAppender.topEventByLevel(Level.INFO).getMessage(), "[bWithAArray,[a=1],[a=2]]");
		
		log.info("{}",cWithASet);
		Assert.assertEquals(cWithASet.toString(LevelOfDetail.BRIEF),"[cWithASet  ,Sun Feb 01 00:00:00 IST 1976]");
		Assert.assertEquals(customAppender.topEventByLevel(Level.INFO).getMessage(),"[cWithASet  ,Sun Feb 01 00:00:00 IST 1976]");
		
		log.debug("{}",bWithAArray);
		Assert.assertEquals(bWithAArray.toString(LevelOfDetail.MEDIUM),"[bWithAArray,Sun Feb 01 00:00:00 IST 1976,[a=1,b=2.0],[a=2,b=4.0]]");
		Assert.assertEquals(customAppender.topEventByLevel(Level.DEBUG).getMessage(),"[bWithAArray,Sun Feb 01 00:00:00 IST 1976,[a=1,b=2.0],[a=2,b=4.0]]");
		
		log.debug("{}",cWithASet);
		Assert.assertEquals(cWithASet.toString(LevelOfDetail.MEDIUM),"[cWithASet  ,Sun Feb 01 00:00:00 IST 1976,[a=1,b=2.0],[a=2,b=4.0]]");
		Assert.assertEquals(customAppender.topEventByLevel(Level.DEBUG).getMessage(),"[cWithASet  ,Sun Feb 01 00:00:00 IST 1976,[a=1,b=2.0],[a=2,b=4.0]]");
	}

	private static class ClassA implements LevelledToString {
		public ClassA(int a, float b, String k) {
			super();
			this.a = a;
			this.b = b;
			this.k = k;
		}

		int a;
		float b;
		String k;

		@Override
		public String toString(final LevelOfDetail level) {
			switch (level) {
			case BRIEF:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.briefStyle).append("a", a)
						.toString();
			case MEDIUM:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.mediumStyle).append("a", a)
						.append("b", b).toString();
			default:
				return toString();
			}
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("a", a).append("b", b)
					.append("k", k).toString();
		}

		public int getA() {
			return a;
		}

		public float getB() {
			return b;
		}

		public String getK() {
			return k;
		}
	}

	private static class ClassB extends ClassA implements LevelledToString {
		/**
		 * @param name
		 * @param date
		 * @param aArray
		 */
		public ClassB(int i, float j, String k,String name, Date date, ClassA[] aArray) {
			super(i,j,k);
			this.name = name;
			this.date = date;
			this.aArray = aArray;
		}

		String name;
		Date date;
		ClassA[] aArray;

		@Override
		public String toString(LevelOfDetail level) {
			switch (level) {
			case BRIEF:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.briefStyle).append("name", name)
						.append("aArray", aArray).toString();
			case MEDIUM:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.mediumStyle).append("name", name)
						.append("date", date).append("aArray", aArray)
						.toString();
			default:
				return toString();
			}
		}
	}

	private static class ClassC implements LevelledToString {
		/**
		 * @param name
		 * @param date
		 * @param aArray
		 */
		public ClassC(String name, Date date, Set<ClassA> aSet) {
			super();
			this.name = name;
			this.date = date;
			this.aSet = aSet;
		}

		String name;
		Date date;
		Set<ClassA> aSet;

		@Override
		public String toString(LevelOfDetail level) {
			switch (level) {
			case BRIEF:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.briefStyle).append("name", name)
						.append("date", date).toString();
			case MEDIUM:
				return new ToStringBuilder(this,
						ToLevelledStringStyle.mediumStyle).append("name", name)
						.append("date", date).append("aSet", aSet).toString();
			default:
				return toString();
			}
		}
	}

}