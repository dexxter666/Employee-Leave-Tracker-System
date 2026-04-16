package com.employeetracker.model;

//Enum representing possible statuses of a leave request

public enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED;

    @Override
    public String toString(){
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

}
