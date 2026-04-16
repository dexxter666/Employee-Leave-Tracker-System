package com.employeetracker.model;

import java.time.LocalDate;

/**
 * FullTimeEmployee extends Employee - demonstrating INHERITANCE
 * This class inherits all fields and methods from Employee
 */
public class FullTimeEmployee extends Employee {

    // Additional field specific to full-time employees
    private double baseSalary;

    // Constructor - calls parent constructor using super()
    public FullTimeEmployee(String id, String name, String email, LocalDate hireDate, double baseSalary) {
        super(id, name, email, hireDate);  // Call parent constructor
        this.baseSalary = baseSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    /**
     * POLYMORPHISM in action: This method overrides the abstract method from Employee
     * Full-time employees get 20 days annual leave
     */
    @Override
    public double getAnnualLeaveAllowance() {
        return 20.0;
    }

    /**
     * POLYMORPHISM in action: This method overrides the abstract method from Employee
     */
    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Salary: $%.2f", baseSalary);
    }
}
