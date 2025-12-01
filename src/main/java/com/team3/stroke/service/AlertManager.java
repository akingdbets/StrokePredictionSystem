package com.team3.stroke.service;

import com.team3.stroke.domain.Risk; //Risk 클래스 가져오기
import org.springframework.stereotype.Service;

@Service
public class AlertManager {

    // 수정된 부분: String이 아니라 'Risk' 객체를 통째로 받습니다.
    public void checkRiskAndAlert(Risk risk) {
        String level = risk.getRiskLevel(); // "DANGER", "WARNING", "NORMAL"
        int score = risk.getScore();

        System.out.println("\n[시스템] 위험도 분석 결과 수신 (점수: " + score + ")");

        if ("DANGER".equals(level)) {
            System.out.println("🚨 [긴급 알림] 뇌졸중 재발 '위험(DANGER)' 단계입니다!");
            System.out.println("   >> 조치: 주치의에게 환자 데이터를 즉시 전송합니다.");
            System.out.println("   >> 조치: 보호자에게 비상 연락 문자를 발송합니다.");

        } else if ("WARNING".equals(level)) {
            System.out.println("⚠️ [주의 알림] '주의(WARNING)' 단계입니다.");
            System.out.println("   >> 조치: 생활 습관 점검이 필요합니다.");

        } else {
            System.out.println("✅ [알림 없음] '정상(NORMAL)' 단계입니다. 현재 상태를 유지하세요.");
        }
    }
}