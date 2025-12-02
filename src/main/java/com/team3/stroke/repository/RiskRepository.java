package com.team3.stroke.repository;

import com.team3.stroke.domain.Risk; // Patient 아님!
import org.springframework.data.jpa.repository.JpaRepository;

// <Risk, Long> 으로 수정해야 Risk 테이블에 저장됨
public interface RiskRepository extends JpaRepository<Risk, Long> {
}