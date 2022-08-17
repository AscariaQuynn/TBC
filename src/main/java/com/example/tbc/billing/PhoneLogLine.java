package com.example.tbc.billing;

import java.time.LocalDateTime;

public class PhoneLogLine {

    private String number;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public PhoneLogLine(String number, LocalDateTime startTime, LocalDateTime endTime) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return new StringBuilder("{\n")
                .append("number: ").append(number).append("\n")
                .append("startTime: ").append(startTime).append("\n")
                .append("endTime: ").append(endTime).append("\n")
                .append("}").append("\n")
                .toString();
    }
}
