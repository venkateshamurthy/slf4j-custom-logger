package com.github.venkateshamurthy.util.logging;

/**
 * The Interface LevelledToString provides a simple interface to allow objects to pick attributes of
 * interest at each {@link LevelOfDetail} for representing the string form of the object.
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