import com.employeetracker.model.Employee;
import com.employeetracker.model.FullTimeEmployee;
import com.employeetracker.model.PartTimeEmployee;
import com.employeetracker.model.LeaveStatus;
import com.employeetracker.service.EmployeeService;
import com.employeetracker.service.LeaveService;
import com.employeetracker.storage.FileStorageService;
import com.employeetracker.validation.InputValidator;

import java.time.LocalDate;
import java.util.Scanner;
public class Main {

    private static FileStorageService storage = new FileStorageService();

    private static EmployeeService employeeService = new EmployeeService(storage);

    private static LeaveService leaveService = new LeaveService(employeeService, storage);

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("EMPLOYEE LEAVE TRACKER SYSTEM");
        System.out.println("");

        System.out.println("\n Loading saved data...");
        employeeService.loadFromFile();
        leaveService.loadFromFile();

        if (employeeService.getEmployeeCount() == 0) {
            System.out.println("No saved data found. Loading sample data for first time...");
            loadSampleData();
            employeeService.saveToFile();
            leaveService.saveToFile();
            System.out.println("Sample data saved to files.");
        }

        System.out.println("System ready! " + employeeService.getEmployeeCount() + " employees loaded.");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    employeeMenu();
                    break;
                case 2:
                    leaveMenu();
                    break;
                case 3:
                    System.out.println("\nExiting... Saving data...");
                    employeeService.saveToFile();
                    leaveService.saveToFile();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 1-3.");
            }
        }
        scanner.close();
    }

    private static void showMainMenu() {
        System.out.println("");
        System.out.println("MAIN MENU");
        System.out.println("");
        System.out.println("1. EMPLOYEE MANAGEMENT");
        System.out.println("2. LEAVE REQUEST MANAGEMENT");
        System.out.println("3. EXIT");
        System.out.println("");
    }
    private static void employeeMenu() {
        boolean back = true;
        while (back) {
            System.out.println("EMPLOYEE MANAGEMENT");
            System.out.println("");
            System.out.println("1. CREATE - Add New Employee");
            System.out.println("2.  READ - View All Employees");
            System.out.println("3.  READ - Search Employee");
            System.out.println("4. ️  UPDATE - Edit Employee");
            System.out.println("5.️  DELETE - Remove Employee");
            System.out.println("6.  Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addEmployeeMenu();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    searchEmployeeMenu();
                    break;
                case 4:
                    updateEmployeeMenu();
                    break;
                case 5:
                    deleteEmployeeMenu();
                    break;
                case 6:
                    back = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }


    private static void leaveMenu() {
        boolean back = true;
        while (back) {
            System.out.println("LEAVE REQUEST MANAGEMENT");
            System.out.println("");
            System.out.println("1. CREATE - Submit Leave Request");
            System.out.println("2.  READ - View All Leave Requests");
            System.out.println("3.  READ - Search Leave Requests by ID");
            System.out.println("4.  READ - Check Leave Balance");
            System.out.println("5.  READ - View Pending Requests");
            System.out.println("6.  UPDATE - Approve Leave Request");
            System.out.println("7.  UPDATE - Reject Leave Request");
            System.out.println("8.  DELETE - Cancel a Pending Request");
            System.out.println("9. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    submitLeaveMenu();
                    break;
                case 2:
                    leaveService.displayAllRequests();
                    break;
                case 3:
                    viewMyRequestsMenu();
                    break;
                case 4:
                    checkLeaveBalanceMenu();
                    break;
                case 5:
                    leaveService.displayPendingRequests();
                    break;
                case 6:
                    approveRequestMenu();
                    break;
                case 7:
                    rejectRequestMenu();
                    break;
                case 8:
                    cancelRequestMenu();
                    break;
                case 9:
                    back = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addEmployeeMenu() {

        System.out.println("ADD NEW EMPLOYEE");
        System.out.println("");
        System.out.println("Select Employee Type:");
        System.out.println("1. Full-Time Employee");
        System.out.println("2. Part-Time Employee");
        int type = getIntInput("Choice (1-2): ");

        if (type != 1 && type != 2) {
            System.out.println("Invalid employee type! Please select 1 or 2.");
            return;
        }

        String id;
        boolean valid;
        do {
            id = getStringInput("Enter Employee ID (format: EMP001, EMP002, etc.): ");

            valid = InputValidator.isValidEmployeeId(id);

            if (!valid) {
                System.out.println("Example: EMP001, EMP002, EMP003");
            }

        } while (!valid);

        if (employeeService.employeeExists(id)) {
            System.out.println("Error: Employee ID already exists!");
            return;
        }

        String name;
        do {
            name = getStringInput("Enter Name: ");
        } while (!InputValidator.isValidName(name));

        String email;
        do {
            email = getStringInput("Enter Email: ");
        } while (!InputValidator.isValidEmail(email));

        LocalDate hireDate;
        do {
            hireDate = getDateInput("Enter Hire Date (YYYY-MM-DD): ");
        } while (!InputValidator.isValidDate(hireDate, true));

        if (type == 1) {
            double salary;
            do {
                salary = getDoubleInput("Enter Annual Salary($): ");
            } while (!InputValidator.isValidSalary(salary));

            FullTimeEmployee emp = new FullTimeEmployee(id, name, email, hireDate, salary);
            employeeService.addEmployee(emp);

        } else if (type == 2) {
            double hourlyRate;
            do {
                hourlyRate = getDoubleInput("Enter Hourly Rate($): ");
            } while (!InputValidator.isValidHourlyRate(hourlyRate));

            int hoursPerWeek;
            do {
                hoursPerWeek = getIntInput("Enter Hours per Week (1-40): ");
            } while (!InputValidator.isValidHoursPerWeek(hoursPerWeek));

            PartTimeEmployee emp = new PartTimeEmployee(id, name, email, hireDate, hourlyRate, hoursPerWeek);
            employeeService.addEmployee(emp);
        }
    }

    private static void viewAllEmployees() {
        employeeService.displayAllEmployees();
    }

    private static void searchEmployeeMenu() {
        System.out.println("SEARCH EMPLOYEE");
        System.out.println("");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        int choice = getIntInput("Choice (1-2): ");

        if (choice == 1) {
            String id = getStringInput("Enter Employee ID: ");
            employeeService.displayEmployeeDetails(id);
        } else if (choice == 2) {
            String name = getStringInput("Enter Name: ");
            var results = employeeService.findEmployeesByName(name);
            if (results.isEmpty()) {
                System.out.println( "No employees found.");
            } else {
                for (Employee emp : results) {
                    System.out.println(emp);
                }
            }
        }
    }

    private static void updateEmployeeMenu() {
        System.out.println("UPDATE EMPLOYEE");
        System.out.println("");

        String id = getStringInput("Enter Employee ID to update: ");

        if (!employeeService.employeeExists(id)) {
            System.out.println(" Employee not found!");
            return;
        }

        employeeService.displayEmployeeDetails(id);

        System.out.println("\n1. Update Basic Info");
        System.out.println("2. Update Salary/Rate");
        System.out.println("3. Cancel");

        int choice = getIntInput("Choice: ");

        if (choice == 1) {
            String newName = getStringInput("Enter New Name (Enter to skip): ");
            String newEmail = getStringInput("Enter New Email (Enter to skip): ");
            String dateInput = getStringInput("Enter New Hire Date (Enter to skip): ");
            LocalDate newDate = dateInput.isEmpty() ? null : LocalDate.parse(dateInput);

            employeeService.updateEmployee(id,
                    newName.isEmpty() ? null : newName,
                    newEmail.isEmpty() ? null : newEmail,
                    newDate);
        } else if (choice == 2) {
            Employee emp = employeeService.findEmployeeById(id);
            if (emp instanceof FullTimeEmployee) {
                double newSalary = getDoubleInput("Enter New Salary: ");
                employeeService.updateFullTimeSalary(id, newSalary);
            } else if (emp instanceof PartTimeEmployee) {
                String rateInput = getStringInput("Enter New Hourly Rate (Enter to skip): ");
                String hoursInput = getStringInput("Enter New Hours/Week (Enter to skip): ");

                Double newRate = rateInput.isEmpty() ? null : Double.parseDouble(rateInput);
                Integer newHours = hoursInput.isEmpty() ? null : Integer.parseInt(hoursInput);

                employeeService.updatePartTimeDetails(id, newRate, newHours);
            }
        }
    }

    private static void deleteEmployeeMenu() {
        System.out.println(" DELETE EMPLOYEE");
        System.out.println("");

        String id = getStringInput("Enter Employee ID to delete: ");

        if (employeeService.employeeExists(id)) {
            employeeService.displayEmployeeDetails(id);
            String confirm = getStringInput("\nAre you sure? (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                employeeService.deleteEmployee(id);
            }
        } else {
            System.out.println("Employee not found!");
        }
    }

    private static void submitLeaveMenu() {
        System.out.println("SUBMIT LEAVE REQUEST");
        System.out.println("");

        String employeeId = getStringInput("Enter Your Employee ID: ");

        if (!employeeService.employeeExists(employeeId)) {
            System.out.println("Employee ID not found!");
            return;
        }

        leaveService.displayLeaveBalance(employeeId);

        System.out.println("\nLeave Types:");
        System.out.println("1. Vacation");
        System.out.println("2. Sick");
        System.out.println("3. Personal");
        System.out.println("4. Other");

        int typeChoice = getIntInput("Select leave type (1-4): ");
        String leaveType = switch (typeChoice) {
            case 1 -> "Vacation";
            case 2 -> "Sick";
            case 3 -> "Personal";
            default -> "Other";
        };

        LocalDate startDate = getDateInput("Enter Start Date (YYYY-MM-DD): ");
        LocalDate endDate = getDateInput("Enter End Date (YYYY-MM-DD): ");
        String reason = getStringInput("Enter Reason: ");

        leaveService.submitLeaveRequest(employeeId, leaveType, startDate, endDate, reason);
    }

    private static void viewMyRequestsMenu() {
        String employeeId = getStringInput("Enter an Employee ID: ");
        if (!employeeService.employeeExists(employeeId)) {
            System.out.println(" Employee ID not found!");
            return;
        }
        leaveService.displayEmployeeRequests(employeeId);
    }

    private static void checkLeaveBalanceMenu() {
        String employeeId = getStringInput("Enter Employee ID: ");
        leaveService.displayLeaveBalance(employeeId);
    }

    private static void approveRequestMenu() {
        System.out.println("APPROVE LEAVE REQUEST");
        System.out.println("");

        leaveService.displayPendingRequests();

        if (leaveService.getPendingRequests().isEmpty()) {
            return;
        }

        String requestId = getStringInput("\nEnter Request ID to APPROVE: ");
        leaveService.approveRequest(requestId);
    }

    private static void rejectRequestMenu() {
        System.out.println("REJECT LEAVE REQUEST");
        System.out.println("");

        leaveService.displayPendingRequests();

        if (leaveService.getPendingRequests().isEmpty()) {
            return;
        }

        String requestId = getStringInput("\nEnter Request ID to REJECT: ");
        String reason = getStringInput("Enter rejection reason: ");
        leaveService.rejectRequest(requestId, reason);
    }

    private static void cancelRequestMenu() {
        System.out.println("CANCEL LEAVE REQUEST");
        System.out.println("");

        String employeeId = getStringInput("Enter Your Employee ID: ");

        if (!employeeService.employeeExists(employeeId)) {
            System.out.println( "Employee ID not found!");
            return;
        }

        var pendingRequests = leaveService.getRequestsByEmployee(employeeId).stream()
                .filter(r -> r.getStatus() == LeaveStatus.PENDING)
                .toList();

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending requests to cancel.");
            return;
        }

        System.out.println("\nYour Pending Requests:");
        for (var request : pendingRequests) {
            System.out.println("   " + request.getRequestId() + " | " + request.getLeaveType() +
                    " | " + request.getStartDate() + " to " + request.getEndDate());
        }

        String requestId = getStringInput("\nEnter Request ID to CANCEL: ");
        leaveService.cancelRequest(requestId);
    }

    private static void loadSampleData() {
        System.out.println("\nLoading sample data...");

        // Employees
        FullTimeEmployee emp1 = new FullTimeEmployee("EMP001", "John Wick", "johnwick@gmail.com",
                LocalDate.of(2023, 1, 15), 55000.0);
        FullTimeEmployee emp2 = new FullTimeEmployee("EMP002", "Mohammed Salah", "salah11@gmail.com",
                LocalDate.of(2022, 3, 10), 75000.0);
        PartTimeEmployee emp3 = new PartTimeEmployee("EMP003", "Michael Olise", "nonchalant17@gmail.com",
                LocalDate.of(2024, 1, 20), 22.5, 25);
        PartTimeEmployee emp4 = new PartTimeEmployee("EMP004", "Levy Ackerman", "captainlevy@gmail.com",
                LocalDate.of(2024, 2, 15), 18.0, 20);

        employeeService.addEmployee(emp1);
        employeeService.addEmployee(emp2);
        employeeService.addEmployee(emp3);
        employeeService.addEmployee(emp4);

        // Leave Requests
        leaveService.submitLeaveRequest("EMP001", "Vacation",
                LocalDate.of(2024, 5, 10), LocalDate.of(2024, 5, 15), "Summer vacation");
        leaveService.submitLeaveRequest("EMP001", "Sick",
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2), "Flu");
        leaveService.submitLeaveRequest("EMP002", "Vacation",
                LocalDate.of(2024, 6, 20), LocalDate.of(2024, 6, 25), "Family trip");
        leaveService.submitLeaveRequest("EMP003", "Personal",
                LocalDate.of(2024, 4, 15), LocalDate.of(2024, 4, 15), "Doctor appointment");

        System.out.println("Loaded " + employeeService.getEmployeeCount() + " employees");
        System.out.println("Loaded " + leaveService.getAllLeaveRequests().size() + " leave requests");
    }



    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD (e.g., 2024-03-31)");
            }
        }
    }

}