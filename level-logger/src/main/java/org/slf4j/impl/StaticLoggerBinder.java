package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import com.github.venkateshamurthy.util.logging.CustomFormattingLog4jLoggerFactory;

/**
 * A custom static logger binder that binds to a user defined {@link ILoggerFactory} such as
 * {@link CustomFormattingLog4jLoggerFactory}.
 */
public final class StaticLoggerBinder implements LoggerFactoryBinder {

   /**
    * Declare the version of the SLF4J API this implementation is compiled against. The value of
    * this field is usually modified with each release. Per SLF4J,
    * "To avoid constant folding by the compiler, this field must *not* be final".
    */
   public static String REQUESTED_API_VERSION = "1.6";

   /** Logger factory class. */
   private static final Class<? extends ILoggerFactory> loggerFactoryClass =
         CustomFormattingLog4jLoggerFactory.class;
   // Log4jLoggerFactory.class;

   /**
    * The unique instance of this class.
    */
   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

   /**
    * The ILoggerFactory instance returned by the {@link #getLoggerFactory} method should always be
    * the same object.
    */
   private final ILoggerFactory loggerFactory;

   /**
    * Instantiates a new static logger binder.
    */
   private StaticLoggerBinder() {
      try {
         loggerFactory = loggerFactoryClass.newInstance();
      } catch (IllegalAccessException | InstantiationException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Return the singleton of this class.
    *
    * @return the StaticLoggerBinder singleton
    */
   public static StaticLoggerBinder getSingleton() {
      return SINGLETON;
   }

   @Override
   public ILoggerFactory getLoggerFactory() {
      return loggerFactory;
   }

   @Override
   public String getLoggerFactoryClassStr() {
      return loggerFactoryClass.getName();
   }
}