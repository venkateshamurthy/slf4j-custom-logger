package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation;
import java.util.Date;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

@ToLevelledStringAnnotation(brief = { "name", "dateOfBirth" }, medium = { "name", "dateOfBirth", "salary", "boss" })
@SuppressWarnings("all")
public class Employee implements LevelledToString {
  protected final Date dateOfBirth;
  
  protected final String name;
  
  protected final double salary;
  
  private String boss;
  
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
    Class<? extends Employee> _class = this.getClass();
    String _simpleName = _class.getSimpleName();
    String _plus = ("The standard " + _simpleName);
    String _plus_1 = (_plus + ".toString invoked for ");
    String _plus_2 = (_plus_1 + this.name);
    System.out.println(_plus_2);
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, false);
  }
  
  public String setBoss(final String bossName) {
    return this.boss = bossName;
  }
  
  public String getBoss() {
    return this.boss;
  }
  
  private static Logger log = org.slf4j.LoggerFactory.getLogger(Employee.class);;
  
  /**
   * {@inheritDoc}
   */
  public String toString(final LevelOfDetail level) {
    log.trace("The special {}.toString({}) is invoked","Employee",level.name());
    if (level==null ){
       throw new IllegalArgumentException("level parameter cannot be null");
    }
    switch(level) {
       case NONE   : return "";
       case BRIEF  : return String.format("%s %s ",  name, dateOfBirth);
       case MEDIUM : return String.format("%s %s %s %s ", name, dateOfBirth, salary, boss);
       default     : return toString();
    }
  }
}
