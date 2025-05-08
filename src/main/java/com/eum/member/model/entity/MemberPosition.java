package com.eum.member.model.entity;

import com.eum.global.model.entity.Position;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Member_Position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    public static MemberPosition of(Member member, Position position) {
        MemberPosition memberPosition = new MemberPosition();
        memberPosition.member = member;
        memberPosition.position = position;
        return memberPosition;
    }
}
