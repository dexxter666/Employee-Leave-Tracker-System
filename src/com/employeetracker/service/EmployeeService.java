package com.employeetracker.service;

import com.employeetracker.model.Employee;
import com.employeetracker.model.FullTimeEmployee;
import com.employeetracker.model.PartTimeEmployee;
import com.employeetracker.storage.FileStorageService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD operations for Employee objects
// This class demonstrates encapsulation and separation of concerns

public class EmployeeService {

    private FileStorageService storage;
    public EmployeeService(FileStorageService storage) {
        this.employees = new ArrayList<>();
        this.storage = storage;
    }


    private List<Employee> employees;

    public EmployeeService(){
        this.employees = new ArrayList<>();
    }
    public boolean addEmployee(Employee employee) {
        if (findEmployeeById(employee.getId()) != null) {
            System.out.println("Error: Employee with ID " + employee.getId() + " already exists!");
            return false;
        }
        employees.add(employee);
        saveToFile();
        System.out.println("Employee added successfully!");
        return true;
    }
    public List<Employee> getAllEmployees(){
        return new ArrayList<>(employees);
    }
    public Employee findEmployeeById(String id){
        for(Employee emp:employees){
            if(emp.getId().equalsIgnoreCase(id)){
                return emp;
            }
        }
       return null;
    }
    public List<Employee> findEmployeesByName(String name){
        List<Employee> result = new ArrayList<>();
        for(Employee emp : employees){
            if((emp.getName().toLowerCase().contains(name.toLowerCase()))){
                result.add(emp);
            }
        }
        return result;
    }
    public boolean updateEmployee(String id, String newName, String newEmail, LocalDate newHireDate) {
        Employee employee = findEmployeeById(id);
        if (employee == null) {
            System.out.println("Error: Employee with ID " + id + " not found!");
            return false;
        }

        if (newName != null && !newName.trim().isEmpty()) {
            employee.setName(newName);
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            employee.setEmail(newEmail);
        }
        if (newHireDate != null) {
            employee.setHireDate(newHireDate);
        }

        saveToFile();
        System.out.println("Employee updated successfully!");
        return true;
    }
    // Update full-time employee salary
    public boolean updateFullTimeSalary(String id, double newSalary) {
        Employee employee = findEmployeeById(id);
        if (employee == null) {
            System.out.println("Error: Employee with ID " + id + " not found!");
            return false;
        }

        if (employee instanceof FullTimeEmployee) {
            ((FullTimeEmployee) employee).setBaseSalary(newSalary);
            saveToFile();
            System.out.println("Salary updated successfully!");
            return true;
        } else {
            System.out.println("Error: Employee is not full-time!");
            return false;
        }
    }
    //Update part-time employee details
    public boolean updatePartTimeDetails(String id, Double hourlyRate, Integer hoursPerWeek) {
        Employee employee = findEmployeeById(id);
        if (employee == null) {
            System.out.println(" Error: Employee with ID " + id + " not found!");
            return false;
        }

        if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) employee;
            if (hourlyRate != null) {
                pt.setHourlyRate(hourlyRate);
            }
            if (hoursPerWeek != null) {
                pt.setHoursPerWeek(hoursPerWeek);
            }
            saveToFile();
            System.out.println("Part-time employee details updated successfully!");
            return true;
        } else {
            System.out.println("Error: Employee is not part-time!");
            return false;
        }
    }
    //DELETE - remove an employee by ID
    public boolean deleteEmployee(String id) {
        Employee employee = findEmployeeById(id);
        if (employee == null) {
            System.out.println("Error: Employee with ID " + id + " not found!");
            return false;
        }
        employees.remove(employee);
        saveToFile();
        System.out.println("Employee deleted successfully!");
        return true;
    }
    public void displayAllEmployees(){
        if(employees.isEmpty()){
            System.out.println("No employees found. Please add some employees first.");
            return;
        }
        System.out.println("EMPLOYEE LIST");
        System.out.println("");
        for(Employee emp : employees){
            System.out.println(emp);
            System.out.println("");
        }
        System.out.println("Total employees: " + employees.size());
    }
    public void displayEmployeeDetails(String id){
        Employee employee = findEmployeeById(id);
        if(employee == null){
            System.out.println("Employee with ID " + id + " not found!");
            return;
        }
        System.out.println("");
        System.out.println("EMPLOYEE DETAILS");
        System.out.println(employee);

        if(employee instanceof FullTimeEmployee){
            FullTimeEmployee ft = (FullTimeEmployee) employee;
            System.out.println("Annual salary: $" + ft.getBaseSalary());
            System.out.println("Monthly salary: $" + (ft.getBaseSalary() / 12));
        } else if(employee instanceof PartTimeEmployee){
            PartTimeEmployee pt = (PartTimeEmployee) employee;
            System.out.println("Hourly Rate: $" + pt.getHourlyRate());
            System.out.println("Hours per Week: " + pt.getHoursPerWeek());
            System.out.println("Weely Earnings: $" + (pt.getHourlyRate() * pt.getHoursPerWeek()));
        }
        System.out.println("");
    }
    public void loadFromFile() {
        List<Employee> loaded = storage.loadEmployees();
        if (!loaded.isEmpty()) {
            this.employees = loaded;
        }
    }
    public void saveToFile() {
        storage.saveEmployees(employees);
    }



    //Get total number of employees
    public int getEmployeeCount(){
        return employees.size();
    }
    public boolean employeeExists(String id){
        return findEmployeeById(id) != null;
    }
    public void exportToCSV(String filePath){
        storage.exportEmployeesToCSV(filePath, employees);
    }
}
