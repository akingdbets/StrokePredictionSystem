package com.team3.stroke.repository;

import com.team3.stroke.domain.HealthData; // Patient 아님!
import org.springframework.data.jpa.repository.JpaRepository;

// <HealthData, Long> 으로 수정해야 HealthData 테이블에 저장됨
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
}