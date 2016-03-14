package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;

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
  public static <T extends Collection<? extends LevelledToString>> String toString(final T objects, final LevelOfDetail level) {
    final boolean nonNullObjects = (!Objects.equal(objects, null));
    final boolean nonNullLevel = (!Objects.equal(level, null));
    Preconditions.checkArgument((nonNullObjects && nonNullLevel), 
      "The Collection<LevelledToString> objects or level cannot be null/empty");
    final StringBuilder pattern = new StringBuilder();
    final int objectsLength = objects.size();
    final String[] fieldArray = new String[objectsLength];
    int i = 0;
    for (final LevelledToString lToS : objects) {
      {
        pattern.append("%s ");
        final LevelledToString object = lToS;
        final boolean nonNullObject = (!Objects.equal(object, null));
        Preconditions.checkArgument(nonNullObject, "The elements in object collection cannot be null");
        final String objectStringValue = object.toString(level);
        int _plusPlus = i++;
        fieldArray[_plusPlus] = objectStringValue;
      }
    }
    String _string = pattern.toString();
    return String.format(_string, fieldArray);
  }
}
