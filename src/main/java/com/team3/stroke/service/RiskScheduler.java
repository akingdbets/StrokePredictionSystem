package com.team3.stroke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiskScheduler {

    private final RiskManager riskManager;

    // 매일 자정 (00:00:00)에 실행
    // cron 표현식: 초 분 시 일 월 요일
//    @Scheduled(cron = "0 0 0 * * *")
//    public void runMidnightCalculation() {
//        riskManager.calculateAllPatientsRisk();
//    }

     //(테스트용) 10초마다 실행 (제대로 도는지 확인하고 싶으면 주석 풀고 테스트)
     @Scheduled(fixedRate = 10000)
     public void testSchedule() {
        System.out.println("테스트 스케줄러 실행");
        riskManager.calculateAllPatientsRisk();
     }
}