package com.github.venkateshamurthy.util.logging;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.util.ObjectUtils;

/**
 * The Class CustomFormattingLog4jAdapter allows a custom {@link IFormattingTupleFactory formatting
 * tuple factory} to be used to provide a customized string representation in Log statements.
 */
public class CustomFormattingLog4jLoggerAdapter extends AbstractLog4jLoggerAdapter {

   /** Serial Id. */
   private static final long serialVersionUID = -3281490149895615451L;
   /** The formatting tuple factory. */
   private final IFormattingTupleFactory tupleFactory;

   /**
    * Instantiates a new log4j logger adapter.<br>
    * WARN: constructor should have only package access so that only
    * {@link CustomFormattingLog4jLoggerFactory} be able to create one.
    *
    * @param logger
    *           the log4j logger
    */

   public CustomFormattingLog4jLoggerAdapter(final org.apache.log4j.Logger logger,
         final IFormattingTupleFactory tupleFactory) {
      super(logger);
      this.tupleFactory = tupleFactory;
   }

   /**
    * Constructor.
    *
    * @param name
    *           of the logger.
    * @param tupleFactory
    *           an instance of {@link FormattingTuleFactory}
    */
   public CustomFormattingLog4jLoggerAdapter(final String name,
         final IFormattingTupleFactory tupleFactory) {
      this(Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(name) ? LogManager.getRootLogger() : LogManager
            .getLogger(name), tupleFactory);
   }

   @Override
   protected final void doLog(final Slf4jLevel slf4jLevel, final String format,
         final Object... args) {
      Level level = slf4jLevel.getLog4jLevel();
      if (logger.isEnabledFor(level)) {
         Level effectiveLevel = level == Level.TRACE ? effectiveTraceLevel() : level;
         IFormattingTuple ft;
         if (args != null && args.length == 1) {
            ft =
                  args[0]!=null && args[0].getClass().isArray() ? tupleFactory.arrayFormat(slf4jLevel, format,
                        ObjectUtils.toObjectArray(args[0])) : tupleFactory.format(slf4jLevel,
                        format, args[0]);
         } else if (args != null && args.length == 2) {
            ft = tupleFactory.format(slf4jLevel, format, args[0], args[1]);
         } else {
            ft = tupleFactory.arrayFormat(slf4jLevel, format, args);
         }
         logger.log(FQCN, effectiveLevel, ft.getMessage(), ft.getThrowable());
      }
   }
}
