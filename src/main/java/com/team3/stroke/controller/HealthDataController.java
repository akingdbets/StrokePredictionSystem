package com.team3.stroke.controller;

import com.team3.stroke.domain.Risk;
import com.team3.stroke.service.RiskManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.team3.stroke.dto.HealthInputRequest; // 👈 방금 만든 파일 import

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthDataController {

    private final RiskManager riskManager;

    @PostMapping("/input")
    public String inputData(@RequestBody HealthInputRequest request) {
        Risk result = riskManager.processHealthDataInput(request);

        if (result.isThresholdExceeded()) {
            return "경고! 위험도가 높습니다. 현재 점수: " + result.getScore();
        } else {
            return "데이터 저장 완료. 현재 점수: " + result.getScore();
        }
    }
}