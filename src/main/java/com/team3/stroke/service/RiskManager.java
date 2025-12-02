package com.team3.stroke.service;

import com.team3.stroke.dto.HealthInputRequest;
import com.team3.stroke.domain.*;
import com.team3.stroke.repository.ParameterRepository; // 변경됨
import com.team3.stroke.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RiskManager {

    private final Patient Repository patientRepository;
    private final ParameterRepository parameterRepository; // 변경됨
    private final AlertManager alertManager;

    public Risk processHealthDataInput(HealthInputRequest request) {
        // 1. 환자 조회
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환자입니다."));

        // 2. 건강 데이터 저장
        HealthData data = new HealthData();
        data.setRecordDate(LocalDateTime.now());
        data.setSystolicBP(request.getSystolicBP());
        data.setSmokingStatus(request.isSmokingStatus());
        data.setBloodSugar(request.getBloodSugar());
        data.setBodyComposition(request.getBodyComposition());
        data.setActivityLevel(request.getActivityLevel());
        data.setMedicationTaken(request.isMedicationTaken());

        data.setPatient(patient);
        patient.getHealthDataList().add(data);

        // 3. 위험도 계산 (Parameter 사용)
        int score = calculateRiskLogic(data);

        // 임계치도 Parameter에서 가져옴
        Parameter thresholdParam = parameterRepository.findByParameterName("TOTAL_THRESHOLD");
        boolean exceeded = score >= thresholdParam.getThreshold();

        // 4. Risk 생성 및 저장
        Risk risk = new Risk();
        risk.setScore(score);
        risk.setRiskLevel(exceeded ? "WARNING" : "NORMAL");
        risk.setThresholdExceeded(exceeded);
        risk.setCalculatedDate(LocalDateTime.now());

        patient.addRisk(risk);

        // 5. 알림
        alertManager.checkRiskAndAlert(risk);

        return risk;
    }

    // ✅ ParameterRepository를 사용하는 로직으로 변경
    private int calculateRiskLogic(HealthData data) {
        int score = 0;

        // 1. 혈압
        Parameter bpParam = parameterRepository.findByParameterName("BP_HIGH");
        if (data.getSystolicBP() >= bpParam.getThreshold()) {
            score += bpParam.getScore();
        }

        // 2. 혈당
        Parameter sugarParam = parameterRepository.findByParameterName("SUGAR_HIGH");
        if (data.getBloodSugar() >= sugarParam.getThreshold()) {
            score += sugarParam.getScore();
        }

        // 3. 흡연
        Parameter smokingParam = parameterRepository.findByParameterName("SMOKING");
        if (data.isSmokingStatus()) {
            score += smokingParam.getScore();
        }

        // 4. 활동량 (낮을수록 위험)
        Parameter activityParam = parameterRepository.findByParameterName("ACTIVITY_LOW");
        if (data.getActivityLevel() <= activityParam.getThreshold()) {
            score += activityParam.getScore();
        }

        // 5. 약물 복용 (안 먹었으면 위험)
        Parameter medParam = parameterRepository.findByParameterName("MED_SKIP");
        if (!data.isMedicationTaken()) {
            score += medParam.getScore();
        }

        return score;
    }
}
