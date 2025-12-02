package com.team3.stroke;

import com.team3.stroke.domain.*;
import com.team3.stroke.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ParameterRepository parameterRepository;

    @PostConstruct
    @Transactional
    public void init() {

        // 1. 주치의(Doctor) 초기화
        Doctor doctor = null;
        if (doctorRepository.count() == 0) {
            doctor = new Doctor();
            doctor.setName("김닥터");
            doctor.setSpecialty("신경과");
            doctorRepository.save(doctor);
            System.out.println("✅ [InitDb] 주치의(김닥터) 생성 완료");
        } else {
            doctor = doctorRepository.findAll().get(0);
        }

        // 2. 환자(Patient) 및 상세 데이터 초기화
        if (patientRepository.count() == 0) {

            // ==========================================
            // CASE 1: 고위험 환자 (홍길동)
            // ==========================================
            Patient p1 = new Patient("홍길동");
            p1.setDoctor(doctor); // 주치의 연결

            // (1) 위험도 (Risk) 데이터 생성
            Risk r1 = new Risk();
            r1.setScore(90);                 // 점수 (int)
            r1.setRiskLevel("DANGER");       // 위험 단계
            r1.setThresholdExceeded(true);   // 임계치 초과 여부
            r1.setCalculatedDate(LocalDateTime.now()); // 산출 일시

            p1.addRisk(r1); // 환자와 연결

            // (2) 건강 데이터 (HealthData) 생성 - [수정된 필드명 반영]
            HealthData h1 = new HealthData();
            h1.setSystolicBP(155.0);         // 수축기 혈압 (double)
            h1.setBloodSugar(145.5f);        // 혈당 (float)
            h1.setSmokingStatus(true);       // 흡연 여부 (boolean)
            h1.setBodyComposition("비만");    // 체성분
            h1.setActivityLevel(1);          // 활동량 (낮음)
            h1.setMedicationTaken(false);    // 약물 복용 여부 (미복용)
            h1.setRecordDate(LocalDateTime.now().minusDays(1)); // 기록 일시 (어제)

            h1.setPatient(p1);
            p1.getHealthDataList().add(h1);

            // (3) 주치의 리포트(메모) 생성
            Report rep1 = new Report();
            rep1.setDoctorMemo("혈압과 혈당 수치가 모두 위험 수준입니다. 정밀 검사가 필요합니다.");
            rep1.setCreatedDate(LocalDateTime.now().minusWeeks(1)); // 1주일 전 메모

            rep1.setPatient(p1);
            p1.getReports().add(rep1);

            patientRepository.save(p1);

            // ==========================================
            // CASE 2: 저위험 환자 (이순신)
            // ==========================================
            Patient p2 = new Patient("이순신");
            p2.setDoctor(doctor);

            // (1) 위험도 (Risk)
            Risk r2 = new Risk();
            r2.setScore(40);
            r2.setRiskLevel("NORMAL");
            r2.setThresholdExceeded(false);
            r2.setCalculatedDate(LocalDateTime.now());

            p2.addRisk(r2);

            // (2) 건강 데이터 (HealthData)
            HealthData h2 = new HealthData();
            h2.setSystolicBP(118.0);
            h2.setBloodSugar(95.0f);
            h2.setSmokingStatus(false);
            h2.setBodyComposition("정상");
            h2.setActivityLevel(3);          // 활동량 (보통)
            h2.setMedicationTaken(true);     // 약물 복용 (복용함)
            h2.setRecordDate(LocalDateTime.now()); // 오늘

            h2.setPatient(p2);
            p2.getHealthDataList().add(h2);

            patientRepository.save(p2);

            System.out.println("✅ [InitDb] 환자(홍길동, 이순신) 및 상세 데이터 생성 완료");
        }

        // 3. 시스템 파라미터(Parameter) 초기화
        if (parameterRepository.count() == 0) {
            parameterRepository.save(new Parameter("BP_HIGH", 140.0, 40));
            parameterRepository.save(new Parameter("SUGAR_HIGH", 126.0, 20));
            parameterRepository.save(new Parameter("SMOKING", 1.0, 20));
            parameterRepository.save(new Parameter("ACTIVITY_LOW", 2.0, 10));
            parameterRepository.save(new Parameter("MED_SKIP", 0.0, 10));
            parameterRepository.save(new Parameter("TOTAL_THRESHOLD", 80.0, 0));

            System.out.println("✅ [InitDb] 위험도 산출 파라미터 생성 완료");
        }
    }
}