package com.employeetracker.model;
import java.time.LocalDate;

//PartTimeEmployee extends Employee - demonstrating INHERITANCE

public class PartTimeEmployee extends Employee{

    //Additional fields specific to part-time employees

    private double hourlyRate;
    private int hoursPerWeek;

    public PartTimeEmployee(String id, String name, String email, LocalDate hireDate,
                            double hourlyRate, int hoursPerWeek){
        super(id, name, email, hireDate);
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
    }
    public double getHourlyRate(){
        return hourlyRate;
    }
    public void setHourlyRate(double hourlyRate){
        this.hourlyRate = hourlyRate;
    }
    public int getHoursPerWeek(){
        return hoursPerWeek;
    }
    public void setHoursPerWeek(int hoursPerWeek){
        this.hoursPerWeek = hoursPerWeek;
    }
    //Part-time employees get prorated leave based on hours worked

    @Override
    public double getAnnualLeaveAllowance(){
        //Formula: (hours per week/40) * 20 days
        return (hoursPerWeek/40.0)* 20.0;
    }
    @Override
    public String getEmployeeType(){
        return "Part-Time";
    }
    @Override
    public String toString(){
        return super.toString() + String.format("| Rate: $%.2f/hr | Hours: %d/week", hourlyRate, hoursPerWeek);
    }
}
