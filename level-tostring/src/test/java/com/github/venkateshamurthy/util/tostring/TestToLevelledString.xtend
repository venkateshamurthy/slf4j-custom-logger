package com.github.venkateshamurthy.util.tostring

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.testng.annotations.Test
import org.testng.Assert
import org.eclipse.xtend.lib.macro.services.Problem
@Test(groups=#['common'])
class TestToLevelledString {
    extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(class.classLoader)
    @Test
    def void testToLevelledString()
    {
        compilerTester.compile(
        '''
        package com.github.venkateshamurthy.util.tostring.xtend
        
        import java.util.Date
        import java.util.List
        import java.util.ArrayList
        import java.util.Set
        import java.util.HashSet
        import org.apache.commons.lang3.builder.ToStringBuilder
        import com.github.venkateshamurthy.util.logging.LevelOfDetail
        
        @ToLevelledStringAnnotation(brief=#['name','dateOfBirth'], medium=#['boss','name','dateOfBirth','salary'])
        class Employee {
            val protected Date dateOfBirth
            val protected String name
            val protected double salary
            var Employer boss;
            new(Date dateOfBirth, String name, double salary){
                this.name = name
                this.dateOfBirth = dateOfBirth
                this.salary = salary
            }
            def setBoss(Employer boss){
                this.boss = boss
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
                return ToStringBuilder.reflectionToString(this)
            }
            
        }
        @ToLevelledStringAnnotation(medium=#['reportees'])
        class Employer extends Employee{
            val Set<Employee> reportees = new HashSet<Employee>();
            new(Date dateOfBirth, String name, double salary) {
                super(dateOfBirth, name, salary)
            }
            new(Date dateOfBirth, String name, double salary, Employee... employees) {
                super(dateOfBirth, name, salary)
                reportees.addAll(employees);
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
                System.out.println('Trying to print something:'+m.toString(LevelOfDetail.MEDIUM))
            }
        }
        '''
        ,[
            Assert.assertTrue(allProblems.isEmpty())
            for(Problem problem:allProblems)
                println(problem.message)
            
        ]
        )
    }
}
