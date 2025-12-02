package com.team3.stroke.service;

import com.team3.stroke.domain.Parameter;
import com.team3.stroke.domain.Patient;
import com.team3.stroke.domain.Risk;
import com.team3.stroke.dto.ReportResponseDto;
import com.team3.stroke.repository.ParameterRepository;
import com.team3.stroke.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final PatientRepository patientRepository;
    private final ParameterRepository parameterRepository;

    // 메인 기능: 리포트 생성
    public ReportResponseDto generateReport(Long patientId, String period) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("환자 없음"));

        // 1. 기간 설정 (일수)
        int days = "MONTHLY".equalsIgnoreCase(period) ? 30 : 7;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(days);
        LocalDateTime prevStartDate = startDate.minusDays(days); // 비교용 이전 기간

        // 2. 데이터 필터링
        List<Risk> allHistory = patient.getRiskHistory();

        // 이번 기간 데이터
        List<Risk> currentLogs = filterRiskByDate(allHistory, startDate, now);
        // 이전 기간 데이터 (비교용)
        List<Risk> prevLogs = filterRiskByDate(allHistory, prevStartDate, startDate);

        // 3. 분석 수행
        return analyzeData(currentLogs, prevLogs, period);
    }

    private List<Risk> filterRiskByDate(List<Risk> history, LocalDateTime start, LocalDateTime end) {
        return history.stream()
                .filter(r -> r.getCalculatedDate().isAfter(start) && r.getCalculatedDate().isBefore(end))
                .collect(Collectors.toList());
    }

    private ReportResponseDto analyzeData(List<Risk> current, List<Risk> prev, String period) {
        // A. 합계 및 알림 횟수 계산
        int totalScore = current.stream().mapToInt(Risk::getScore).sum();
        int alertCount = (int) current.stream().filter(Risk::isThresholdExceeded).count();

        // B. 임계치 기준값 계산 (DB에서 'TOTAL_THRESHOLD' 가져옴)
        Parameter thresholdParam = parameterRepository.findByParameterName("TOTAL_THRESHOLD");
        double singleThreshold = (thresholdParam != null) ? thresholdParam.getThreshold() : 80.0;

        // 기준 점수 = (하루 임계치 * 데이터가 존재하는 일수)
        // -> 데이터가 없는 날은 0점처리 되므로 공정하게 비교하기 위해 데이터 수만큼만 곱함
        int dataCount = current.isEmpty() ? 1 : current.size();
        int totalThreshold = (int) (singleThreshold * dataCount);

        // C. 이전 기간 대비 변화 (평균 점수 비교)
        double currentAvg = current.stream().mapToInt(Risk::getScore).average().orElse(0.0);
        double prevAvg = prev.stream().mapToInt(Risk::getScore).average().orElse(0.0);
        double change = currentAvg - prevAvg;

        // D. 메시지 생성
        String statusMsg = (totalScore > totalThreshold)
                ? "주의: 누적 위험도가 기준치보다 높습니다."
                : "양호: 관리가 잘 되고 있습니다.";

        String trendMsg;
        if (change > 0) trendMsg = "지난 기간보다 위험도가 증가했습니다. (" + String.format("%.1f", change) + "점 상승)";
        else if (change < 0) trendMsg = "지난 기간보다 상태가 호전되었습니다. (" + String.format("%.1f", Math.abs(change)) + "점 감소)";
        else trendMsg = "지난 기간과 비슷한 상태입니다.";

        return ReportResponseDto.builder()
                .periodType(period)
                .totalRiskScore(totalScore)
                .totalThresholdScore(totalThreshold)
                .riskStatus(statusMsg)
                .alertCount(alertCount)
                .averageScore(currentAvg)
                .scoreChange(change)
                .trendMessage(trendMsg)
                .build();
    }
}