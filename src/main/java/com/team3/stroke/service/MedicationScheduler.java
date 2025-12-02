package com.team3.stroke.service;

import com.team3.stroke.domain.Medication;
import com.team3.stroke.domain.Patient;
import com.team3.stroke.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicationScheduler {

    private final PatientRepository patientRepository;
    private final MedicationManager medicationManager;

    // 매 1분마다 실행 (초는 00초 기준)
    // 예: 14:00:00, 14:01:00, 14:02:00 ...
    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void checkAllMedications() {
        // 현재 서버 시간 (HH:mm 형태 문자열로 변환)
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        // (로그는 너무 자주 찍히면 시끄러우니 주석 처리하거나 필요할 때만 켭니다)
        System.out.println("⏰ [MedicationScheduler] 복약 시간 체크 중... (" + currentTime + ")");

        // 모든 환자를 조회
        List<Patient> allPatients = patientRepository.findAll();

        for (Patient patient : allPatients) {
            for (Medication med : patient.getMedications()) {
                medicationManager.checkMedicationTime(currentTime, med, patient.getName());
            }
        }
    }
}