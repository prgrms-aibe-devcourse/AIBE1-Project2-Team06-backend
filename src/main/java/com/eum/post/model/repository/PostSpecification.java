package com.eum.post.model.repository;

import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostPosition;
import com.eum.post.model.entity.PostTechStack;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    // RecruitType으로 필터링
    public static Specification<Post> hasRecruitType(RecruitType recruitType) {
        return (root, query, criteriaBuilder) -> {
            if (recruitType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("recruitType"), recruitType);
        };
    }
    // ProgressMethod로 필터링
    public static Specification<Post> hasProgressMethod(ProgressMethod progressMethod) {
        return (root, query, criteriaBuilder) -> {
            if (progressMethod == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("progressMethod"), progressMethod);
        };
    }

    // CultureFit으로 필터링
    public static Specification<Post> hasCultureFit(CultureFit cultureFit) {
        return (root, query, criteriaBuilder) -> {
            if (cultureFit == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("cultureFit"), cultureFit);
        };
    }
    // Position으로 필터링 (단일 선택)
    public static Specification<Post> hasPosition(Long positionId) {
        return (root, query, criteriaBuilder) -> {
            if (positionId == null) {
                return null;
            }

            // 중복 제거
            query.distinct(true);

            // Join으로 PostPosition 연결
            Join<Post, PostPosition> postPositionJoin = root.join("postPositions", JoinType.LEFT);
            Join<PostPosition, Position> positionJoin = postPositionJoin.join("position", JoinType.LEFT);

            return criteriaBuilder.equal(positionJoin.get("id"), positionId);
        };
    }
    // TechStack으로 필터링 (여러 기술 스택 모두 포함 - AND 조건)
    public static Specification<Post> hasTechStacks(List<Long> techStackIds) {
        return (root, query, criteriaBuilder) -> {
            if (techStackIds == null || techStackIds.isEmpty()) {
                return null;
            }

            // 중복 제거
            query.distinct(true);

            // 각 기술 스택에 대한 서브쿼리 생성
            List<Predicate> predicates = new ArrayList<>();

            for (Long techStackId : techStackIds) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PostTechStack> subRoot = subquery.from(PostTechStack.class);

                Join<PostTechStack, Post> postJoin = subRoot.join("post");
                Join<PostTechStack, TechStack> techStackJoin = subRoot.join("techStack");

                subquery.select(postJoin.get("id"))
                        .where(criteriaBuilder.equal(techStackJoin.get("id"), techStackId));

                predicates.add(criteriaBuilder.in(root.get("id")).value(subquery));
            }

            // AND 조건 (모든 기술 스택을 포함하는 게시글)
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
