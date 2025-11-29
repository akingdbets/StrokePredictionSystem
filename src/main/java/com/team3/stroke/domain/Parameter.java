package com.team3.stroke.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Parameter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String parameterName; // 규칙 이름 (예: "BP_HIGH")

    private double threshold;     // 기준값 (예: 140.0)
    private int score;            // 부여할 점수 (패키지 다이어그램의 weightValue 역할)

    public Parameter(String parameterName, double threshold, int score) {
        this.parameterName = parameterName;
        this.threshold = threshold;
        this.score = score;
    }
}