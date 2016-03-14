package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.github.venkateshamurthy.util.tostring.xtend.Employee;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation;
import java.util.Date;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.slf4j.Logger;

@ToLevelledStringAnnotation(brief = { "reportees" }, medium = { "budget", "reportees" })
@SuppressWarnings("all")
public class Employer extends Employee implements LevelledToString {
  private double budget;
  
  private Employee[] reportees;
  
  public Employer(final Date dateOfBirth, final String name, final double salary) {
    super(dateOfBirth, name, salary);
    this.reportees = new Employee[] {};
  }
  
  public Employee[] addReportee(final Employee employee) {
    Employee[] _xblockexpression = null;
    {
      List<Employee> _list = IterableExtensions.<Employee>toList(((Iterable<Employee>)Conversions.doWrapArray(this.reportees)));
      final List<Employee> existingReportees = CollectionLiterals.<Employee>newArrayList(((Employee[])Conversions.unwrapArray(_list, Employee.class)));
      existingReportees.add(employee);
      int _size = existingReportees.size();
      Employee[] _newArrayOfSize = new Employee[_size];
      this.reportees = _newArrayOfSize;
      _xblockexpression = existingReportees.<Employee>toArray(this.reportees);
    }
    return _xblockexpression;
  }
  
  public Employee[] getReportees() {
    return this.reportees;
  }
  
  public double setBudget(final double budget) {
    return this.budget = budget;
  }
  
  public double getBudget() {
    return this.budget;
  }
  
  private static Logger log = org.slf4j.LoggerFactory.getLogger(Employer.class);;
  
  /**
   * {@inheritDoc}
   */
  public String toString(final LevelOfDetail level) {
    log.trace("The special {}.toString({}) is invoked","Employer",level.name());
    if (level==null ){
       throw new IllegalArgumentException("level parameter cannot be null");
    }
    switch(level) {
       case NONE   : return "";
       case BRIEF  : return String.format("[%s] %s ",  super.toString(level), reportees==null?"null":com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringUtil.toString(reportees, level));
       case MEDIUM : return String.format("[%s] %s %s ", super.toString(level), budget, reportees==null?"null":com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringUtil.toString(reportees, level));
       default     : return toString();
    }
  }
}
