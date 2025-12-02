package com.team3.stroke.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponseDto {
    private String periodType; // "WEEKLY" or "MONTHLY"

    // 1. 위험도 점수 비교 (사용자 점수 vs 기준치)
    private int totalRiskScore;       // 기간 내 내 위험도 점수 합계
    private int totalThresholdScore;  // 기준 점수 (임계치 * 데이터 수)
    private String riskStatus;        // 상태 평가 (예: "기준치보다 20점 높습니다")

    // 2. 위험도 알림 전송 횟수
    private int alertCount;           // "DANGER" 횟수

    // 3. 이전 기간 대비 변화
    private double averageScore;      // 이번 기간 평균 점수
    private double scoreChange;       // 지난 기간 대비 점수 등락 (예: -5.5면 감소)
    private String trendMessage;      // "지난주보다 상태가 호전되었습니다."
}