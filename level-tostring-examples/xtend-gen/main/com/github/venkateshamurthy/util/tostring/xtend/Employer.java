package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.github.venkateshamurthy.util.tostring.xtend.Employee;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation;
import java.util.Date;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;

@ToLevelledStringAnnotation(medium = { "reportees" })
@SuppressWarnings("all")
public class Employer extends Employee implements LevelledToString {
  private final Employee[] reportees;
  
  public Employer(final Date dateOfBirth, final String name, final double salary) {
    super(dateOfBirth, name, salary);
    this.reportees = new Employee[] {};
  }
  
  public Employer(final Date dateOfBirth, final String name, final double salary, final Employee... employees) {
    super(dateOfBirth, name, salary);
    int _length = employees.length;
    Employee[] _newArrayOfSize = new Employee[_length];
    this.reportees = _newArrayOfSize;
    for (int i = 0; (i < this.reportees.length); i++) {
      Employee _get = employees[i];
      this.reportees[i] = _get;
    }
  }
  
  public boolean addReportee(final Employee employee) {
    return ((List<Employee>)Conversions.doWrapArray(this.reportees)).add(employee);
  }
  
  public Employee[] getReportees() {
    return this.reportees;
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
       case BRIEF  : return String.format("[%s] %s ",  super.toString(level), com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringUtil.toString(reportees, level));
       case MEDIUM : return String.format("[%s] %s ", super.toString(level), com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringUtil.toString(reportees, level));
       default     : return toString();
    }
  }
}
