package com.github.venkateshamurthy.util.logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


@Test(groups="common")
public class TestSample {

	private static final String CONFIG_FILE_KEY = "log4j.configuration";
	private final CustomAppender customAppender = new CustomAppender();
	org.apache.log4j.Logger root;

	@BeforeMethod
	public void setUp() throws Exception {
		System.setProperty(CONFIG_FILE_KEY,	"log4j.properties");
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

	@Test public void testSample(){
		Logger log=LoggerFactory.getLogger(Sample.class);
	    Employee a = new Employee(new Date(70, 6, 24), "Aemployee", 2500000d);
	    Employee b = new Employee(new Date(75, 9, 20), "Bemployee", 2500000d);
	    Employer boss = new Employer(new Date(60, 3, 6),"BigBoss", 25000000d);
	    boss.setBudget(100000d);
	    Employer m = new Employer(new Date(65, 1, 1),"Manager", 5000000d);
	    m.setBudget(10000d);
	    boss.addReportee(m);
	    m.addReportee(a);
	    m.addReportee(b);
	    a.setBoss(m.getName());
	    b.setBoss(m.getName());
	    m.setBoss(boss.getName());
	    log.debug("Boss->{}",boss);
        String bossMsgToStringStyle="Boss->[[BigBoss,Wed Apr 06 00:00:00 IST 1960,salary=2.5E7,<null>],budget=100000.0,"
        		+ "[[Manager,Mon Feb 01 00:00:00 IST 1965,salary=5000000.0,BigBoss],budget=10000.0,"
        		+ "[Aemployee,Fri Jul 24 00:00:00 IST 1970,salary=2500000.0,Manager],"
        		+ "[Bemployee,Mon Oct 20 00:00:00 IST 1975,salary=2500000.0,Manager]]]";
        Assert.assertEquals(customAppender.eventsByLevel(Level.DEBUG).size(),1);
		Assert.assertEquals(customAppender.topEventByLevel(Level.DEBUG).getMessage(), bossMsgToStringStyle);
		log.debug("Manager->{}",m);
		String mgrMsgWithStyle="Manager->[[Manager,Mon Feb 01 00:00:00 IST 1965,salary=5000000.0,BigBoss],budget=10000.0,"
				+ "[Aemployee,Fri Jul 24 00:00:00 IST 1970,salary=2500000.0,Manager],"
				+ "[Bemployee,Mon Oct 20 00:00:00 IST 1975,salary=2500000.0,Manager]]";
		Assert.assertEquals(customAppender.eventsByLevel(Level.DEBUG).size(),2);
		Assert.assertEquals(customAppender.topEventByLevel(Level.DEBUG).getMessage(), mgrMsgWithStyle);

	}
	
	private static class CustomAppender extends AppenderSkeleton {

		   /** The map. */
		   private final Map<Level, List<LoggingEvent>> map = new LinkedHashMap<Level, List<LoggingEvent>>(){
			   {
				   put(Level.ALL,new ArrayList<LoggingEvent>());
				   put(Level.TRACE,new ArrayList<LoggingEvent>());
				   put(Level.DEBUG,new ArrayList<LoggingEvent>());
				   put(Level.INFO,new ArrayList<LoggingEvent>());
				   put(Level.WARN,new ArrayList<LoggingEvent>());
				   put(Level.ERROR,new ArrayList<LoggingEvent>());
				   put(Level.OFF,new ArrayList<LoggingEvent>());
				   put(Level.FATAL,new ArrayList<LoggingEvent>());
			   }
		   };
		   private final List<LoggingEvent> list = new ArrayList<>();
		   /** The extract location info. */
		   public boolean extractLocationInfo = false;

		   /*
		    * (non-Javadoc)
		    * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
		    */
		   @Override
		   protected void append(LoggingEvent event) {
		      list.add(event);
		      if (!map.containsKey(event.getLevel()))
		         map.put(event.getLevel(), new ArrayList<LoggingEvent>());

		      map.get(event.getLevel()).add(event);

		      if (extractLocationInfo) {
		         event.getLocationInformation();
		      }
		   }

		   public Map<Level, List<LoggingEvent>> getMap() {
		      return map;
		   }

		   public int size() {
		      return list.size();
		   }

		   public int countEventsByLevel(Level level) {
		      return eventsByLevel(level).size();
		   }

		   public List<LoggingEvent> eventsByLevel(Level level) {
		      return map.get(level);
		   }

		   public LoggingEvent topEventByLevel(Level level) {
		      List<LoggingEvent> list = eventsByLevel(level);
		      return list.get(list.size() - 1);
		   }
		   /*
		    * (non-Javadoc)
		    * @see org.apache.log4j.AppenderSkeleton#close()
		    */
		   @Override
		   public void close() {
		      list.clear();
		      map.clear();
		   }

		   /*
		    * (non-Javadoc)
		    * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
		    */
		   @Override
		   public boolean requiresLayout() {
		      return false;
		   }

		   /**
		    * @return the list
		    */
		   public List<LoggingEvent> getList() {
		      return list;
		   }

		}
}
