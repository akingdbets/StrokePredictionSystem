package com.team3.stroke.service;

import com.team3.stroke.domain.Medication;
import org.springframework.stereotype.Service;

@Service
public class MedicationManager {

    // 버튼을 눌렀을 때(시간 시뮬레이션) 호출됨
    public String checkMedicationTime(String simulatedTime, Medication medication, String patientName) {

        // 로직: 설정된 시간과 시뮬레이션 시간이 같은지 확인
        if (simulatedTime.equals(medication.getScheduledTime())) {

            // 메시지에 "식후 30분"이라는 멘트를 포함시켜서 의학적 느낌을 줌
            String message = "💊 " + patientName + "님! " + medication.getMedicineName() + " 복용 시간입니다. (식후 30분)";
            System.out.println(message);
            return message;
        }

        return null; // 시간 안 맞으면 아무것도 안 함
    }
}