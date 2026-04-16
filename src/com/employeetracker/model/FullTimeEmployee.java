package com.employeetracker.model;

import java.time.LocalDate;
public class FullTimeEmployee extends Employee {

    private double baseSalary;

    public FullTimeEmployee(String id, String name, String email, LocalDate hireDate, double baseSalary) {
        super(id, name, email, hireDate);
        this.baseSalary = baseSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public double getAnnualLeaveAllowance() {
        return 20.0;
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Salary: $%.2f", baseSalary);
    }
}
