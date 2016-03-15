package com.github.venkateshamurthy.util.logging

import java.util.Date
import java.util.List
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation

@ToLevelledStringAnnotation(brief=#['name', 'dateOfBirth'], medium=#['name', 'dateOfBirth', 'salary', 'boss'])
class Employee {
    val protected Date dateOfBirth
    val protected String name
    val protected double salary
    var String boss;

    new(Date dateOfBirth, String name, double salary) {
        this.name = name
        this.dateOfBirth = dateOfBirth
        this.salary = salary
    }

    def getDateOfBirth() {
        dateOfBirth
    }

    def getName() {
        name
    }

    def getSalary() {
        salary
    }

    override String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, false)
    }

    def setBoss(String bossName) {
        boss = bossName
    }

    def getBoss() {
        boss
    }
}

@ToLevelledStringAnnotation(brief=#['reportees'], medium=#['budget','reportees'])
class Employer extends Employee {
    var double budget;
    var Employee[] reportees;

    new(Date dateOfBirth, String name, double salary) {
        super(dateOfBirth, name, salary)
        reportees = #[]
    }

    def addReportee(Employee employee) {
        val List<Employee> existingReportees = newArrayList(reportees.toList);
        existingReportees.add(employee)
        reportees = newArrayOfSize(existingReportees.size())
        existingReportees.toArray(reportees)
    }

    def getReportees() {
        reportees
    }

    def setBudget(double budget) {
        this.budget = budget
    }

    def getBudget() {
        budget
    }

}

