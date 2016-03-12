package com.github.venkateshamurthy.util.logging;

import org.apache.log4j.Level;
import org.slf4j.spi.LocationAwareLogger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

// TODO: Auto-generated Javadoc
/**
 * The Enum  on Slf4j log Level that maps LocationAwareLogger to a log4j level.
 */
enum Slf4jLevel {
   /** ERROR level.*/
   ERROR(LocationAwareLogger.ERROR_INT, Level.ERROR),
   /** WARN level.*/
   WARN(LocationAwareLogger.WARN_INT, Level.WARN),
   /** INFo level.*/
   INFO(LocationAwareLogger.INFO_INT, Level.INFO),
   /** The DEBUG level. */
   DEBUG(LocationAwareLogger.DEBUG_INT, Level.DEBUG),
   /** The trace. */
   TRACE(LocationAwareLogger.TRACE_INT, Level.TRACE);

   /** The bi map. */
   private static final BiMap<Integer, Level> biMap = HashBiMap.create(values().length);
   static {
      for (Slf4jLevel slf4jLevel : values()) {
         biMap.put(slf4jLevel.locationAwareLoggerLevel, slf4jLevel.log4jLevel);
      }
   }

   /** The location aware logger level. */
   private final int locationAwareLoggerLevel;

   /** The log4j level. */
   private final Level log4jLevel;


   /**
    * Constructor.
    *
    * @param intLevel
    *           a @{link LocationAwareLogger} level
    * @param level
    *           a Log4j Level
    */
   Slf4jLevel(final int intLevel, final Level level) {
      locationAwareLoggerLevel = intLevel;
      log4jLevel = level;

   }

   /**
    * Gets the location aware logger level.
    *
    * @return the locationAwareLoggerLevel
    */
   public int getLocationAwareLoggerLevel() {
      return locationAwareLoggerLevel;
   }

   /**
    * Gets the log4j level.
    *
    * @return the log4jLevel
    */
   public Level getLog4jLevel() {
      return log4jLevel;
   }

   /**
    * Gets the log4j level.
    *
    * @param locationAwareLoggerLevel
    *           the location aware logger level
    * @return the log4j level
    */
   public static Level getLog4jLevel(final int locationAwareLoggerLevel) {
      Level log4jLevel = biMap.get(locationAwareLoggerLevel);
      if (log4jLevel != null) {
         return log4jLevel;
      }
      throw new IllegalArgumentException(String.format(
            "Unsupported LocationAwareLogger %s level passed.", locationAwareLoggerLevel));
   }
}