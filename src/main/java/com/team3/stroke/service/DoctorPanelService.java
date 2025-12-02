package com.team3.stroke.service;

import com.team3.stroke.domain.HealthData;
import com.team3.stroke.domain.Patient;
import com.team3.stroke.domain.Report;
import com.team3.stroke.domain.Risk;
import com.team3.stroke.dto.PatientPanelDto;
import com.team3.stroke.dto.PatientReportDetailDto;
import com.team3.stroke.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorPanelService {

    private final PatientRepository patientRepository;

    // 주치의 ID를 받아 환자 패널 리스트 반환
    public List<PatientPanelDto> getPatientPanel(Long doctorId, String sortType) {
        // 1. 주치의가 담당하는 모든 환자 조회
        List<Patient> patients = patientRepository.findByDoctorId(doctorId);

        // 2. Entity -> DTO 변환
        List<PatientPanelDto> panelList = patients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 3. 정렬 로직
        if ("RISK".equalsIgnoreCase(sortType)) {
            // [수정] int 타입이므로 comparingInt 사용 (점수 높은 순 내림차순)
            panelList.sort(Comparator.comparingInt(PatientPanelDto::getCurrentRiskScore).reversed());
        } else {
            // 기본: 이름순 정렬
            panelList.sort(Comparator.comparing(PatientPanelDto::getName));
        }

        return panelList;
    }

    // DTO 변환 메서드
    private PatientPanelDto convertToDto(Patient patient) {
        // [수정] 가장 최근 Risk 데이터 찾기
        // 1. calculatedAt -> calculatedDate 로 변경
        // 2. 값이 없으면 기본값 설정
        Risk latestRisk = patient.getRiskHistory().stream()
                .max(Comparator.comparing(Risk::getCalculatedDate)) // [변경 포인트]
                .orElse(null);

        int score = 0;
        boolean isHighRisk = false;

        if (latestRisk != null) {
            score = latestRisk.getScore(); // [변경 포인트] int 값 가져오기
            isHighRisk = latestRisk.isThresholdExceeded(); // [변경 포인트] 엔티티 필드 바로 사용
        }

        return PatientPanelDto.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .currentRiskScore(score)
                .isHighRisk(isHighRisk)
                .build();
    }


    //환자 상세 리포트 조회
    public PatientReportDetailDto getPatientReportDetail(Long patientId) {
        // 1. 환자 조회
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환자입니다."));

        // 2. 최신 위험도 (Risk)
        Risk latestRisk = patient.getRiskHistory().stream()
                .max(Comparator.comparing(Risk::getCalculatedDate))
                .orElse(null);

        // 3. [수정] 최신 건강 데이터 (HealthData) - recordDate 기준 정렬
        HealthData latestHealth = patient.getHealthDataList().stream()
                .max(Comparator.comparing(HealthData::getRecordDate)) // recordedAt -> recordDate
                .orElse(null);

        // 4. 최신 리포트/메모 (Report)
        Report latestReport = patient.getReports().stream()
                .max(Comparator.comparing(Report::getCreatedDate))
                .orElse(null);

        // 5. DTO 변환
        return PatientReportDetailDto.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .doctorName(patient.getDoctor() != null ? patient.getDoctor().getName() : "미배정")

                // 위험도 정보
                .currentRiskScore(latestRisk != null ? latestRisk.getScore() : 0)
                .riskLevel(latestRisk != null ? latestRisk.getRiskLevel() : "UNKNOWN")

                // [수정] 건강 데이터 매핑
                .recentSystolicBP(latestHealth != null ? latestHealth.getSystolicBP() : 0.0)
                .recentBloodSugar(latestHealth != null ? latestHealth.getBloodSugar() : 0.0f)
                .isSmoker(latestHealth != null && latestHealth.isSmokingStatus())
                .lastCheckupDate(latestHealth != null ? latestHealth.getRecordDate() : null)

                // 소견 정보
                .doctorMemo(latestReport != null ? latestReport.getDoctorMemo() : "등록된 소견이 없습니다.")
                .lastReportDate(latestReport != null ? latestReport.getCreatedDate() : null)
                .build();
    }

}