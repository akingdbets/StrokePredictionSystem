package com.team3.stroke.service;

import com.team3.stroke.domain.Patient;
import com.team3.stroke.domain.Risk;
import com.team3.stroke.dto.PatientPanelDto;
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
}