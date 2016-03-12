package com.github.venkateshamurthy.util.logging;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import org.slf4j.helpers.MessageFormatter;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.github.venkateshamurthy.util.logging.IFormattingTuple.Slf4jFormattingTuple;


/**
 * A factory for creating IFormattingTuple objects. Please refer to
 * {@link IFormattingTupleFactory#Type} for examples.
 */
public interface IFormattingTupleFactory {

   /** The delim start. */
   char DELIM_START = '{';

   /** The delim stop. */
   char DELIM_STOP = '}';

   /** The delim str. */
   String DELIM_STR = "{}";

   /** The escape char. */
   char ESCAPE_CHAR = '\\';

   /**
    * Format method.
    *
    * @param messagePattern
    *           the message pattern
    * @param arg
    *           the arg
    * @return the IFormattingTuple
    */
   IFormattingTuple format(Slf4jLevel level, String messagePattern, Object arg);

   /**
    * Formatter.
    *
    * @param messagePattern
    *           the message pattern
    * @param arg1
    *           the arg1
    * @param arg2
    *           the arg2
    * @return the IFormattingTuple
    */
   IFormattingTuple format(Slf4jLevel level, String messagePattern, Object arg1, Object arg2);

   /**
    * Array formatter.
    *
    * @param messagePattern
    *           the message pattern
    * @param argArray
    *           the arg array
    * @return the IFormattingTuple
    */
   IFormattingTuple arrayFormat(Slf4jLevel level, final String messagePattern,
         final Object[] argArray);

   /**
    * An enum of predefined implementations of {@link IFormattingTupleFactory}.
    */
   enum Type implements IFormattingTupleFactory {
      /** Default SLF4J Message formatting tuple generator. */
      DEFAULT(new Slf4jMessageFormatter()),
      /** Custom Message Formatting Tuple generator. */
      CUSTOM(new CustomSlf4jMessageFormatter());

      /** The formatting tuple factory. */
      private final IFormattingTupleFactory tupleFactory;

      /**
       * Instantiates a new {@link IFormattingTupleFactory}.
       *
       * @param tupleFactory
       *           the tuple factory
       */
      Type(final IFormattingTupleFactory tupleFactory) {
         this.tupleFactory = tupleFactory;
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg) {
         return tupleFactory.format(level, messagePattern, arg);
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg1, final Object arg2) {
         return tupleFactory.format(level, messagePattern, arg1, arg2);
      }

      @Override
      public IFormattingTuple arrayFormat(final Slf4jLevel level, final String messagePattern,
            final Object[] argArray) {
         return tupleFactory.arrayFormat(level, messagePattern, argArray);
      }
   }

   /**
    * A Simple adapter of existing {@link MessageFormatter}.
    */
   static class Slf4jMessageFormatter implements IFormattingTupleFactory {
      private Slf4jMessageFormatter() {
         // Empty constructor.
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg) {
         return new Slf4jFormattingTuple(MessageFormatter.format(messagePattern, arg));
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg1, final Object arg2) {
         return new Slf4jFormattingTuple(MessageFormatter.format(messagePattern, arg1, arg2));
      }

      @Override
      public IFormattingTuple arrayFormat(final Slf4jLevel level, final String messagePattern,
            final Object[] argArray) {
         return new Slf4jFormattingTuple(MessageFormatter.arrayFormat(messagePattern, argArray));
      }
   }

   /** A static factory for specializing on custom requirement to print log level based print. */
   static class CustomSlf4jMessageFormatter implements IFormattingTupleFactory {
      /**
       * single escape of 1 character length. Here the escape char is
       * {@link IFormattingTupleFactory#ESCAPE_CHAR}.
       */
      private static final int SINGLE_ESCAPE = 1;
      /**
       * double escape of 2 characters length. Here the escape char is
       * {@link IFormattingTupleFactory#ESCAPE_CHAR}.
       */
      private static final int DOUBLE_ESCAPE = 2;

      /** The level to detail map. */
      private final EnumMap<Slf4jLevel, LevelOfDetail> levelToDetailMap =
            new EnumMap<Slf4jLevel, LevelOfDetail>(Slf4jLevel.class);

      /**
       * Instantiates a new custom slf4j message formatter.
       */
      private CustomSlf4jMessageFormatter() {
         levelToDetailMap.put(Slf4jLevel.TRACE, LevelOfDetail.ALL);
         levelToDetailMap.put(Slf4jLevel.DEBUG, LevelOfDetail.MEDIUM);
         levelToDetailMap.put(Slf4jLevel.INFO, LevelOfDetail.BRIEF);
         levelToDetailMap.put(Slf4jLevel.ERROR, LevelOfDetail.ALL);
         levelToDetailMap.put(Slf4jLevel.WARN, LevelOfDetail.ALL);
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg) {
         return arrayFormat(level, messagePattern, new Object[] { arg });
      }

      @Override
      public IFormattingTuple format(final Slf4jLevel level, final String messagePattern,
            final Object arg1, final Object arg2) {
         return arrayFormat(level, messagePattern, new Object[] { arg1, arg2 });
      }

