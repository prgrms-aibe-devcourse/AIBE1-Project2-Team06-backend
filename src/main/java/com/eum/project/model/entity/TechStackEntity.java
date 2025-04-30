package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String stackName;

    public static TechStackEntity of(
            String stackName
    ){
        TechStackEntity techStackEntity = new TechStackEntity();
        techStackEntity.stackName = stackName;
        return techStackEntity;
    }
}

