package com.employeetracker.model;
import java.time.LocalDate;

/**
 * Abstract parent class for all employee types.
 * This demonstrates INHERITANCE - child classes will extend this class.
 * This also demonstrates ENCAPSULATION - all fields are private.
 */
public abstract class Employee {
    //Private fields = ENCAPSULATION
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
    //Getters and setters = ENCAPSULATION
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
    //Abstract methods = INHERITANCE will be demonstrated when child classes
    //provide different implementations of these methods

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
