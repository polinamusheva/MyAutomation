package com.example.my_automation.enums;

public enum Status {
    STATUS_ACTIVE,
    STATUS_FINISHED,
    FAILED;

    private Status() {
    }

    public String getSource() {
        return this.name();
    }
}
