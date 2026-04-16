package com.employeetracker.model;

public enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED;

    @Override
    public String toString(){
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

}
