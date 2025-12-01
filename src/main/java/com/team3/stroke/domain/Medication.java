package com.team3.stroke.domain;

public class Medication {
    private String medicineName; // 약 이름
    private String scheduledTime; // 복용 시간 (예: "09:00")

    // 생성자 (Constructor)
    public Medication(String medicineName, String scheduledTime) {
        this.medicineName = medicineName;
        this.scheduledTime = scheduledTime;
    }

    // Getter (값을 꺼내는 기능)
    public String getMedicineName() { return medicineName; }
    public String getScheduledTime() { return scheduledTime; }
}