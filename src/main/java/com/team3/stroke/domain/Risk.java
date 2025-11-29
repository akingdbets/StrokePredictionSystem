package com.team3.stroke.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Risk {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;
    private String riskLevel; // "NORMAL", "WARNING", "DANGER"
    private boolean isThresholdExceeded;
    private LocalDateTime calculatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
}