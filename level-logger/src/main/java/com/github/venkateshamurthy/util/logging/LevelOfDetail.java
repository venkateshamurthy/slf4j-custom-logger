package com.github.venkateshamurthy.util.logging;

/**
 * The Enum LevelOfDetail to indicate different levels of details from the object to be displayed.It
 * basically helps the objects to choose the set of attributes for formulating string
 * representation. This is used in conjunction with {@link LevelledToString#toString(LevelOfDetail)}
 * method.
 */
public enum LevelOfDetail {

   /**
    * The none level where an object can choose not to emit anything in the
    * {@link LevelledToString#toString(LevelOfDetail)} method.
    */
   NONE,

   /**
    * The brief level where an object can choose to emit a very brief/terse message from the object
    * (making use of few key attributes) in the {@link LevelledToString#toString(LevelOfDetail)}
    * method.
    */
   BRIEF,

   /**
    * The medium level where an object can choose to emit a good amount of detailed message from the
    * object (making use of key as-well-as few other attributes) in the
    * {@link LevelledToString#toString(LevelOfDetail)} method.
    */
   MEDIUM,

   /** The all level can be used to emit all the attribute information. */
   ALL,
}
