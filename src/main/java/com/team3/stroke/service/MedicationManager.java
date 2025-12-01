package com.team3.stroke.service;

import com.team3.stroke.domain.Medication;
import com.team3.stroke.domain.Patient; // Patient도 필요하면 import
import org.springframework.stereotype.Service;

@Service
public class MedicationManager {

    // 환자 정보까지 같이 받아서 누구에게 알림을 보낼지 명확히 함
    public void checkMedicationTime(String currentTime, Medication medication, String patientName) {
        // 실제로는 스케줄러가 1분마다 이 함수를 호출한다고 가정
        if (currentTime.equals(medication.getScheduledTime())) {
            sendAlarm(medication.getMedicineName(), patientName);
        }
    }

    private void sendAlarm(String medicineName, String patientName) {
        System.out.println("\n💊 [복약 알림] " + patientName + "님, '" + medicineName + "' 복용 시간입니다!");
        System.out.println("   >> 알림이 스마트폰으로 전송되었습니다.");
    }
}