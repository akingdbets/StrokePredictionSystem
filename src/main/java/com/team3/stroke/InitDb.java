package com.team3.stroke;

import com.team3.stroke.domain.Parameter; // 변경됨
import com.team3.stroke.domain.Patient;
import com.team3.stroke.repository.ParameterRepository; // 변경됨
import com.team3.stroke.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final PatientRepository patientRepository;
    private final ParameterRepository parameterRepository; // 변경됨

    @PostConstruct
    public void init() {
        // 1. 환자 초기화
        if (patientRepository.count() == 0) {
            Patient p1 = new Patient("홍길동");
            patientRepository.save(p1);
        }

        // 2. 파라미터(Parameter) 초기화
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