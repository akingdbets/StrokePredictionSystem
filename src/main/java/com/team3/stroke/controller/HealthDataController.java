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
        // 이제 반환값이 void이므로 받지 않음
        riskManager.processHealthDataInput(request);

        return "데이터가 성공적으로 저장되었습니다. 위험도 분석은 자정에 진행됩니다.";
    }
}