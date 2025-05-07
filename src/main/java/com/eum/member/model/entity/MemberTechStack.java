package com.eum.member.model.entity;

import com.eum.global.model.entity.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Member_TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "techStack_id", nullable = false)
    private TechStack techStack;

    public static MemberTechStack of(Member member, TechStack techStack) {
        MemberTechStack memberTechStack = new MemberTechStack();
        memberTechStack.member = member;
        memberTechStack.techStack = techStack;
        return memberTechStack;
    }
}
