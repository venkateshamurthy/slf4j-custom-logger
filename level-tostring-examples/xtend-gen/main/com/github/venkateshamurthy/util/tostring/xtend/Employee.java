package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ToLevelledStringAnnotation(brief = { "name", "dateOfBirth" }, medium = { "name", "dateOfBirth", "salary" })
@SuppressWarnings("all")
public class Employee implements LevelledToString {
  protected final Date dateOfBirth;
  
  protected final String name;
  
  protected final double salary;
  
  public Employee(final Date dateOfBirth, final String name, final double salary) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.salary = salary;
  }
  
  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }
  
  public String getName() {
    return this.name;
  }
  
  public double getSalary() {
    return this.salary;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString(final LevelOfDetail level) {
    if (level==null ){
       throw new IllegalArgumentException("level parameter cannot be null");
    }
    switch(level) {
       case NONE   : return "";
       case BRIEF  : return String.format("%s %s ",  name, dateOfBirth);
       case MEDIUM : return String.format("%s %s %s ", name, dateOfBirth, salary);
       default     : return toString();
    }
  }
}
