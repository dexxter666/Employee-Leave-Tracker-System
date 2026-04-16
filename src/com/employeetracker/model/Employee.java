package com.employeetracker.model;
import java.time.LocalDate;

public abstract class Employee {

    private String id;
    private String name;
    private String email;
    private LocalDate hireDate;
    public Employee(String id, String name, String email, LocalDate hireDate){
        this.id = id;
        this.name = name;
        this.email = email;
        this.hireDate = hireDate;
    }
    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public abstract double getAnnualLeaveAllowance();
    public abstract String getEmployeeType();

    public String getBasicInfo(){
        return String.format("ID: %s | Name: %s | Email: %s", id, name, email);
    }
    @Override
    public String toString() {
        return String.format("%s | Type: %s | Hire Date: %s | Leave Allowance: %.1f days",
                getBasicInfo(), getEmployeeType(), hireDate, getAnnualLeaveAllowance());
    }
}
