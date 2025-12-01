package com.team3.stroke.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientPanelDto {
    private Long patientId;
    private String name;
    private int currentRiskScore; // 현재 위험도
    private boolean isHighRisk;      // 고위험군 여부 (예: 80점 이상)
    private String recentReportDate; // 최근 리포트 날짜
}