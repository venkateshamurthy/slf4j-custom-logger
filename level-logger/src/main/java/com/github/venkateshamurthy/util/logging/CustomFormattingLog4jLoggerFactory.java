package com.github.venkateshamurthy.util.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.helpers.LogLog;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * A factory for creating {@link CustomFormattingLog4jLoggerAdapter} objects. This requires a
 * pre-set {@link IFormattingTupleFactory} that can create {@link IFormattingTuple} for customized
 * levels of details in string representation of objects to be logged.
 */
public class CustomFormattingLog4jLoggerFactory implements ILoggerFactory {

   /**
    * A Map of name of the logger to a corresponding {@link CustomFormattingLog4jLoggerAdapter}.
    */
   private final Map<String, Logger> loggerMap;

   /** The tuple factory which could be set for testing. */
   private IFormattingTupleFactory tupleFactory;
   /**
    * Instantiates a new spbm log4j logger factory.
    */
   public CustomFormattingLog4jLoggerFactory() {
      loggerMap = new HashMap<>();
      setTupleFactory(IFormattingTupleFactory.Type.CUSTOM);
   }

   /*
    * (non-Javadoc)
    * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
    */
   @Override
   public final Logger getLogger(final String name) {
      synchronized (loggerMap) {
         if (!loggerMap.containsKey(name)) {
            loggerMap.put(name, new CustomFormattingLog4jLoggerAdapter(name, tupleFactory));
            LogLog.debug(String.format("Creating logger of the name %s\n", name));
         }
      }
      return loggerMap.get(name);
   }

   /**
    * Get FormattingTupleFactory.
    *
    * @return the tupleFactory
    */
   public final IFormattingTupleFactory getTupleFactory() {
      return tupleFactory;
   }

   /**
    * Set FormattingTupleFactory.
    *
    * @param tupleFactory
    *           the tupleFactory to set
    */
   public final void setTupleFactory(final IFormattingTupleFactory tupleFactory) {
      Preconditions.checkNotNull(tupleFactory, "FormattingTupleFactory '%s' passed cannot be null",
            "tupleFactory");
      this.tupleFactory = tupleFactory;
   }
}
