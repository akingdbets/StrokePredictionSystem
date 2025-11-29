package com.team3.stroke.repository;

import com.team3.stroke.domain.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    // 파라미터 이름으로 찾기
    Parameter findByParameterName(String parameterName);
}