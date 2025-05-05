package com.eum.post.model.repository;

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

    // 키워드로 검색 필터링 (제목, 글 내용)
    public static Specification<Post> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            // 공백으로 검색어 분리
            String[] words = keyword.toLowerCase().trim().split("\\s+");
            List<Predicate> predicates = new ArrayList<>();

            for (String word : words) {
                if (!word.isEmpty()) {
                    String likePattern = "%" + word + "%";

                    // 대소문자 구분 없이 검색하기 위해 lower 함수 사용
                    Predicate wordPredicate = criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), likePattern)
                    );

                    predicates.add(wordPredicate);
                }
            }

            // AND 조건: 모든 단어가 포함되어야 함
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

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

    // Position으로 필터링 (단일 선택) - 서브쿼리 사용
    public static Specification<Post> hasPosition(Long positionId) {
        return (root, query, criteriaBuilder) -> {
            if (positionId == null) {
                return null;
            }

            // 중복 제거
            query.distinct(true);

            // 서브쿼리 생성
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<PostPosition> postPositionRoot = subquery.from(PostPosition.class);

            // postPosition 서브쿼리 조건 설정
            subquery.select(postPositionRoot.get("post").get("id"))
                    .where(criteriaBuilder.equal(postPositionRoot.get("position").get("id"), positionId));

            // 메인 쿼리에 서브쿼리 조건 적용
            return root.get("id").in(subquery);
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

                // 서브쿼리 조건 설정
                subquery.select(subRoot.get("post").get("id"))
                        .where(criteriaBuilder.equal(subRoot.get("techStack").get("id"), techStackId));

                // 메인 쿼리에 서브쿼리 조건 적용
                predicates.add(root.get("id").in(subquery));
            }

            // AND 조건 (모든 기술 스택을 포함하는 게시글)
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}