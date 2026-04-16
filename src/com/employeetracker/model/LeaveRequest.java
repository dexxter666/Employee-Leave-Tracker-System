package com.employeetracker.model;
import java.time.LocalDate;

public class LeaveRequest {
    private String requestId;
    private String employeeId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
    private String reason;

    public LeaveRequest(String requestId, String employeeId, String leaveType,
                        LocalDate startDate, LocalDate endDate, String reason){
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
    }
    public String getRequestId(){
        return requestId;
    }
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }
    public String getEmployeeId(){
        return employeeId;
    }
    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }
    public String getLeaveType(){
        return leaveType;
    }
    public void  setLeaveType(String leaveType){
        this.leaveType = leaveType;
    }
    public LocalDate getStartDate(){
        return startDate;
    }
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }
    public LocalDate getEndDate(){
        return endDate;
    }
    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }
    public LeaveStatus getStatus() {
        return status;
    }
    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }

    public long getNumberOfDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    @Override
    public String toString() {
        return String.format("Request #%s | Employee: %s | Type: %s | %s to %s | Status: %s | Days: %d",
                requestId, employeeId, leaveType, startDate, endDate, status, getNumberOfDays());
    }
}
