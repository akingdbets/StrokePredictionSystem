package com.team3.stroke.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class HealthData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime recordDate;

    private double systolicBP;          // ✅ 혈압 (다시 추가됨)
    private boolean smokingStatus;      // 흡연 여부
    private float bloodSugar;           // 혈당
    private String bodyComposition;     // 체성분
    private int activityLevel;          // 활동량
    private boolean medicationTaken;    // 약물 복용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
}