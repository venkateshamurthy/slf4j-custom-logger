package com.github.venkateshamurthy.util.logging;

/**
 * The Interface LevelledToString provides a simple interface to allow objects to pick attributes of
 * interest at each {@link LevelOfDetail} for representing the string form of the object.
 * <p>
 * An example class could be:
 *
 * <pre>
 * import org.apache.commons.lang3.builder.ToStringBuilder;
 *
 * class ClassA implements LevelledToString {
 *    int a;
 *    float b;
 *    String k;
 *
 *    &#064;Override
 *    public String toString(final LevelOfDetail level) {
 *       switch (level) {
 *       case BRIEF:
 *          return new ToStringBuilder(this).append(&quot;a&quot;, a).toString();
 *       case MEDIUM:
 *          return new ToStringBuilder(this).append(&quot;a&quot;, a).append(&quot;b&quot;, b)
 *             .toString();
 *       default:
 *          return toString();
 *       }
 *    }
 *
 *    &#064;Override
 *    public String toString() {
 *       return new ToStringBuilder(this).append(&quot;a&quot;, a).append(&quot;b&quot;, b)
 *          .append(&quot;k&quot;, k).toString();
 *    }
 * }
 * </pre>
 */
public interface LevelledToString {

   /**
    * To string method that uses a passed LevelOfDetail and makes strings out of level corresponding
    * attributes.
    *
    * @param level
    *           the {@link LevelOfDetail level}
    * @return the string
    */
   String toString(LevelOfDetail level);
}