package com.team3.stroke;

import com.team3.stroke.domain.Medication;
import com.team3.stroke.domain.Risk; // Risk 가져오기
import com.team3.stroke.service.AlertManager;
import com.team3.stroke.service.MedicationManager;

public class SewonTestMain {
    public static void main(String[] args) {
        System.out.println("=== [세원] 통합 알림 기능 테스트 시작 ===");

        // 1. 매니저 준비
        AlertManager alertManager = new AlertManager();
        MedicationManager medManager = new MedicationManager();

        // -------------------------------------------------
        // [TEST 1] 위험 알림 테스트 (동윤님 코드와 연동 시뮬레이션)
        // -------------------------------------------------
        System.out.println("\n--- 1. 위험도 알림 테스트 ---");

        // 가짜 Risk 객체 생성 (동윤님 로직이 계산해줬다고 가정)
        Risk highRisk = new Risk();
        highRisk.setScore(85);
        highRisk.setRiskLevel("DANGER"); // 동윤님 코드 규칙

        Risk normalRisk = new Risk();
        normalRisk.setScore(20);
        normalRisk.setRiskLevel("NORMAL");

        // 매니저에게 객체 전달
        alertManager.checkRiskAndAlert(highRisk);  // 🚨 울려야 함
        alertManager.checkRiskAndAlert(normalRisk); // ✅ 떠야 함

        // -------------------------------------------------
        // [TEST 2] 복약 알림 테스트 (시간 경과 시뮬레이션)
        // -------------------------------------------------
        System.out.println("\n--- 2. 복약 알림 테스트 ---");

        Medication aspirin = new Medication("아스피린", "09:00");
        String patientName = "김세원";

        // 상황: 현재 시간이 08:00일 때
        System.out.println("[Time: 08:00]");
        medManager.checkMedicationTime("08:00", aspirin, patientName); // 아무 반응 없어야 함

        // 상황: 현재 시간이 09:00일 때 (일치!)
        System.out.println("[Time: 09:00]");
        medManager.checkMedicationTime("09:00", aspirin, patientName); // 💊 알림 떠야 함

        System.out.println("\n=== 테스트 종료 ===");
    }
}