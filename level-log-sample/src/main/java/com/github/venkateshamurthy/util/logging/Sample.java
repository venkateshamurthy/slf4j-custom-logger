package com.github.venkateshamurthy.util.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sample{
    public static void main(String[] args){
        Logger log=LoggerFactory.getLogger(Sample.class);
        Employee a = new Employee(new Date(70, 6, 24), "Aemployee", 2500000d);
        Employee b = new Employee(new Date(75, 9, 20), "Bemployee", 2500000d);
        Employer boss = new Employer(new Date(60, 3, 6),"BigBoss", 25000000d);
        boss.setBudget(100000d);
        Employer m = new Employer(new Date(65, 1, 1),"Manager", 5000000d);
        m.setBudget(10000d);
        boss.addReportee(m);
        m.addReportee(a);
        m.addReportee(b);
        a.setBoss(m.getName());
        b.setBoss(m.getName());
        m.setBoss(boss.getName());
        log.debug("Boss->{}",boss);
        log.debug("Manager->{}",m);
    }
}
