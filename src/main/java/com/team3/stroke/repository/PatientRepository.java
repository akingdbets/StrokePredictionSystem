package com.team3.stroke.repository;

import com.team3.stroke.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // 기본 CRUD 기능 자동 제공
}