package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.tostring.xtend.Employee;
import com.github.venkateshamurthy.util.tostring.xtend.Employer;
import java.util.Date;

@SuppressWarnings("all")
public class Examples {
  public static void main(final String[] args) {
    Date _date = new Date(70, 6, 24);
    final Employee a = new Employee(_date, "Aemployee", 2500000d);
    Date _date_1 = new Date(75, 9, 20);
    final Employee b = new Employee(_date_1, "Bemployee", 2500000d);
    Date _date_2 = new Date(65, 1, 1);
    final Employer m = new Employer(_date_2, "Manager", 5000000d, a, b);
  }
}
