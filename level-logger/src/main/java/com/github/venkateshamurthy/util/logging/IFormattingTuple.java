package com.github.venkateshamurthy.util.logging;

import org.slf4j.helpers.FormattingTuple;

/**
 * The Interface IFormattingTuple for containing/holding the data after the formatting.
 */
public interface IFormattingTuple {
   /**
    * Gets the message.
    *
    * @return the message
    */
   String getMessage();

   /**
    * Gets the arg array.
    *
    * @return the arg array
    */
   Object[] getArgArray();

   /**
    * Gets the throwable.
    *
    * @return the throwable
    */
   Throwable getThrowable();

   /**
    * The Class Slf4jFormattingTuple is just an adapter of the containing interface
    * {@link IFormattingTuple} for the adaptee {@link FormattingTuple} since it is not bound to an
    * interface.
    */
   class Slf4jFormattingTuple implements IFormattingTuple {

      /** The formatting tuple. */
      private final FormattingTuple ft;

      /**
       * Constructor.
       *
       * @param ft
       *           the {@link FormattingTuple}
       */
      Slf4jFormattingTuple(final FormattingTuple ft) {
         this.ft = ft;
      }

      public Slf4jFormattingTuple(final String message) {
         this(new FormattingTuple(message));
      }

      public Slf4jFormattingTuple(final String message, final Object[] argArray,
            final Throwable throwable) {
         this(new FormattingTuple(message, argArray, throwable));
      }

      @Override
      public String getMessage() {
         return ft.getMessage();
      }

      @Override
      public Object[] getArgArray() {
         return ft.getArgArray();
      }

      @Override
      public Throwable getThrowable() {
         return ft.getThrowable();
      }
   }
}