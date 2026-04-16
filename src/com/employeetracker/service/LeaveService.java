package com.employeetracker.service;

import com.employeetracker.model.Employee;
import com.employeetracker.model.LeaveRequest;
import com.employeetracker.model.LeaveStatus;
import com.employeetracker.storage.FileStorageService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveService {

    private FileStorageService storage;

    private List<LeaveRequest> leaveRequests;
    private EmployeeService employeeService;
    private int nextRequestId;

    public LeaveService(EmployeeService employeeService, FileStorageService storage) {
        this.leaveRequests = new ArrayList<>();
        this.employeeService = employeeService;
        this.storage = storage;
        this.nextRequestId = 1;
    }


    /**
     * CREATE - Submit a new leave request
     * @return true if request was submitted successfully
     */
    public boolean submitLeaveRequest(String employeeId, String leaveType,
                                      LocalDate startDate, LocalDate endDate, String reason)
    {

        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Error: Employee with ID " + employeeId + " not found!");
            return false;
        }


        if (startDate.isAfter(endDate)) {
            System.out.println("Error: Start date cannot be after end date!");
            return false;
        }

        if (startDate.isBefore(LocalDate.now())) {
            System.out.println("Error: Cannot request leave for past dates!");
            return false;
        }


        double requestedDays = calculateDays(startDate, endDate);
        double availableBalance = getAvailableLeaveBalance(employeeId);

        if (requestedDays > availableBalance) {
            System.out.printf("Error: Insufficient leave balance! Available: %.1f days, Requested: %.1f days\n",
                    availableBalance, requestedDays);
            return false;
        }

        if (hasOverlappingRequest(employeeId, startDate, endDate)) {
            System.out.println("Error: You already have a leave request for these dates!");
            return false;
        }


        String requestId = "REQ" + String.format("%03d", nextRequestId++);


        LeaveRequest request = new LeaveRequest(requestId, employeeId, leaveType,
                startDate, endDate, reason);
        leaveRequests.add(request);
        saveToFile();
        System.out.println("Leave request submitted successfully! Request ID: " + requestId);
        System.out.println("   Days requested: " + requestedDays);
        System.out.println("   Status: PENDING (waiting for approval)");
        return true;
    }


    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    public List<LeaveRequest> getRequestsByEmployee(String employeeId) {
        return leaveRequests.stream()
                .filter(request -> request.getEmployeeId().equalsIgnoreCase(employeeId))
                .collect(Collectors.toList());
    }

    public List<LeaveRequest> getRequestsByStatus(LeaveStatus status) {
        return leaveRequests.stream()
                .filter(request -> request.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<LeaveRequest> getPendingRequests() {
        return getRequestsByStatus(LeaveStatus.PENDING);
    }

    public LeaveRequest findRequestById(String requestId) {
        for (LeaveRequest request : leaveRequests) {
            if (request.getRequestId().equalsIgnoreCase(requestId)) {
                return request;
            }
        }
        return null;
    }

    public boolean approveRequest(String requestId) {
        LeaveRequest request = findRequestById(requestId);
        if (request == null) {
            System.out.println( "Error: Request ID " + requestId + " not found!");
            return false;
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Error: Request is already " + request.getStatus());
            return false;
        }

        request.setStatus(LeaveStatus.APPROVED);
        saveToFile();
        System.out.println("Leave request " + requestId + " has been APPROVED!");
        System.out.println("   Employee: " + request.getEmployeeId());
        System.out.println("   Days approved: " + request.getNumberOfDays());
        return true;
    }

    public boolean rejectRequest(String requestId, String rejectionReason) {
        LeaveRequest request = findRequestById(requestId);
        if (request == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Error: Request is already " + request.getStatus());
            return false;
        }

        request.setStatus(LeaveStatus.REJECTED);
        saveToFile();
        System.out.println("Leave request " + requestId + " has been REJECTED!");
        if (rejectionReason != null && !rejectionReason.isEmpty()) {
            System.out.println("   Reason: " + rejectionReason);
        }
        return true;
    }

    public boolean cancelRequest(String requestId) {
        LeaveRequest request = findRequestById(requestId);
        if (request == null) {
            System.out.println("Error: Request ID " + requestId + " not found!");
            return false;
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Error: Cannot cancel a request that is already " + request.getStatus());
            return false;
        }

        leaveRequests.remove(request);
        saveToFile();
        System.out.println("Leave request " + requestId + " has been CANCELLED!");
        return true;
    }

    private long calculateDays(LocalDate startDate, LocalDate endDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private double getAvailableLeaveBalance(String employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) {
            return 0;
        }

        double totalAllowance = employee.getAnnualLeaveAllowance();


        double usedLeave = leaveRequests.stream()
                .filter(r -> r.getEmployeeId().equalsIgnoreCase(employeeId))
                .filter(r -> r.getStatus() == LeaveStatus.APPROVED)
                .mapToDouble(r -> r.getNumberOfDays())
                .sum();

        return totalAllowance - usedLeave;
    }

    private boolean hasOverlappingRequest(String employeeId, LocalDate startDate, LocalDate endDate) {
        return leaveRequests.stream()
                .filter(r -> r.getEmployeeId().equalsIgnoreCase(employeeId))
                .filter(r -> r.getStatus() == LeaveStatus.PENDING || r.getStatus() == LeaveStatus.APPROVED)
                .anyMatch(r -> !(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate())));
    }

    public void displayAllRequests() {
        if (leaveRequests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }
        System.out.println("ALL LEAVE REQUESTS");
        System.out.println("");

        for (LeaveRequest request : leaveRequests) {
            System.out.println(request);
            System.out.println("");
        }
        System.out.println("Total requests: " + leaveRequests.size());
    }

    public void displayPendingRequests() {
        List<LeaveRequest> pending = getPendingRequests();

        if (pending.isEmpty()) {
            System.out.println("No pending leave requests.");
            return;
        }

        System.out.println("PENDING LEAVE REQUESTS (Awaiting Approval)");
        System.out.println("");

        for (LeaveRequest request : pending) {
            Employee emp = employeeService.findEmployeeById(request.getEmployeeId());
            String empName = (emp != null) ? emp.getName() : "Unknown";
            System.out.println("ID: " + request.getRequestId() + " | Employee: " + empName +
                    " (" + request.getEmployeeId() + ")");
            System.out.println("   Type: " + request.getLeaveType() + " | " +
                    request.getStartDate() + " to " + request.getEndDate());
            System.out.println("   Days: " + request.getNumberOfDays() + " | Reason: " + request.getReason());
            System.out.println("");
        }
        System.out.println("Total pending: " + pending.size());
    }

    public void displayEmployeeRequests(String employeeId) {
        List<LeaveRequest> requests = getRequestsByEmployee(employeeId);

        if (requests.isEmpty()) {
            System.out.println("No leave requests found for employee " + employeeId);
            return;
        }
        System.out.println("LEAVE REQUESTS FOR EMPLOYEE: " + employeeId);
        System.out.println("");

        Employee emp = employeeService.findEmployeeById(employeeId);
        if (emp != null) {
            System.out.println("Employee: " + emp.getName());
            System.out.println("Annual Leave Allowance: " + emp.getAnnualLeaveAllowance() + " days");
            System.out.println("Available Balance: " + getAvailableLeaveBalance(employeeId) + " days");
        }

        System.out.println("\nRequest History");
        for (LeaveRequest request : requests) {
            String statusIcon = switch (request.getStatus()) {
                case APPROVED -> "✅";
                case REJECTED -> "❌";
                default -> "⏳";
            };
            System.out.printf("%s %s | %s to %s | Status: %s | Days: %d\n",
                    statusIcon, request.getRequestId(), request.getStartDate(),
                    request.getEndDate(), request.getStatus(), request.getNumberOfDays());
        }
        System.out.println("");
    }

    public void displayLeaveBalance(String employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            return;
        }

        double totalAllowance = employee.getAnnualLeaveAllowance();
        double usedLeave = leaveRequests.stream()
                .filter(r -> r.getEmployeeId().equalsIgnoreCase(employeeId))
                .filter(r -> r.getStatus() == LeaveStatus.APPROVED)
                .mapToDouble(r -> r.getNumberOfDays())
                .sum();
        double availableBalance = totalAllowance - usedLeave;
        System.out.println("LEAVE BALANCE FOR: " + employee.getName());
        System.out.println("");
        System.out.println("Annual Allowance:  " + totalAllowance + " days");
        System.out.println("Used Leave:        " + usedLeave + " days");
        System.out.println("Available Balance: " + availableBalance + " days");

        if (availableBalance <= 5) {
            System.out.println("Warning: Low leave balance!");
        }
    }
    public void loadFromFile() {
        List<LeaveRequest> loaded = storage.loadLeaveRequests();
        if (!loaded.isEmpty()) {
            this.leaveRequests = loaded;
            for (LeaveRequest request : loaded) {
                String id = request.getRequestId();
                if (id.startsWith("REQ")) {
                    try {
                        int num = Integer.parseInt(id.substring(3));
                        if (num >= nextRequestId) {
                            nextRequestId = num + 1;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }
    public void saveToFile() {
        storage.saveLeaveRequests(leaveRequests);
    }



    public void loadSampleLeaveRequests() {
        submitLeaveRequest("EMP001", "Vacation",
                LocalDate.of(2024, 5, 10), LocalDate.of(2024, 5, 15), "Summer vacation");

        submitLeaveRequest("EMP001", "Sick",
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 2), "Flu");

        submitLeaveRequest("EMP002", "Vacation",
                LocalDate.of(2024, 6, 20), LocalDate.of(2024, 6, 25), "Family trip");

        submitLeaveRequest("EMP003", "Personal",
                LocalDate.of(2024, 4, 15), LocalDate.of(2024, 4, 15), "Doctor appointment");
    }

    public void exportToCSV(String filePath) {
        storage.exportLeaveRequestsToCSV(filePath, leaveRequests);
    }
}
