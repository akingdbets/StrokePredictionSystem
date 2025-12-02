package com.team3.stroke.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 1:N Composition 관계 (환자가 삭제되면 건강데이터/위험도 이력도 삭제됨)
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<HealthData> healthDataList = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Risk> riskHistory = new ArrayList<>();

    // 주치의와의 관계 설정 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // 리포트 리스트 추가
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    // 생성자
    public Patient(String name) {
        this.name = name;
    }

    // 연관관계 편의 메서드
    public void addRisk(Risk risk) {
        this.riskHistory.add(risk);
        risk.setPatient(this);
    }
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Medication> medications = new ArrayList<>();

    public void addMedication(Medication medication) {
        this.medications.add(medication);
        medication.setPatient(this);
    }
}