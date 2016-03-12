package com.github.venkateshamurthy.util.tostring.examples;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The Class CustomAppender.
 */
public class CustomAppender extends AppenderSkeleton {

   /** The map. */
   private final Map<Level, List<LoggingEvent>> map = new LinkedHashMap<>();
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