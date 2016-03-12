package com.github.venkateshamurthy.util.logging;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.slf4j.Marker;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.spi.LocationAwareLogger;

/**
 * The Class AbstractLog4jLoggerAdapter provides base abstraction to common Log4j specific logger
 * methods(basically the boiler plate). The Derived classes can now focus on specialization such as
 * incorporating {@link LevelledToString} etc while logging.
 *
 * @see CustomFormattingLog4jLoggerAdapter
 */
abstract class AbstractLog4jLoggerAdapter extends MarkerIgnoringBase implements
      LocationAwareLogger, Serializable {

   /** Serial id. */
   protected static final long serialVersionUID = 6182834493563598289L;
   /** Following the pattern discussed in pages 162 through 168 of "The complete log4j manual". */
   protected final String FQCN = getClass().getName();
   /** The log4j core logger. */
   protected final transient org.apache.log4j.Logger logger;
   /** The trace level was introduced in log4j 1.2.12. Hence a flag to find its trace capable. */
   protected final boolean traceCapable;

   /**
    * Constructor for a new log4j logger adapter.
    *
    * @param logger
    *           the logger
    */
   AbstractLog4jLoggerAdapter(final org.apache.log4j.Logger logger) {
      this.logger = logger;
      this.name = logger.getName();
      traceCapable = isTraceCapable();
   }

   /**
    * Is this logger instance enabled for the TRACE level?
    *
    * @return True if this Logger is enabled for level TRACE, false otherwise.
    */
   @Override
   public boolean isTraceEnabled() {
      return traceCapable ? logger.isTraceEnabled() : logger.isDebugEnabled();
   }

   /**
    * Log a message object at level TRACE.
    *
    * @param msg
    *           - the message object to be logged
    */
   @Override
   public void trace(final String msg) {
      logger.log(FQCN, effectiveTraceLevel(), msg, null);
   }

   /**
    * Log a message at level TRACE according to the specified format and argument.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for level TRACE.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg
    *           the argument
    */
   @Override
   public void trace(final String format, final Object arg) {
      doLog(Slf4jLevel.TRACE, format, arg);
   }

   /**
    * Log a message at level TRACE according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg1
    *           the first argument
    * @param arg2
    *           the second argument
    */
   @Override
   public void trace(final String format, final Object arg1, final Object arg2) {
      doLog(Slf4jLevel.TRACE, format, arg1, arg2);
   }

   /**
    * Log a message at level TRACE according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the TRACE level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arguments
    *           an array of arguments
    */
   @Override
   public void trace(final String format, final Object... arguments) {
      doLog(Slf4jLevel.TRACE, format, arguments);
   }

   /**
    * Log an exception (throwable) at level TRACE with an accompanying message.
    *
    * @param msg
    *           the message accompanying the exception
    * @param throwable
    *           the exception (throwable) to log
    */
   @Override
   public void trace(final String msg, final Throwable throwable) {
      logger.log(FQCN, effectiveTraceLevel(), msg, throwable);
   }

   /**
    * Is this logger instance enabled for the DEBUG level?
    *
    * @return True if this Logger is enabled for level DEBUG, false otherwise.
    */
   @Override
   public boolean isDebugEnabled() {
      return logger.isDebugEnabled();
   }

   /**
    * Log a message object at level DEBUG.
    *
    * @param msg
    *           - the message object to be logged
    */
   @Override
   public void debug(final String msg) {
      logger.log(FQCN, Level.DEBUG, msg, null);
   }

   /**
    * Log a message at level DEBUG according to the specified format and argument.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for level DEBUG.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg
    *           the argument
    */
   @Override
   public void debug(final String format, final Object arg) {
      doLog(Slf4jLevel.DEBUG, format, arg);
   }

   /**
    * Log a message at level DEBUG according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg1
    *           the first argument
    * @param arg2
    *           the second argument
    */
   @Override
   public void debug(final String format, final Object arg1, final Object arg2) {
      doLog(Slf4jLevel.DEBUG, format, arg1, arg2);
   }

   /**
    * Log a message at level DEBUG according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the DEBUG level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arguments
    *           an array of arguments
    */
   @Override
   public void debug(final String format, final Object... arguments) {
      doLog(Slf4jLevel.DEBUG, format, arguments);
   }

   /**
    * Log an exception (throwable) at level DEBUG with an accompanying message.
    *
    * @param msg
    *           the message accompanying the exception
    * @param throwable
    *           the exception (throwable) to log
    */
   @Override
   public void debug(final String msg, final Throwable throwable) {
      logger.log(FQCN, Level.DEBUG, msg, throwable);
   }

   /**
    * Is this logger instance enabled for the INFO level?
    *
    * @return True if this Logger is enabled for the INFO level, false otherwise.
    */
   @Override
   public boolean isInfoEnabled() {
      return logger.isInfoEnabled();
   }

   /**
    * Log a message object at the INFO level.
    *
    * @param msg
    *           - the message object to be logged
    */
   @Override
   public void info(final String msg) {
      logger.log(FQCN, Level.INFO, msg, null);
   }

   /**
    * Log a message at level INFO according to the specified format and argument.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the INFO level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg
    *           the argument
    */
   @Override
   public void info(final String format, final Object arg) {
      doLog(Slf4jLevel.INFO, format, arg);
   }

   /**
    * Log a message at the INFO level according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the INFO level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg1
    *           the first argument
    * @param arg2
    *           the second argument
    */
   @Override
   public void info(final String format, final Object arg1, final Object arg2) {
      doLog(Slf4jLevel.INFO, format, arg1, arg2);
   }

   /**
    * Log a message at level INFO according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the INFO level.
    * </p>
    *
    * @param format
    *           the format string
    * @param argArray
    *           an array of arguments
    */
   @Override
   public void info(final String format, final Object... argArray) {
      doLog(Slf4jLevel.INFO, format, argArray);
   }

   /**
    * Log an exception (throwable) at the INFO level with an accompanying message.
    *
    * @param msg
    *           the message accompanying the exception
    * @param throwable
    *           the exception (throwable) to log
    */
   @Override
   public void info(final String msg, final Throwable throwable) {
      logger.log(FQCN, Level.INFO, msg, throwable);
   }

   /**
    * Is this logger instance enabled for the WARN level?
    *
    * @return True if this Logger is enabled for the WARN level, false otherwise.
    */
   @Override
   public boolean isWarnEnabled() {
      return logger.isEnabledFor(Level.WARN);
   }

   /**
    * Log a message object at the WARN level.
    *
    * @param msg
    *           - the message object to be logged
    */
   @Override
   public void warn(final String msg) {
      logger.log(FQCN, Level.WARN, msg, null);
   }

   /**
    * Log a message at the WARN level according to the specified format and argument.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the WARN level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg
    *           the argument
    */
   @Override
   public void warn(final String format, final Object arg) {
      doLog(Slf4jLevel.WARN, format, arg);
   }

   /**
    * Log a message at the WARN level according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the WARN level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg1
    *           the first argument
    * @param arg2
    *           the second argument
    */
   @Override
   public void warn(final String format, final Object arg1, final Object arg2) {
      doLog(Slf4jLevel.WARN, format, arg1, arg2);
   }

   /**
    * Log a message at level WARN according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the WARN level.
    * </p>
    *
    * @param format
    *           the format string
    * @param argArray
    *           an array of arguments
    */
   @Override
   public void warn(final String format, final Object... argArray) {
      doLog(Slf4jLevel.WARN, format, argArray);
   }

   /**
    * Log an exception (throwable) at the WARN level with an accompanying message.
    *
    * @param msg
    *           the message accompanying the exception
    * @param throwable
    *           the exception (throwable) to log
    */
   @Override
   public void warn(final String msg, final Throwable throwable) {
      logger.log(FQCN, Level.WARN, msg, throwable);
   }

   /**
    * Is this logger instance enabled for level ERROR?
    *
    * @return True if this Logger is enabled for level ERROR, false otherwise.
    */
   @Override
   public boolean isErrorEnabled() {
      return logger.isEnabledFor(Level.ERROR);
   }

   /**
    * Log a message object at the ERROR level.
    *
    * @param msg
    *           - the message object to be logged
    */
   @Override
   public void error(final String msg) {
      logger.log(FQCN, Level.ERROR, msg, null);
   }

   /**
    * Log a message at the ERROR level according to the specified format and argument.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the ERROR level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg
    *           the argument
    */
   @Override
   public void error(final String format, final Object arg) {
      doLog(Slf4jLevel.ERROR, format, arg);
   }

   /**
    * Log a message at the ERROR level according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the ERROR level.
    * </p>
    *
    * @param format
    *           the format string
    * @param arg1
    *           the first argument
    * @param arg2
    *           the second argument
    */
   @Override
   public void error(final String format, final Object arg1, final Object arg2) {
      doLog(Slf4jLevel.ERROR, format, arg1, arg2);
   }

   /**
    * Log a message at level ERROR according to the specified format and arguments.
    * <p>
    * This form avoids superfluous object creation when the logger is disabled for the ERROR level.
    * </p>
    *
    * @param format
    *           the format string
    * @param argArray
    *           an array of arguments
    */
   @Override
   public void error(final String format, final Object... argArray) {
      doLog(Slf4jLevel.ERROR, format, argArray);
   }

   /**
    * Log an exception (throwable) at the ERROR level with an accompanying message.
    *
    * @param msg
    *           the message accompanying the exception
    * @param throwable
    *           the exception (throwable) to log
    */
   @Override
   public void error(final String msg, final Throwable throwable) {
      logger.log(FQCN, Level.ERROR, msg, throwable);
   }

   /**
    * The core Logging {@inheritDoc}.
    */
   @Override
   public void log(final Marker marker, final String callerFqcn,
         final int locationAwareLoggerLevel, final String msg, final Object[] argArray,
         final Throwable throwable) {
      Level log4jLevel =
            locationAwareLoggerLevel == LocationAwareLogger.TRACE_INT ? effectiveTraceLevel()
                  : Slf4jLevel.getLog4jLevel(locationAwareLoggerLevel);
      logger.log(callerFqcn, log4jLevel, msg, throwable);
   }

   /**
    * Get an effective trace level based on {@link #traceCapable}.
    *
    * @return either of {@link Level#TRACE} or {@link Level#DEBUG}
    */
   protected Level effectiveTraceLevel() {
      return traceCapable ? Level.TRACE : Level.DEBUG;
   }

   /**
    * Checks if underlying logger has trace level logging enabled.
    *
    * @return true, if is trace capable
    */
   protected boolean isTraceCapable() {
      try {
         logger.isTraceEnabled();
         return true;
      } catch (NoSuchMethodError e) {
         return false;
      }
   }

   /**
    * This method does Level enabled formatting and logging.
    *
    * @param slf4jLevel
    *           the {@link Slf4jLevel} passed
    * @param format
    *           message format in the standard slf4j marker format {}
    * @param args
    *           to be passed to the logging
    */
   protected abstract void doLog(final Slf4jLevel slf4jLevel, final String format,
         final Object... args);
}
