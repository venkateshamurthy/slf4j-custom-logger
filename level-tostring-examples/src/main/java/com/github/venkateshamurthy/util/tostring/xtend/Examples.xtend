package com.github.venkateshamurthy.util.tostring.xtend

import java.util.Date
import java.util.List
import java.util.ArrayList
import org.apache.commons.lang3.builder.ToStringBuilder
import com.github.venkateshamurthy.util.logging.LevelOfDetail
import org.apache.commons.lang3.builder.ToStringStyle

@ToLevelledStringAnnotation(brief=#['name','dateOfBirth'], medium=#['name','dateOfBirth','salary'])
class Employee {
    val protected Date dateOfBirth
    val protected String name
    val protected double salary
    
    new(Date dateOfBirth, String name, double salary){
        this.name = name
        this.dateOfBirth = dateOfBirth
        this.salary = salary
    }
    def getDateOfBirth(){
        dateOfBirth
    }
    def getName(){
        name
    }
    def getSalary(){
        salary
    }
    
    override String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE)
    }
    
}
@ToLevelledStringAnnotation(medium=#['reportees'])
class Employer extends Employee{
    val Employee[] reportees;
    new(Date dateOfBirth, String name, double salary) {
        super(dateOfBirth, name, salary)
        reportees =#[]
    }
    new(Date dateOfBirth, String name, double salary, Employee... employees) {
        super(dateOfBirth, name, salary)
        reportees = newArrayOfSize(employees.length)
        for(var int i=0;i<reportees.length;i++)
            reportees.set(i,employees.get(i))
    }
    
    def addReportee(Employee employee){
        reportees.add(employee)
    }
    
    def getReportees(){
        reportees
    }
}

class Examples {
    def static void main(String[] args){
        val Employee a = new Employee(new Date(70,6,24),"Aemployee", 2500000d)
        val Employee b = new Employee(new Date(75,9,20),"Bemployee", 2500000d)
        val Employer m = new Employer(new Date(65,1,1),"Manager", 5000000d,a,b)
    }
}