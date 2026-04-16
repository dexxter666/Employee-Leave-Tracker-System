package com.employeetracker.validation;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String ID_REGEX = "^EMP\\d{3}$";
    private static final Pattern ID_PATTERN = Pattern.compile(ID_REGEX);

    public static boolean isValidEmployeeId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Validation Error: Employee ID cannot be empty");
            return false;
        }

        if (!ID_PATTERN.matcher(id).matches()) {
            System.out.println("Validation Error: Employee ID must be in format EMP001, EMP002, etc.");
            return false;
        }

        return true;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Validation Error: Name cannot be empty");
            return false;
        }

        if (name.trim().length() < 2) {
            System.out.println("Validation Error: Name must be at least 2 characters long");
            return false;
        }

        if (!name.matches("^[A-Za-z\\s\\-']+$")) {
            System.out.println("Validation Error: Name can only contain letters, spaces, hyphens, and apostrophes");
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Validation Error: Email cannot be empty");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Validation Error: Invalid email format. Example: name@domain.com");
            return false;
        }

        return true;
    }

    public static boolean isValidSalary(double salary) {
        if (salary <= 0) {
            System.out.println("Validation Error: Salary must be greater than 0");
            return false;
        }

        if (salary > 1_000_000) {
            System.out.println("Validation Error: Salary cannot exceed $1,000,000");
            return false;
        }

        return true;
    }

    public static boolean isValidHourlyRate(double rate) {
        if (rate <= 0) {
            System.out.println("Validation Error: Hourly rate must be greater than 0");
            return false;
        }

        if (rate > 200) {
            System.out.println("Validation Error: Hourly rate cannot exceed $200");
            return false;
        }

        return true;
    }

    public static boolean isValidHoursPerWeek(int hours) {
        if (hours <= 0) {
            System.out.println("Validation Error: Hours per week must be greater than 0");
            return false;
        }

        if (hours > 40) {
            System.out.println("Validation Error: Hours per week cannot exceed 40 for part-time employees");
            return false;
        }

        return true;
    }

    public static boolean isValidDate(LocalDate date, boolean canBeInPast) {
        if (date == null) {
            System.out.println("Validation Error: Date cannot be empty");
            return false;
        }

        if (!canBeInPast && date.isBefore(LocalDate.now())) {
            System.out.println("Validation Error: Start date cannot be in the past");
            return false;
        }

        return true;
    }

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            System.out.println("Validation Error: Dates cannot be empty");
            return false;
        }

        if (startDate.isAfter(endDate)) {
            System.out.println("Validation Error: Start date cannot be after end date");
            return false;
        }

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysBetween > 30) {
            System.out.println("Validation Error: Leave request cannot exceed 30 days");
            return false;
        }

        return true;
    }

    public static boolean isValidLeaveType(String leaveType) {
        if (leaveType == null || leaveType.trim().isEmpty()) {
            System.out.println("Validation Error: Leave type cannot be empty");
            return false;
        }

        String[] validTypes = {"Vacation", "Sick", "Personal", "Other"};
        for (String type : validTypes) {
            if (type.equalsIgnoreCase(leaveType)) {
                return true;
            }
        }

        System.out.println("Validation Error: Leave type must be Vacation, Sick, Personal, or Other");
        return false;
    }

    public static boolean isValidReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("Validation Error: Reason cannot be empty");
            return false;
        }

        if (reason.length() > 200) {
            System.out.println("Validation Error: Reason cannot exceed 200 characters");
            return false;
        }

        return true;
    }

    public static boolean isValidPositiveInt(int number) {
        if (number <= 0) {
            System.out.println("Validation Error: Number must be positive");
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Validation Error: " + fieldName + " cannot be empty");
            return false;
        }
        return true;
    }
}
