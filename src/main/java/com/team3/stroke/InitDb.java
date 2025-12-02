package com.team3.stroke;

import com.team3.stroke.domain.Doctor;   // 추가
import com.team3.stroke.domain.Parameter;
import com.team3.stroke.domain.Patient;
import com.team3.stroke.domain.Risk;     // 추가
import com.team3.stroke.repository.DoctorRepository; // 추가
import com.team3.stroke.repository.ParameterRepository;
import com.team3.stroke.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // 연관관계 설정을 위해 권장

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final PatientRepository patientRepository;
    private final ParameterRepository parameterRepository;
    private final DoctorRepository doctorRepository; // [추가] 주치의 저장소

    @PostConstruct
    @Transactional // [추가] 데이터 생성 시 트랜잭션 처리
    public void init() {

        // 0. 주치의 초기화 (없으면 생성)
        Doctor doctor = null;
        if (doctorRepository.count() == 0) {
            doctor = new Doctor();
            doctor.setName("김닥터");
            doctor.setSpecialty("신경과");
            doctorRepository.save(doctor);
            System.out.println("✅ [InitDb] Doctor(주치의: 김닥터) 생성 완료! ID: " + doctor.getId());
        } else {
            // 이미 있으면 첫 번째 의사 가져오기 (테스트용)
            doctor = doctorRepository.findAll().get(0);
        }

        // 1. 환자 초기화
        if (patientRepository.count() == 0) {

            // --- 환자 1: 홍길동 (고위험군, 90점) ---
            Patient p1 = new Patient("홍길동");
            p1.setDoctor(doctor); // 주치의 연결

            Risk r1 = new Risk();
            r1.setScore(90);
            r1.setRiskLevel("DANGER");
            r1.setThresholdExceeded(true); // 임계치 초과
            r1.setCalculatedDate(LocalDateTime.now());

            p1.addRisk(r1); // 편의 메서드로 양방향 연결
            patientRepository.save(p1);

            Risk r_yesterday = new Risk();
            r_yesterday.setScore(85);
            r_yesterday.setThresholdExceeded(true);
            r_yesterday.setCalculatedDate(LocalDateTime.now().minusDays(1));
            p1.addRisk(r_yesterday);

            // 2. 8일 전 (저위험 - 지난주 비교용)
            Risk r_lastWeek = new Risk();
            r_lastWeek.setScore(50);
            r_lastWeek.setThresholdExceeded(false);
            r_lastWeek.setCalculatedDate(LocalDateTime.now().minusDays(8));
            p1.addRisk(r_lastWeek);

            patientRepository.save(p1);
            System.out.println("✅ [InitDb] 홍길동 과거 이력 데이터 추가 완료");

            // --- 환자 2: 이순신 (저위험군, 40점) ---
            Patient p2 = new Patient("이순신");
            p2.setDoctor(doctor);

            Risk r2 = new Risk();
            r2.setScore(40);
            r2.setRiskLevel("NORMAL");
            r2.setThresholdExceeded(false); // 임계치 미만
            r2.setCalculatedDate(LocalDateTime.now());

            p2.addRisk(r2);
            patientRepository.save(p2);

            System.out.println("✅ [InitDb] Patient(홍길동:고위험, 이순신:저위험) 및 Risk 데이터 생성 완료!");
        }

        // 2. 파라미터(Parameter) 초기화 (기존 코드 유지)
        if (parameterRepository.count() == 0) {
            parameterRepository.save(new Parameter("BP_HIGH", 140.0, 40));
            parameterRepository.save(new Parameter("SUGAR_HIGH", 126.0, 20));
            parameterRepository.save(new Parameter("SMOKING", 1.0, 20));
            parameterRepository.save(new Parameter("ACTIVITY_LOW", 2.0, 10));
            parameterRepository.save(new Parameter("MED_SKIP", 0.0, 10));
            parameterRepository.save(new Parameter("TOTAL_THRESHOLD", 80.0, 0));

            System.out.println("✅ [InitDb] Parameter(위험도 규칙) 세팅 완료!");
        }
    }
}