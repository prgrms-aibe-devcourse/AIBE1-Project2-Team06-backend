package com.eum.project;

import com.eum.project.model.entity.ProjectEntity;
import com.eum.project.model.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProjectRepository projectRepository) {
        return args -> {
            ProjectEntity project = ProjectEntity.of(
                    1L, // userId
                    "AI 팀원 모집합니다!", // title
                    "AI 프로젝트 함께 하실 분 구합니다. 열정 가득한 분 환영합니다!", // content
                    ProjectEntity.RecruitType.PROJECT,
                    4, // 모집 인원
                    ProjectEntity.ProgressMethod.ALL,
                    ProjectEntity.Period.MONTH_3,
                    LocalDate.of(2025, 5, 30), // 마감일
                    "KAKAO",
                    "https://www.kakaocorp.com/page/detail/10811"
            );

            projectRepository.save(project);

            System.out.println("✅ 샘플 프로젝트가 저장되었습니다!");
        };
    }

//    @Bean
//    CommandLineRunner initDatabaseTech(TechStackRepository techStackRepository) {
//        return args -> {
//             TechStackEntity techStackEntity = TechStackEntity.of(
//                    "Spring"
//            );
//             techStackRepository.save(techStackEntity);
//            log.info("✅ 샘플 프로젝트가 저장되었습니다!");
//        };
//    }

//    @Bean
//    CommandLineRunner initDatabaseTech(TechStackRepository techStackRepository) {
//        return args -> {
//            List<TechStackEntity> techStacks = List.of(
//                    TechStackEntity.of("Spring"),
//                    TechStackEntity.of("React"),
//                    TechStackEntity.of("MySQL"),
//                    TechStackEntity.of("JPA")
//            );
//
//            techStackRepository.saveAll(techStacks);
//            log.info("✅ 샘플 기술 스택이 저장되었습니다!");
//        };
//    }

}
