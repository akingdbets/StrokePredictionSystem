package com.team3.stroke.dto;

import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class HealthInputRequest {

    private Long patientId;          // 환자 ID (누구의 데이터인지)

    private double systolicBP;       // 수축기 혈압 (핵심 위험 인자)
    private boolean smokingStatus;   // 흡연 여부
    private float bloodSugar;        // 혈당
    private String bodyComposition;  // 체성분 (비만 등)
    private int activityLevel;       // 활동량 (1~5)
    private boolean medicationTaken; // 약물 복용 여부
}