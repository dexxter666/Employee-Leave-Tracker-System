package com.employeetracker.storage;

import com.employeetracker.model.Employee;
import com.employeetracker.model.FullTimeEmployee;
import com.employeetracker.model.PartTimeEmployee;
import com.employeetracker.model.LeaveRequest;
import com.employeetracker.model.LeaveStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading data to/from CSV files
 */
public class FileStorageService {

    private static final String DATA_DIR = "data/";
    private static final String EMPLOYEES_FILE = DATA_DIR + "employees.csv";
    private static final String LEAVE_REQUESTS_FILE = DATA_DIR + "leave_requests.csv";

    public FileStorageService() {
        // Create data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    //Save methods

    /**
     * Save all employees to CSV file
     */
    public void saveEmployees(List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            // Write header
            writer.println("ID,Name,Email,HireDate,Type,Salary,HourlyRate,HoursPerWeek");

            // Write each employee
            for (Employee emp : employees) {
                String line = employeeToCsv(emp);
                writer.println(line);
            }

            System.out.println("Saved " + employees.size() + " employees to file.");

        } catch (IOException e) {
            System.err.println( "Error saving employees: " + e.getMessage());
        }
    }

    /**
     * Save all leave requests to CSV file
     */
    public void saveLeaveRequests(List<LeaveRequest> requests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_REQUESTS_FILE))) {
            // Write header
            writer.println("RequestId,EmployeeId,LeaveType,StartDate,EndDate,Status,Reason");

            // Write each request
            for (LeaveRequest request : requests) {
                String line = leaveRequestToCsv(request);
                writer.println(line);
            }

            System.out.println(" Saved " + requests.size() + " leave requests to file.");

        } catch (IOException e) {
            System.err.println(" Error saving leave requests: " + e.getMessage());
        }
    }

    //Load methods

    /**
     * Load all employees from CSV file
     */
    public List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();
        File file = new File(EMPLOYEES_FILE);

        if (!file.exists()) {
            System.out.println("No existing employee file found. Starting fresh.");
            return employees;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                Employee emp = csvToEmployee(line);
                if (emp != null) {
                    employees.add(emp);
                }
            }

            System.out.println("Loaded " + employees.size() + " employees from file.");

        } catch (IOException e) {
            System.err.println("Error loading employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Load all leave requests from CSV file
     */
    public List<LeaveRequest> loadLeaveRequests() {
        List<LeaveRequest> requests = new ArrayList<>();
        File file = new File(LEAVE_REQUESTS_FILE);

        if (!file.exists()) {
            System.out.println(" No existing leave requests file found. Starting fresh.");
            return requests;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                LeaveRequest request = csvToLeaveRequest(line);
                if (request != null) {
                    requests.add(request);
                }
            }

            System.out.println("Loaded " + requests.size() + " leave requests from file.");

        } catch (IOException e) {
            System.err.println(" Error loading leave requests: " + e.getMessage());
        }

        return requests;
    }

    //Conversion methods

    /**
     * Convert Employee object to CSV string
     */
    private String employeeToCsv(Employee emp) {
        if (emp instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) emp;
            return String.format("%s,%s,%s,%s,FullTime,%.2f,,",
                    ft.getId(),
                    ft.getName(),
                    ft.getEmail(),
                    ft.getHireDate(),
                    ft.getBaseSalary());
        } else if (emp instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) emp;
            return String.format("%s,%s,%s,%s,PartTime,,%.2f,%d",
                    pt.getId(),
                    pt.getName(),
                    pt.getEmail(),
                    pt.getHireDate(),
                    pt.getHourlyRate(),
                    pt.getHoursPerWeek());
        }
        return "";
    }

    /**
     * Convert CSV string to Employee object
     */
    private Employee csvToEmployee(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            return null;
        }

        String id = parts[0];
        String name = parts[1];
        String email = parts[2];
        LocalDate hireDate = LocalDate.parse(parts[3]);
        String type = parts[4];

        if (type.equals("FullTime")) {
            double salary = Double.parseDouble(parts[5]);
            return new FullTimeEmployee(id, name, email, hireDate, salary);
        } else if (type.equals("PartTime")) {
            double hourlyRate = Double.parseDouble(parts[6]);
            int hoursPerWeek = Integer.parseInt(parts[7]);
            return new PartTimeEmployee(id, name, email, hireDate, hourlyRate, hoursPerWeek);
        }

        return null;
    }

    /**
     * Convert LeaveRequest object to CSV string
     */
    private String leaveRequestToCsv(LeaveRequest request) {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                request.getRequestId(),
                request.getEmployeeId(),
                request.getLeaveType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStatus().name(),
                request.getReason().replace(",", ";") // Replace commas to avoid CSV issues
        );
    }

    /**
     * Convert CSV string to LeaveRequest object
     */
    private LeaveRequest csvToLeaveRequest(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            return null;
        }

        String requestId = parts[0];
        String employeeId = parts[1];
        String leaveType = parts[2];
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);
        LeaveStatus status = LeaveStatus.valueOf(parts[5].toUpperCase());
        String reason = parts[6].replace(";", ",");

        LeaveRequest request = new LeaveRequest(requestId, employeeId, leaveType,
                startDate, endDate, reason);
        request.setStatus(status);

        return request;
    }

     //Export all employees to a CSV file (user-specified location)

    /**
     * Export all employees to CSV file
     */
    public void exportEmployeesToCSV(String filePath, List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("ID,Name,Email,HireDate,EmployeeType,Salary/Rate,HoursPerWeek");

            // Write each employee
            for (Employee emp : employees) {
                String line = exportEmployeeToCSVLine(emp);
                writer.println(line);
            }

            System.out.println("Successfully exported " + employees.size() + " employees to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting employees: " + e.getMessage());
        }
    }

    /**
     * Convert a single employee to CSV line
     */
    private String exportEmployeeToCSVLine(Employee emp) {
        String id = emp.getId();
        String name = emp.getName();
        String email = emp.getEmail();
        String hireDate = emp.getHireDate().toString();
        String employeeType = emp.getEmployeeType();

        if (emp instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) emp;
            double salary = ft.getBaseSalary();
            return String.format("%s,%s,%s,%s,%s,%.2f,", id, name, email, hireDate, employeeType, salary);
        } else if (emp instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) emp;
            double rate = pt.getHourlyRate();
            int hours = pt.getHoursPerWeek();
            return String.format("%s,%s,%s,%s,%s,%.2f,%d", id, name, email, hireDate, employeeType, rate, hours);
        }

        return String.format("%s,%s,%s,%s,%s,,", id, name, email, hireDate, employeeType);
    }

    /**
     * Export all leave requests to CSV file
     */
    public void exportLeaveRequestsToCSV(String filePath, List<LeaveRequest> leaveRequests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("RequestID,EmployeeID,LeaveType,StartDate,EndDate,Status,Reason,Days");

            // Write each leave request
            for (LeaveRequest request : leaveRequests) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%d",
                        request.getRequestId(),
                        request.getEmployeeId(),
                        request.getLeaveType(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getStatus().name(),
                        request.getReason().replace(",", ";"), // Replace commas to avoid breaking CSV
                        request.getNumberOfDays()
                );
                writer.println(line);
            }

            System.out.println("Successfully exported " + leaveRequests.size() + " leave requests to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting leave requests: " + e.getMessage());
        }
    }
}
