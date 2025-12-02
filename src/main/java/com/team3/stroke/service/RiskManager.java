package com.team3.stroke.service;

import com.team3.stroke.dto.HealthInputRequest;
import com.team3.stroke.domain.*;
import com.team3.stroke.repository.*; // 리포지토리 모두 포함
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RiskManager {

    private final PatientRepository patientRepository;
    private final ParameterRepository parameterRepository;
    private final RiskRepository riskRepository; // ✅ 추가됨
    private final AlertManager alertManager;

    // [1] 데이터 입력: 오직 "저장"만 수행 (계산 X)
    public void processHealthDataInput(HealthInputRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환자입니다."));

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
        // Transactional에 의해 자동 저장됨
    }

    // [2] 일괄 계산: 스케줄러가 호출할 메서드 (여기서 계산 & 알림)
    public void calculateAllPatientsRisk() {
        List<Patient> allPatients = patientRepository.findAll();

        for (Patient patient : allPatients) {
            // 가장 최근 데이터 1개만 가져와서 분석
            HealthData latestData = patient.getHealthDataList().stream()
                    .max(Comparator.comparing(HealthData::getRecordDate))
                    .orElse(null);

            if (latestData != null) {
                int score = calculateRiskLogic(latestData);

                Parameter thresholdParam = parameterRepository.findByParameterName("TOTAL_THRESHOLD");
                boolean exceeded = score >= thresholdParam.getThreshold();

                Risk risk = new Risk();
                risk.setScore(score);
                risk.setRiskLevel(exceeded ? "DANGER" : "NORMAL");
                risk.setThresholdExceeded(exceeded);
                risk.setCalculatedDate(LocalDateTime.now());

                patient.addRisk(risk);
                riskRepository.save(risk); // 이력 저장

                // 알림 전송 (AlertManager 위임)
                alertManager.checkRiskAndAlert(risk);
            }
        }
    }

    // (내부 계산 로직은 기존과 동일)
    private int calculateRiskLogic(HealthData data) {
        int score = 0;
        Parameter bpParam = parameterRepository.findByParameterName("BP_HIGH");
        if (data.getSystolicBP() >= bpParam.getThreshold()) score += bpParam.getScore();

        Parameter sugarParam = parameterRepository.findByParameterName("SUGAR_HIGH");
        if (data.getBloodSugar() >= sugarParam.getThreshold()) score += sugarParam.getScore();

        Parameter smokingParam = parameterRepository.findByParameterName("SMOKING");
        if (data.isSmokingStatus()) score += smokingParam.getScore();

        Parameter activityParam = parameterRepository.findByParameterName("ACTIVITY_LOW");
        if (data.getActivityLevel() <= activityParam.getThreshold()) score += activityParam.getScore();

        Parameter medParam = parameterRepository.findByParameterName("MED_SKIP");
        if (!data.isMedicationTaken()) score += medParam.getScore();

        return score;
    }
}