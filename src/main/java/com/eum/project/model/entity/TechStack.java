package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String stackName;

    public static TechStack of(
            String stackName
    ){
        TechStack techStackEntity = new TechStack();
        techStackEntity.stackName = stackName;
        return techStackEntity;
    }
}

