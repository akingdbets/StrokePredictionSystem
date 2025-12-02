package com.team3.stroke.controller;

import com.team3.stroke.dto.ReportResponseDto;
import com.team3.stroke.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 주간/월간 리포트 조회 API
    // 사용법: GET /api/report/1?period=WEEKLY
    @GetMapping("/{patientId}")
    public ResponseEntity<ReportResponseDto> getReport(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "WEEKLY") String period) {

        ReportResponseDto report = reportService.generateReport(patientId, period);
        return ResponseEntity.ok(report);
    }
}