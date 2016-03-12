package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import org.eclipse.xtext.xbase.lib.Conversions;

/**
 * A simple utility to deal with group of {@link LevelledToString} objects.
 */
@SuppressWarnings("all")
public class ToLevelledStringUtil {
  /**
   * Get level based printing on array of {@link LevelledToString} objects.
   * @param objects are collection/array of {@link LevelledToString}
   * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
   * @return String form of level based attributes
   */
  public static String toString(final LevelledToString[] objects, final LevelOfDetail level) {
    final boolean nonNullObjects = (!Objects.equal(objects, null));
    final boolean nonNullLevel = (!Objects.equal(level, null));
    Preconditions.checkArgument((nonNullObjects && nonNullLevel), 
      "The ToDetailedString[] objects or the level cannot be null/empty");
    final StringBuilder pattern = new StringBuilder();
    final int objectsLength = objects.length;
    final String[] fieldArray = new String[objectsLength];
    for (int i = 0; (i < objectsLength); i++) {
      {
        pattern.append("%s ");
        final LevelledToString object = objects[i];
        final boolean nonNullObject = (!Objects.equal(object, null));
        Preconditions.checkArgument(nonNullObject, "The elements in  object array cannot be null");
        final String objectStringValue = object.toString(level);
        fieldArray[i] = objectStringValue;
      }
    }
    String _string = pattern.toString();
    return String.format(_string, fieldArray);
  }
  
  /**
   * Get level based printing on array of {@link LevelledToString} objects.
   * @param objects are collection/array of {@link LevelledToString}
   * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
   * @return String form of level based attributes
   */
  public static String toString(final Collection<LevelledToString> objects, final LevelOfDetail level) {
    final boolean nonNullObjects = (!Objects.equal(objects, null));
    final boolean nonNullLevel = (!Objects.equal(level, null));
    Preconditions.checkArgument((nonNullObjects && nonNullLevel), 
      "The Collection<LevelledToString> objects or level cannot be null/empty");
    final StringBuilder pattern = new StringBuilder();
    final int objectsLength = ((Object[])Conversions.unwrapArray(objects, Object.class)).length;
    final String[] fieldArray = new String[objectsLength];
    for (int i = 0; (i < objectsLength); i++) {
      {
        pattern.append("%s ");
        final LevelledToString object = ((LevelledToString[])Conversions.unwrapArray(objects, LevelledToString.class))[i];
        final boolean nonNullObject = (!Objects.equal(object, null));
        Preconditions.checkArgument(nonNullObject, "The elements in object collection cannot be null");
        final String objectStringValue = object.toString(level);
        fieldArray[i] = objectStringValue;
      }
    }
    String _string = pattern.toString();
    return String.format(_string, fieldArray);
  }
}
