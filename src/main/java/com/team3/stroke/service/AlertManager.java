package com.team3.stroke.service;

import com.team3.stroke.domain.Risk;
import org.springframework.stereotype.Service;

@Service
public class AlertManager {

    // 웹 프론트엔드에 결과를 돌려주기 위해 String을 반환하도록 수정했습니다.
    public String checkRiskAndAlert(Risk risk) {
        String level = risk.getRiskLevel(); // 주호님 코드에서 가져옴 ("DANGER" or "NORMAL")

        // 주호님 코드는 위험하면 "DANGER"로 저장함 -> 우리는 이걸 "WARNING" 상황으로 처리
        if ("DANGER".equals(level)) {
            System.out.println("🚨 [알림 서비스] 위험 수치 감지! 환자: " + risk.getPatient().getName());
            return "DANGER"; // 프론트엔드에게 "경고 띄워라"라고 신호 줌
        }

        // 정상이면
        else {
            System.out.println("✅ [알림 서비스] 정상 범위입니다.");
            return "NORMAL";
        }
    }
}