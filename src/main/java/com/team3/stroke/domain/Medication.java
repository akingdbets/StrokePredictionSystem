package com.team3.stroke.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Medication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName; // 약 이름
    private String scheduledTime; // 복용 시간 (예: "09:00")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // 생성자 (Constructor)
    public Medication(String medicineName, String scheduledTime) {
        this.medicineName = medicineName;
        this.scheduledTime = scheduledTime;
    }
}