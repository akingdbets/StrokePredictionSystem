package com.team3.stroke.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PatientReportDetailDto {
    // 1. 환자 기본 정보
    private Long patientId;
    private String name;
    private String doctorName;

    // 2. 핵심 위험도 정보
    private int currentRiskScore;
    private String riskLevel;

    // 3. 최신 건강 데이터 (HealthData 엔티티 반영)
    private double recentSystolicBP; // [수정] String bloodPressure -> double systolicBP
    private float recentBloodSugar;  // double -> float
    private boolean isSmoker;
    private LocalDateTime lastCheckupDate; // recordDate 매핑

    // 4. 주치의 소견
    private String doctorMemo;
    private LocalDateTime lastReportDate;
}