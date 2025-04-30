package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String positionName;

    public static PositionEntity of(
            String positionName
    ){
        PositionEntity position = new PositionEntity();
        position.positionName = positionName;
        return position;
    }
}
