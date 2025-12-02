package com.team3.stroke.service;

import com.team3.stroke.dto.HealthInputRequest;
import com.team3.stroke.domain.*;
import com.team3.stroke.repository.ParameterRepository;
import com.team3.stroke.repository.PatientRepository;
import com.team3.stroke.repository.RiskRepository; // 추가
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
    private final RiskRepository riskRepository; // 추가
    private final AlertManager alertManager;

    // [수정됨] 1. 데이터 입력 시에는 '저장'만 합니다. (계산 X)
    public void processHealthDataInput(HealthInputRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자 없음"));

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
        // patientRepository.save(patient); // Transactional 때문에 자동 저장됨
        System.out.println("✅ 데이터 저장 완료 (위험도 계산은 자정에 실행됩니다)");
    }

    // [신규] 2. 자정에 실행될 '일괄 계산' 로직
    public void calculateAllPatientsRisk() {
        System.out.println("🕛 [Scheduler] 자정 위험도 일괄 계산 시작...");

        List<Patient> allPatients = patientRepository.findAll();

        for (Patient patient : allPatients) {
            // 가장 최근 건강 데이터 가져오기 (데이터가 없으면 건너뜀)
            HealthData latestData = patient.getHealthDataList().stream()
                    .max(Comparator.comparing(HealthData::getRecordDate))
                    .orElse(null);

            if (latestData != null) {
                // 여기서 계산 수행
                int score = calculateRiskLogic(latestData);

                Parameter thresholdParam = parameterRepository.findByParameterName("TOTAL_THRESHOLD");
                boolean exceeded = score >= thresholdParam.getThreshold();

                Risk risk = new Risk();
                risk.setScore(score);
                risk.setRiskLevel(exceeded ? "DANGER" : "NORMAL");
                risk.setThresholdExceeded(exceeded);
                risk.setCalculatedDate(LocalDateTime.now());

                patient.addRisk(risk);
                riskRepository.save(risk); // 명시적 저장

                // 알림 전송 (자정이므로 문자나 조용한 알림으로 보내는 게 좋음)
                alertManager.checkRiskAndAlert(risk);
            }
        }
        System.out.println("🕛 [Scheduler] 일괄 계산 완료.");
    }

    private int calculateRiskLogic(HealthData data) {
        // (기존 계산 로직 그대로 유지...)
        int score = 0;
        Parameter bpParam = parameterRepository.findByParameterName("BP_HIGH");
        if (data.getSystolicBP() >= bpParam.getThreshold()) score += bpParam.getScore();
        // ... 나머지 로직 ...
        return score;
    }
}