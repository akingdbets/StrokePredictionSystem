package com.team3.stroke.controller;

import com.team3.stroke.dto.PatientPanelDto;
import com.team3.stroke.dto.PatientReportDetailDto;
import com.team3.stroke.service.DoctorPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorPanelController {

    private final DoctorPanelService doctorPanelService;

    // 환자 패널 조회 API
    // 예시: GET /api/doctor/1/panel?sort=RISK
    @GetMapping("/{doctorId}/panel")
    public ResponseEntity<List<PatientPanelDto>> viewPanel(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "NAME") String sort) { // sort 파라미터가 없으면 이름순

        List<PatientPanelDto> panelData = doctorPanelService.getPatientPanel(doctorId, sort);
        return ResponseEntity.ok(panelData);
    }

    // [추가] 특정 환자 상세 리포트 조회 API
    // GET /api/doctor/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientReportDetailDto> viewPatientDetail(@PathVariable Long patientId) {
        PatientReportDetailDto reportDetail = doctorPanelService.getPatientReportDetail(patientId);
        return ResponseEntity.ok(reportDetail);
    }
}