package com.team3.stroke.repository;

import com.team3.stroke.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

// Doctor 엔티티의 ID 타입이 Long이므로 JpaRepository<Doctor, Long>을 상속받습니다.
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // 기본적으로 save(), findById(), findAll() 등의 메서드가 자동으로 제공됩니다.
}