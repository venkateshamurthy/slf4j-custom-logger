package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringCompilationParticipant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * This annotation allows user to specify attribute set of a target object to be printed for
 * different levels.
 */
@Target(ElementType.TYPE)
@Active(ToLevelledStringCompilationParticipant.class)
public @interface ToLevelledStringAnnotation {
  /**
   * BRIEF/INFO level attributes.
   */
  public String[] brief() default {};
  /**
   * MEDIUM/DEBUG level attributes.
   */
  public String[] medium() default {};
}
