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
public class Doctor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    private String name;
    private String specialty; // 전문분야

    // 주치의가 담당하는 환자 리스트
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients = new ArrayList<>();
}