      @Override
      public IFormattingTuple arrayFormat(final Slf4jLevel level, final String pattern,
            final Object[] argArray) {

         Throwable throwableCandidate = getThrowableCandidate(argArray);

         if (pattern == null || argArray == null) {
            return new Slf4jFormattingTuple(pattern, argArray, throwableCandidate);
         }

         int charAt = 0;
         StringBuilder sbuf = new StringBuilder(pattern.length() + 50);
         int argIndex;
         for (argIndex = 0; argIndex < argArray.length; argIndex++) {
            final int delimiterAt = pattern.indexOf(DELIM_STR, charAt);
            if (delimiterAt == -1) {
               // No delimiters in pattern so just add up rest of the part of the pattern.
               String msgString =
                     charAt == 0 ? pattern : sbuf.append(pattern.substring(charAt)).toString();
               return new Slf4jFormattingTuple(msgString, argArray, throwableCandidate);
            } else {
               // if you just see one escape such as say in log.debug("Hello \\{}","world")
               if (isEscapedDelimiter(pattern, delimiterAt, SINGLE_ESCAPE)) {
                  // then pattern to pick up : "Hello " ==> take till one left of escape char \\
                  sbuf.append(pattern.substring(charAt, delimiterAt - 1));
                  // Advance the next charAt pointer beyond { character
                  charAt = delimiterAt + 1;
                  // check if double escaped
                  if (isEscapedDelimiter(pattern, delimiterAt, DOUBLE_ESCAPE)) {
                     // Append parameter that may be nested.
                     appendParameter(level, sbuf, argArray[argIndex], new HashSet());
                     // Advance the next charAt pointer beyond {} characters
                     charAt = delimiterAt + 2;
                  } else {
                     argIndex--;
                     sbuf.append(DELIM_START);
                  }
               } else {
                  sbuf.append(pattern.substring(charAt, delimiterAt));
                  appendParameter(level, sbuf, argArray[argIndex], new HashSet());
                  charAt = delimiterAt + 2;
               }
            }
         }
         // append the characters following the last {} pair.
         sbuf.append(pattern.substring(charAt, pattern.length()));
         if (argIndex < argArray.length - 1) {
            throwableCandidate = null;
         }
         return new Slf4jFormattingTuple(sbuf.toString(), argArray, throwableCandidate);
      }

      /**
       * Check if preceding character (depending on howManyEscapes) is an
       * {@link IFormattingTupleFactory#ESCAPE_CHAR}.
       */
      private static boolean isEscapedDelimiter(final String messagePattern,
            final int delimeterStartIndex, final int howManyEscapes) {
         return delimeterStartIndex >= howManyEscapes &&
               messagePattern.charAt(delimeterStartIndex - howManyEscapes) == ESCAPE_CHAR;
      }

      /**
       * Gets any throwable instance holed up in the last element of the argument array.
       *
       * @param argArray
       *           the arg array
       * @return the throwable candidate holed up in the last element of the array.
       */
      private static Throwable getThrowableCandidate(final Object[] argArray) {
         if (ArrayUtils.isNotEmpty(argArray)
               && argArray[argArray.length - 1] instanceof Throwable) {
            return (Throwable) argArray[argArray.length - 1];
         }
         return null;
      }

      private static void primitiveArrayAppend(final StringBuilder sbuf, final Object[] array) {
         sbuf.append('[').append(StringUtils.arrayToCommaDelimitedString(array)).append(']');
      }

      private void objectAppend(final Slf4jLevel level, final StringBuilder sbuf,
            final Object object) {
         try {
            sbuf.append(getObjectAsString(level, object));
         } catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" +
                  object.getClass().getName() + "]");
            t.printStackTrace();
            sbuf.append("[FAILED toString()]");
         }
      }

      private void appendParameter(final Slf4jLevel level, final StringBuilder sbuf,
            final Object object, final Set seenSet) {
         if (object == null) {
            sbuf.append("null");
            return;
         }
         if (!object.getClass().isArray()) {
            objectAppend(level, sbuf, object);
         } else {
            if (object instanceof Object[]) {
               objectArrayAppend(level, sbuf, (Object[]) object, seenSet);
            } else {
               primitiveArrayAppend(sbuf, ObjectUtils.toObjectArray(object));
            }
         }
      }

      private void objectArrayAppend(final Slf4jLevel level, final StringBuilder sbuf,
            final Object[] objectArray, final Set seenSet) {
         sbuf.append('[');
         if (!seenSet.contains(objectArray)) {
            seenSet.add(objectArray);
            for (int i = 0; i < objectArray.length; i++) {
               appendParameter(level, sbuf, objectArray[i], seenSet);
               if (i != objectArray.length - 1) {
                  sbuf.append(", ");
               }
            }
            seenSet.remove(objectArray);
         } else {
            sbuf.append("...");
         }
         sbuf.append(']');
      }

      protected String getObjectAsString(final Slf4jLevel slf4jlevel, final Object object) {
         LevelOfDetail levelOfDetail = levelToDetailMap.get(slf4jlevel);
         return object instanceof LevelledToString ? ((LevelledToString) object)
               .toString(levelOfDetail) : object.toString();
      }
   }
}