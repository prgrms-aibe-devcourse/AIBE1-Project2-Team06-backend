INSERT INTO positions (id, name)
VALUES (1, '프런트엔드');
INSERT INTO positions (id, name)
VALUES (2, '백엔드');
INSERT INTO positions (id, name)
VALUES (3, '디자이너');
INSERT INTO positions (id, name)
VALUES (4, 'IOS');
INSERT INTO positions (id, name)
VALUES (5, '안드로이드');
INSERT INTO positions (id, name)
VALUES (6, '데브옵스');
INSERT INTO positions (id, name)
VALUES (7, 'PM');
INSERT INTO positions (id, name)
VALUES (8, '기획자');
INSERT INTO positions (id, name)
VALUES (9, '마케터');

INSERT INTO tech_stack (name)
VALUES ('JavaScript');
INSERT INTO tech_stack (name)
VALUES ('TypeScript');
INSERT INTO tech_stack (name)
VALUES ('React');
INSERT INTO tech_stack (name)
VALUES ('Vue');
INSERT INTO tech_stack (name)
VALUES ('Nodejs');
INSERT INTO tech_stack (name)
VALUES ('Spring');
INSERT INTO tech_stack (name)
VALUES ('Java');
INSERT INTO tech_stack (name)
VALUES ('Nextjs');
INSERT INTO tech_stack (name)
VALUES ('Nestjs');
INSERT INTO tech_stack (name)
VALUES ('Express');
INSERT INTO tech_stack (name)
VALUES ('Go');
INSERT INTO tech_stack (name)
VALUES ('C');
INSERT INTO tech_stack (name)
VALUES ('Python');
INSERT INTO tech_stack (name)
VALUES ('Django');
INSERT INTO tech_stack (name)
VALUES ('Swift');
INSERT INTO tech_stack (name)
VALUES ('Kotlin');
INSERT INTO tech_stack (name)
VALUES ('MySQL');
INSERT INTO tech_stack (name)
VALUES ('MongoDB');
INSERT INTO tech_stack (name)
VALUES ('php');
INSERT INTO tech_stack (name)
VALUES ('GraphQL');
INSERT INTO tech_stack (name)
VALUES ('Firebase');
INSERT INTO tech_stack (name)
VALUES ('ReactNative');
INSERT INTO tech_stack (name)
VALUES ('Unity');
INSERT INTO tech_stack (name)
VALUES ('Flutter');
INSERT INTO tech_stack (name)
VALUES ('AWS');
INSERT INTO tech_stack (name)
VALUES ('Kubernetes');
INSERT INTO tech_stack (name)
VALUES ('Docker');
INSERT INTO tech_stack (name)
VALUES ('Git');
INSERT INTO tech_stack (name)
VALUES ('Figma');
INSERT INTO tech_stack (name)
VALUES ('Zeplin');
INSERT INTO tech_stack (name)
VALUES ('Jest');
INSERT INTO tech_stack (name)
VALUES ('Svelte');

-- Member 관련 Mock Data (UUIDv4 문자열 저장, H2 호환)
INSERT INTO member (public_id,
                    auth_id,
                    provider,
                    nickname,
                    profile_image_url,
                    career,
                    short_description,
                    created_at,
                    updated_at)
VALUES (RANDOM_UUID(), 'kakao_1001', 'kakao', '김시용',
        'https://picsum.photos/id/1/200', '1 year', '프론트엔드 주니어', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1002', 'kakao', '서주원',
        'https://picsum.photos/id/2/200', '3 years', '백엔드 개발자', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1003', 'kakao', '송창욱',
        'https://picsum.photos/id/3/200', '2 years', 'IOS 앱 개발자', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1004', 'kakao', '은나현',
        'https://picsum.photos/id/4/200', '4 years', '기획 전문가', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1005', 'kakao', '장정명',
        'https://picsum.photos/id/5/200', '5 years', 'DevOps 엔지니어', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1006', 'kakao', '유지민',
        'https://picsum.photos/id/6/200', '1 year', '마케팅 주니어', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1007', 'kakao', '김민정',
        'https://picsum.photos/id/7/200', '6 years', 'PM / 기획 겸직', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1008', 'kakao', '장원영',
        'https://picsum.photos/id/8/200', '3 years', 'React Native 개발자', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1009', 'kakao', '이지은',
        'https://picsum.photos/id/9/200', '2 years', 'Unity 기반 게임 개발자', NOW(), NOW()),
       (RANDOM_UUID(), 'kakao_1010', 'kakao', '고윤정',
        'https://picsum.photos/id/10/200', '4 years', '풀스택 개발자', NOW(), NOW());

INSERT INTO member_position (member_id, position_id)
VALUES (1, 1), -- 프런트엔드
       (2, 2), -- 백엔드
       (3, 4), -- IOS
       (4, 8), -- 기획자
       (5, 6), -- 데브옵스
       (6, 9), -- 마케터
       (7, 7), -- PM
       (8, 1), -- 프런트엔드 (React Native)
       (9, 5), -- 안드로이드 (Unity 기반 게임)
       (10, 2); -- 백엔드

INSERT INTO member_tech_stack (member_id, tech_stack_id)
VALUES (1, 1),
       (1, 3),
       (1, 8),  -- JavaScript, React, Next.js
       (2, 6),
       (2, 7),
       (2, 17), -- Spring, Java, MySQL
       (3, 15),
       (3, 17),
       (3, 25), -- Swift, MySQL, AWS
       (4, 29),
       (4, 30), -- Figma, Zeplin
       (5, 25),
       (5, 26),
       (5, 27), -- AWS, Kubernetes, Docker
       (6, 28),
       (6, 24), -- Git, Flutter
       (7, 20),
       (7, 29), -- GraphQL, Figma
       (8, 21),
       (8, 3),
       (8, 8),  -- ReactNative, React, Nextjs
       (9, 22),
       (9, 11),
       (9, 28), -- Unity, Go, Git
       (10, 2),
       (10, 5),
       (10, 6),
       (10, 17);
-- TypeScript, Nodejs, Spring, MySQL

-- 1. Post 데이터 생성 (첫 번째 세트: 10개 게시글)
INSERT INTO post (user_id, title, content, recruit_type, recruit_member, progress_method, period, deadline, link_type,
                  link, status, created_at, updated_at, culture_fit)
VALUES
-- user_id 1번 (김시용)의 게시글
(1, '리액트 기반 웹 프로젝트 팀원 모집합니다', '안녕하세요! 리액트를 활용한 웹 서비스를 개발할 팀원을 모집합니다. 함께 성장할 분들을 찾고 있어요.', 'PROJECT', 4, 'ONLINE',
 'MONTH_3', '2025-08-15', 'KAKAO', 'https://open.kakao.com/example1', 'RECRUITING', NOW(), NOW(), 'COMMUNICATIVE'),
(1, '타입스크립트 스터디 모집', '타입스크립트 기초부터 고급 기능까지 함께 공부해요. 주 2회 온라인 미팅 진행 예정입니다.', 'STUDY', 5, 'ONLINE', 'MONTH_1',
 '2025-06-20', 'KAKAO', 'https://open.kakao.com/example2', 'RECRUITING', NOW(), NOW(), 'AUTONOMOUS'),

-- user_id 2번 (서주원)의 게시글
(2, '스프링 백엔드 개발자 모집', '스프링부트와 JPA를 활용한 백엔드 프로젝트를 시작합니다. 관심 있는 분들 함께해요!', 'PROJECT', 3, 'ONLINE', 'MONTH_6',
 '2025-07-30', 'GOOGLE', 'https://meet.google.com/example1', 'RECRUITING', NOW(), NOW(), 'PLANNER'),
(2, '자바 스터디 그룹 참여자 모집', '자바 기본기부터 심화 내용까지 차근차근 공부할 스터디원을 모집합니다.', 'STUDY', 6, 'OFFLINE', 'MONTH_2', '2025-06-10',
 'KAKAO', 'https://open.kakao.com/example3', 'RECRUITING', NOW(), NOW(), 'DIRECTIVE'),

-- user_id 3번 (송창욱)의 게시글
(3, 'iOS 앱 개발 프로젝트 함께하실 분', 'Swift와 SwiftUI를 활용한 iOS 앱 개발 프로젝트입니다. 디자이너와 백엔드 개발자분도 환영해요!', 'PROJECT', 4, 'ALL',
 'MONTH_6', '2025-09-01', 'GOOGLE', 'https://meet.google.com/example2', 'RECRUITING', NOW(), NOW(), 'PRACTICAL'),

-- user_id 4번 (은나현)의 게시글
(4, '서비스 기획 스터디 모집합니다', '서비스 기획과 관련된 다양한 주제로 스터디를 진행할 예정입니다. 기획에 관심있는 분들 환영해요!', 'STUDY', 5, 'ALL', 'MONTH_1',
 '2025-07-01', 'KAKAO', 'https://open.kakao.com/example4', 'RECRUITING', NOW(), NOW(), 'HARMONY'),

-- user_id 5번 (장정명)의 게시글
(5, 'DevOps 관련 프로젝트 팀원 구합니다', 'Docker와 Kubernetes를 활용한 클라우드 환경 구축 프로젝트입니다. 함께 성장할 분들을 찾습니다.', 'PROJECT', 3, 'ONLINE',
 'MONTH_3', '2025-08-20', 'GOOGLE', 'https://meet.google.com/example3', 'RECRUITING', NOW(), NOW(), 'AUTONOMOUS'),

-- user_id 6번 (유지민)의 게시글
(6, '마케팅 전략 수립 스터디', '디지털 마케팅 전략 수립에 관한 스터디입니다. 실무 사례를 분석하고 토론할 예정입니다.', 'STUDY', 4, 'OFFLINE', 'MONTH_2', '2025-07-15',
 'KAKAO', 'https://open.kakao.com/example5', 'RECRUITING', NOW(), NOW(), 'COMMUNICATIVE'),

-- user_id 7번 (김민정)의 게시글
(7, '프로덕트 매니저 역량 강화 스터디', 'PM으로서 필요한 역량을 함께 키워나갈 스터디원을 모집합니다. 현업 PM들의 경험 공유도 있을 예정입니다.', 'STUDY', 8, 'ALL', 'MONTH_3',
 '2025-08-05', 'GOOGLE', 'https://meet.google.com/example4', 'RECRUITING', NOW(), NOW(), 'HARMONY'),

-- user_id 8번 (장원영)의 게시글
(8, 'React Native 앱 개발 프로젝트', 'React Native를 활용한 크로스 플랫폼 앱 개발 프로젝트입니다. 디자이너와 백엔드 개발자도 모집합니다.', 'PROJECT', 5, 'ONLINE',
 'MONTH_6', '2025-09-15', 'KAKAO', 'https://open.kakao.com/example6', 'RECRUITING', NOW(), NOW(), 'PRACTICAL'),

-- user_id 9번 (이지은)의 게시글
(9, 'Unity 게임 개발 프로젝트 팀원 모집', 'Unity를 활용한 모바일 게임 개발 프로젝트입니다. 3D 모델링과 기획에 관심 있는 분들도 환영합니다!', 'PROJECT', 6, 'ALL',
 'MONTH_6', '2025-10-01', 'GOOGLE', 'https://meet.google.com/example5', 'RECRUITING', NOW(), NOW(), 'DIRECTIVE'),

-- user_id 10번 (고윤정)의 게시글
(10, '풀스택 개발 스터디 모집', 'React와 Node.js를 활용한 풀스택 개발을 함께 공부할 스터디원을 모집합니다. 실습 위주로 진행할 예정입니다.', 'STUDY', 6, 'ONLINE',
 'MONTH_3', '2025-07-25', 'KAKAO', 'https://open.kakao.com/example7', 'RECRUITING', NOW(), NOW(), 'PLANNER');

-- 2. 추가 게시글 20개 생성 (두 번째 세트)
INSERT INTO post (user_id, title, content, recruit_type, recruit_member, progress_method, period, deadline, link_type,
                  link, status, created_at, updated_at, culture_fit)
VALUES
-- 유저 1번 (김시용)의 추가 게시글
(1, '차세대 웹 기술 스터디 모집', 'WebAssembly, PWA 등 최신 웹 기술을 공부할 스터디원을 모집합니다. 주 1회 온라인 모임 예정입니다.', 'STUDY', 6, 'ONLINE',
 'MONTH_2', '2025-07-10', 'KAKAO', 'https://open.kakao.com/example8', 'RECRUITING', NOW(), NOW(), 'AUTONOMOUS'),
(1, '개인 포트폴리오 사이트 제작 함께할 분', '각자의 포트폴리오 사이트를 함께 제작하며 피드백을 주고받을 팀원을 찾습니다. HTML, CSS, JS 기본기가 있으신 분 환영합니다.', 'PROJECT', 3,
 'ONLINE', 'MONTH_1', '2025-06-30', 'GOOGLE', 'https://meet.google.com/example6', 'RECRUITING', NOW(), NOW(),
 'COMMUNICATIVE'),

-- 유저 2번 (서주원)의 추가 게시글
(2, '클라우드 아키텍처 설계 스터디', 'AWS와 Azure를 활용한 클라우드 아키텍처 설계 패턴을 공부하는 스터디입니다. 실무 사례 중심으로 진행됩니다.', 'STUDY', 5, 'ONLINE',
 'MONTH_3', '2025-08-01', 'KAKAO', 'https://open.kakao.com/example9', 'RECRUITING', NOW(), NOW(), 'PLANNER'),
(2, 'JPA/Hibernate 심화 스터디', 'JPA와 Hibernate의 고급 기능과 성능 최적화 기법을 함께 공부해요. ORM 기초 지식이 있으신 분들 환영합니다.', 'STUDY', 4, 'ALL',
 'MONTH_2', '2025-07-20', 'GOOGLE', 'https://meet.google.com/example7', 'RECRUITING', NOW(), NOW(), 'DIRECTIVE'),

-- 유저 3번 (송창욱)의 추가 게시글
(3, '신규 금융 앱 개발 프로젝트', '핀테크 관련 iOS 앱을 개발할 팀원을 모집합니다. UI/UX 디자이너와 백엔드 개발자도 환영합니다.', 'PROJECT', 5, 'ALL', 'MONTH_4',
 '2025-09-10', 'KAKAO', 'https://open.kakao.com/example10', 'RECRUITING', NOW(), NOW(), 'PRACTICAL'),
(3, 'Swift 언어 기초 스터디', 'Swift 프로그래밍 언어를 처음부터 체계적으로 공부하는 스터디입니다. 프로그래밍 경험이 있으면 좋지만, 초보자도 환영합니다.', 'STUDY', 8, 'ONLINE',
 'MONTH_2', '2025-07-15', 'GOOGLE', 'https://meet.google.com/example8', 'RECRUITING', NOW(), NOW(), 'HARMONY'),

-- 유저 4번 (은나현)의 추가 게시글
(4, 'UX 리서치 방법론 스터디', '사용자 경험 리서치 방법론을 공부하고 실습하는 스터디입니다. 실제 프로젝트에 적용할 수 있는 실용적인 내용으로 진행됩니다.', 'STUDY', 6, 'OFFLINE',
 'MONTH_2', '2025-07-25', 'KAKAO', 'https://open.kakao.com/example11', 'RECRUITING', NOW(), NOW(), 'COMMUNICATIVE'),
(4, '디지털 프로덕트 기획 워크숍', '디지털 프로덕트 기획 과정 전반을 함께 실습하는 워크숍입니다. 현업 기획자들의 노하우 공유도 있을 예정입니다.', 'PROJECT', 10, 'ALL', 'MONTH_1',
 '2025-06-30', 'GOOGLE', 'https://meet.google.com/example9', 'RECRUITING', NOW(), NOW(), 'HARMONY'),

-- 유저 5번 (장정명)의 추가 게시글
(5, '마이크로서비스 아키텍처 구현 프로젝트', 'Spring Cloud와 Docker를 활용한 마이크로서비스 아키텍처 구현 프로젝트입니다. CI/CD 파이프라인도 함께 구축할 예정입니다.', 'PROJECT',
 4, 'ONLINE', 'MONTH_4', '2025-09-05', 'KAKAO', 'https://open.kakao.com/example12', 'RECRUITING', NOW(), NOW(),
 'DIRECTIVE'),
(5, 'Kubernetes 실전 활용 스터디', 'Kubernetes를 실제 프로덕션 환경에서 활용하는 방법을 공부하는 스터디입니다. 기본 개념은 알고 계신 분들을 대상으로 합니다.', 'STUDY', 5,
 'ONLINE', 'MONTH_2', '2025-07-30', 'GOOGLE', 'https://meet.google.com/example10', 'RECRUITING', NOW(), NOW(),
 'PLANNER'),

-- 유저 6번 (유지민)의 추가 게시글
(6, '소셜미디어 마케팅 실전 프로젝트', '실제 브랜드를 대상으로 소셜미디어 마케팅 전략을 수립하고 실행해보는 프로젝트입니다. 포트폴리오 구축에 도움이 될 것입니다.', 'PROJECT', 6, 'ALL',
 'MONTH_3', '2025-08-15', 'KAKAO', 'https://open.kakao.com/example13', 'RECRUITING', NOW(), NOW(), 'COMMUNICATIVE'),
(6, '그로스 해킹 전략 연구 스터디', '데이터 기반의 그로스 해킹 전략을 연구하는 스터디입니다. 실제 사례 분석과 전략 수립 실습이 포함됩니다.', 'STUDY', 4, 'ONLINE', 'MONTH_2',
 '2025-07-20', 'GOOGLE', 'https://meet.google.com/example11', 'RECRUITING', NOW(), NOW(), 'AUTONOMOUS'),

-- 유저 7번 (김민정)의 추가 게시글
(7, '애자일 프로젝트 관리 워크숍', '애자일 방법론을 활용한 프로젝트 관리 실습 워크숍입니다. 스크럼, 칸반 등 다양한 방법론을 실제 적용해봅니다.', 'PROJECT', 8, 'OFFLINE',
 'MONTH_1', '2025-07-10', 'KAKAO', 'https://open.kakao.com/example14', 'RECRUITING', NOW(), NOW(), 'PLANNER'),
(7, '프로덕트 로드맵 구축 스터디', '효과적인 프로덕트 로드맵 구축 방법을 연구하는 스터디입니다. 현업 PM들과 함께 실제 사례를 분석합니다.', 'STUDY', 5, 'ALL', 'MONTH_2',
 '2025-08-01', 'GOOGLE', 'https://meet.google.com/example12', 'RECRUITING', NOW(), NOW(), 'HARMONY'),

-- 유저 8번 (장원영)의 추가 게시글
(8, '크로스 플랫폼 게임 개발 프로젝트', 'React Native와 Unity를 활용한 크로스 플랫폼 게임 개발 프로젝트입니다. 기획자와 디자이너도 환영합니다.', 'PROJECT', 6, 'ONLINE',
 'MONTH_5', '2025-09-20', 'KAKAO', 'https://open.kakao.com/example15', 'RECRUITING', NOW(), NOW(), 'PRACTICAL'),
(8, '모바일 앱 UI/UX 개선 스터디', '모바일 앱의 UI/UX를 분석하고 개선하는 방법을 연구하는 스터디입니다. 실제 앱을 리디자인하는 실습이 포함됩니다.', 'STUDY', 4, 'ALL',
 'MONTH_2', '2025-07-25', 'GOOGLE', 'https://meet.google.com/example13', 'RECRUITING', NOW(), NOW(), 'COMMUNICATIVE'),

-- 유저 9번 (이지은)의 추가 게시글
(9, 'AR/VR 게임 개발 프로젝트', 'Unity를 활용한 AR/VR 게임 개발 프로젝트입니다. 3D 모델링과 게임 기획에 관심 있는 분들도 환영합니다.', 'PROJECT', 5, 'ALL',
 'MONTH_6', '2025-10-15', 'KAKAO', 'https://open.kakao.com/example16', 'RECRUITING', NOW(), NOW(), 'AUTONOMOUS'),
(9, '게임 기획 포트폴리오 만들기', '게임 기획 문서와 프로토타입을 만들어 포트폴리오를 구축하는 스터디입니다. 게임 기획에 관심 있는 분들을 위한 모임입니다.', 'STUDY', 6, 'ONLINE',
 'MONTH_3', '2025-08-10', 'GOOGLE', 'https://meet.google.com/example14', 'RECRUITING', NOW(), NOW(), 'PLANNER'),

-- 유저 10번 (고윤정)의 추가 게시글
(10, 'GraphQL API 개발 프로젝트', 'GraphQL을 활용한 API 서버 개발 프로젝트입니다. React와 Node.js를 함께 사용할 예정입니다.', 'PROJECT', 4, 'ONLINE',
 'MONTH_3', '2025-08-30', 'KAKAO', 'https://open.kakao.com/example17', 'RECRUITING', NOW(), NOW(), 'DIRECTIVE'),
(10, '데이터 시각화 프로젝트', 'D3.js를 활용한 데이터 시각화 프로젝트입니다. 프론트엔드 개발자와 데이터 분석가를 모집합니다.', 'PROJECT', 5, 'ALL', 'MONTH_2',
 '2025-07-20', 'GOOGLE', 'https://meet.google.com/example15', 'RECRUITING', NOW(), NOW(), 'PRACTICAL');

-- 3. Post_Member: 게시글 작성자를 해당 게시글의 멤버로 추가 (is_owner = true)
INSERT INTO post_member (post_id, member_id, is_owner, created_at)
VALUES
-- 1-10번 게시글의 작성자
(1, 1, true, NOW()),   -- 게시글 1의 작성자 (김시용)
(2, 1, true, NOW()),   -- 게시글 2의 작성자 (김시용)
(3, 2, true, NOW()),   -- 게시글 3의 작성자 (서주원)
(4, 2, true, NOW()),   -- 게시글 4의 작성자 (서주원)
(5, 3, true, NOW()),   -- 게시글 5의 작성자 (송창욱)
(6, 4, true, NOW()),   -- 게시글 6의 작성자 (은나현)
(7, 5, true, NOW()),   -- 게시글 7의 작성자 (장정명)
(8, 6, true, NOW()),   -- 게시글 8의 작성자 (유지민)
(9, 7, true, NOW()),   -- 게시글 9의 작성자 (김민정)
(10, 8, true, NOW()),  -- 게시글 10의 작성자 (장원영)
(11, 9, true, NOW()),  -- 게시글 11의 작성자 (이지은)
(12, 10, true, NOW()), -- 게시글 12의 작성자 (고윤정)

-- 11-30번 게시글의 작성자
(13, 1, true, NOW()),  -- 게시글 13의 작성자 (김시용)
(14, 1, true, NOW()),  -- 게시글 14의 작성자 (김시용)
(15, 2, true, NOW()),  -- 게시글 15의 작성자 (서주원)
(16, 2, true, NOW()),  -- 게시글 16의 작성자 (서주원)
(17, 3, true, NOW()),  -- 게시글 17의 작성자 (송창욱)
(18, 3, true, NOW()),  -- 게시글 18의 작성자 (송창욱)
(19, 4, true, NOW()),  -- 게시글 19의 작성자 (은나현)
(20, 4, true, NOW()),  -- 게시글 20의 작성자 (은나현)
(21, 5, true, NOW()),  -- 게시글 21의 작성자 (장정명)
(22, 5, true, NOW()),  -- 게시글 22의 작성자 (장정명)
(23, 6, true, NOW()),  -- 게시글 23의 작성자 (유지민)
(24, 6, true, NOW()),  -- 게시글 24의 작성자 (유지민)
(25, 7, true, NOW()),  -- 게시글 25의 작성자 (김민정)
(26, 7, true, NOW()),  -- 게시글 26의 작성자 (김민정)
(27, 8, true, NOW()),  -- 게시글 27의 작성자 (장원영)
(28, 8, true, NOW()),  -- 게시글 28의 작성자 (장원영)
(29, 9, true, NOW()),  -- 게시글 29의 작성자 (이지은)
(30, 9, true, NOW()),  -- 게시글 30의 작성자 (이지은)
(31, 10, true, NOW()), -- 게시글 31의 작성자 (고윤정)
(32, 10, true, NOW());
-- 게시글 32의 작성자 (고윤정)

-- 4. PostMember - 다른 멤버들이 게시글에 참여 (is_owner = false)
INSERT INTO post_member (post_id, member_id, is_owner, created_at)
VALUES
-- 게시글 1~5에 참여한 멤버들
(1, 3, false, NOW()),   -- 송창욱이 김시용의 리액트 프로젝트에 참여
(1, 5, false, NOW()),   -- 장정명이 김시용의 리액트 프로젝트에 참여
(2, 4, false, NOW()),   -- 은나현이 김시용의 타입스크립트 스터디에 참여
(2, 7, false, NOW()),   -- 김민정이 김시용의 타입스크립트 스터디에 참여
(3, 1, false, NOW()),   -- 김시용이 서주원의 스프링 프로젝트에 참여
(3, 5, false, NOW()),   -- 장정명이 서주원의 스프링 프로젝트에 참여
(4, 7, false, NOW()),   -- 김민정이 서주원의 자바 스터디에 참여
(4, 10, false, NOW()),  -- 고윤정이 서주원의 자바 스터디에 참여
(5, 2, false, NOW()),   -- 서주원이 송창욱의 iOS 프로젝트에 참여
(5, 4, false, NOW()),   -- 은나현이 송창욱의 iOS 프로젝트에 참여

-- 게시글 6~10에 참여한 멤버들
(6, 7, false, NOW()),   -- 김민정이 은나현의 기획 스터디에 참여
(6, 9, false, NOW()),   -- 이지은이 은나현의 기획 스터디에 참여
(7, 2, false, NOW()),   -- 서주원이 장정명의 DevOps 프로젝트에 참여
(7, 10, false, NOW()),  -- 고윤정이 장정명의 DevOps 프로젝트에 참여
(8, 4, false, NOW()),   -- 은나현이 유지민의 마케팅 스터디에 참여
(8, 7, false, NOW()),   -- 김민정이 유지민의 마케팅 스터디에 참여
(9, 4, false, NOW()),   -- 은나현이 김민정의 PM 스터디에 참여
(9, 8, false, NOW()),   -- 장원영이 김민정의 PM 스터디에 참여
(10, 1, false, NOW()),  -- 김시용이 장원영의 React Native 프로젝트에 참여
(10, 3, false, NOW()),  -- 송창욱이 장원영의 React Native 프로젝트에 참여

-- 게시글 11~15에 참여한 멤버들
(11, 3, false, NOW()),  -- 송창욱이 이지은의 Unity 프로젝트에 참여
(11, 8, false, NOW()),  -- 장원영이 이지은의 Unity 프로젝트에 참여
(12, 1, false, NOW()),  -- 김시용이 고윤정의 풀스택 스터디에 참여
(12, 2, false, NOW()),  -- 서주원이 고윤정의 풀스택 스터디에 참여
(13, 3, false, NOW()),  -- 송창욱이 김시용의 웹 기술 스터디에 참여
(13, 5, false, NOW()),  -- 장정명이 김시용의 웹 기술 스터디에 참여
(13, 10, false, NOW()), -- 고윤정이 김시용의 웹 기술 스터디에 참여
(14, 4, false, NOW()),  -- 은나현이 김시용의 포트폴리오 프로젝트에 참여
(15, 1, false, NOW()),  -- 김시용이 서주원의 클라우드 스터디에 참여
(15, 5, false, NOW()),  -- 장정명이 서주원의 클라우드 스터디에 참여
(15, 6, false, NOW()),  -- 유지민이 서주원의 클라우드 스터디에 참여

-- 게시글 16~20에 참여한 멤버들
(16, 7, false, NOW()),  -- 김민정이 서주원의 JPA 스터디에 참여
(16, 10, false, NOW()), -- 고윤정이 서주원의 JPA 스터디에 참여
(17, 2, false, NOW()),  -- 서주원이 송창욱의 금융 앱 프로젝트에 참여
(17, 4, false, NOW()),  -- 은나현이 송창욱의 금융 앱 프로젝트에 참여
(17, 9, false, NOW()),  -- 이지은이 송창욱의 금융 앱 프로젝트에 참여
(18, 1, false, NOW()),  -- 김시용이 송창욱의 Swift 스터디에 참여
(19, 7, false, NOW()),  -- 김민정이 은나현의 UX 스터디에 참여
(19, 8, false, NOW()),  -- 장원영이 은나현의 UX 스터디에 참여
(20, 6, false, NOW()),  -- 유지민이 은나현의 프로덕트 워크숍에 참여
(20, 7, false, NOW()),  -- 김민정이 은나현의 프로덕트 워크숍에 참여

-- 게시글 21~25에 참여한 멤버들
(21, 2, false, NOW()),  -- 서주원이 장정명의 마이크로서비스 프로젝트에 참여
(21, 6, false, NOW()),  -- 유지민이 장정명의 마이크로서비스 프로젝트에 참여
(21, 10, false, NOW()), -- 고윤정이 장정명의 마이크로서비스 프로젝트에 참여
(22, 2, false, NOW()),  -- 서주원이 장정명의 Kubernetes 스터디에 참여
(23, 4, false, NOW()),  -- 은나현이 유지민의 마케팅 프로젝트에 참여
-- 게시글 23~30에 참여한 멤버들(이어서)
(23, 7, false, NOW()),  -- 김민정이 유지민의 마케팅 프로젝트에 참여
(23, 9, false, NOW()),  -- 이지은이 유지민의 마케팅 프로젝트에 참여
(24, 4, false, NOW()),  -- 은나현이 유지민의 그로스 해킹 스터디에 참여
(24, 10, false, NOW()), -- 고윤정이 유지민의 그로스 해킹 스터디에 참여
(25, 4, false, NOW()),  -- 은나현이 김민정의 애자일 워크숍에 참여
(25, 5, false, NOW()),  -- 장정명이 김민정의 애자일 워크숍에 참여
(25, 8, false, NOW()),  -- 장원영이 김민정의 애자일 워크숍에 참여
(26, 4, false, NOW()),  -- 은나현이 김민정의 로드맵 스터디에 참여
(26, 6, false, NOW()),  -- 유지민이 김민정의 로드맵 스터디에 참여
(27, 1, false, NOW()),  -- 김시용이 장원영의 게임 개발 프로젝트에 참여
(27, 3, false, NOW()),  -- 송창욱이 장원영의 게임 개발 프로젝트에 참여
(27, 9, false, NOW()),  -- 이지은이 장원영의 게임 개발 프로젝트에 참여
(28, 3, false, NOW()),  -- 송창욱이 장원영의 UI/UX 스터디에 참여
(28, 4, false, NOW()),  -- 은나현이 장원영의 UI/UX 스터디에 참여
(29, 3, false, NOW()),  -- 송창욱이 이지은의 AR/VR 프로젝트에 참여
(29, 5, false, NOW()),  -- 장정명이 이지은의 AR/VR 프로젝트에 참여
(29, 8, false, NOW()),  -- 장원영이 이지은의 AR/VR 프로젝트에 참여
(30, 6, false, NOW()),  -- 유지민이 이지은의 게임 기획 스터디에 참여
(30, 8, false, NOW()),  -- 장원영이 이지은의 게임 기획 스터디에 참여
(31, 1, false, NOW()),  -- 김시용이 고윤정의 GraphQL 프로젝트에 참여
(31, 2, false, NOW()),  -- 서주원이 고윤정의 GraphQL 프로젝트에 참여
(31, 7, false, NOW()),  -- 김민정이 고윤정의 GraphQL 프로젝트에 참여
(32, 1, false, NOW()),  -- 김시용이 고윤정의 데이터 시각화 프로젝트에 참여
(32, 4, false, NOW());
-- 은나현이 고윤정의 데이터 시각화 프로젝트에 참여

-- 5. PostPosition 데이터 생성 (모든 게시글의 필요한 포지션)
INSERT INTO post_position (post_id, position_id)
VALUES
-- 게시글 1~5에 필요한 포지션
(1, 1),  -- 리액트 프로젝트: 프런트엔드
(1, 2),  -- 리액트 프로젝트: 백엔드
(1, 3),  -- 리액트 프로젝트: 디자이너
(2, 1),  -- 타입스크립트 스터디: 프런트엔드
(2, 2),  -- 타입스크립트 스터디: 백엔드
(3, 2),  -- 스프링 백엔드 프로젝트: 백엔드
(3, 6),  -- 스프링 백엔드 프로젝트: 데브옵스
(4, 2),  -- 자바 스터디: 백엔드
(5, 4),  -- iOS 앱 프로젝트: iOS
(5, 2),  -- iOS 앱 프로젝트: 백엔드

-- 게시글 6~10에 필요한 포지션
(5, 3),  -- iOS 앱 프로젝트: 디자이너
(6, 8),  -- 서비스 기획 스터디: 기획자
(7, 6),  -- DevOps 프로젝트: 데브옵스
(7, 2),  -- DevOps 프로젝트: 백엔드
(8, 9),  -- 마케팅 스터디: 마케터
(9, 7),  -- PM 스터디: PM
(9, 8),  -- PM 스터디: 기획자
(10, 1), -- React Native 프로젝트: 프런트엔드
(10, 2), -- React Native 프로젝트: 백엔드
(10, 3), -- React Native 프로젝트: 디자이너

-- 게시글 11~15에 필요한 포지션
(11, 8), -- Unity 게임 프로젝트: 기획자
(11, 3), -- Unity 게임 프로젝트: 디자이너
(12, 1), -- 풀스택 스터디: 프런트엔드
(12, 2), -- 풀스택 스터디: 백엔드
(13, 1), -- 웹 기술 스터디: 프런트엔드
(13, 2), -- 웹 기술 스터디: 백엔드
(14, 1), -- 포트폴리오 프로젝트: 프런트엔드
(14, 3), -- 포트폴리오 프로젝트: 디자이너
(15, 2), -- 클라우드 스터디: 백엔드
(15, 6), -- 클라우드 스터디: 데브옵스

-- 게시글 16~20에 필요한 포지션
(16, 2), -- JPA 스터디: 백엔드
(17, 4), -- 금융 앱 프로젝트: iOS
(17, 2), -- 금융 앱 프로젝트: 백엔드
(17, 3), -- 금융 앱 프로젝트: 디자이너
(18, 4), -- Swift 스터디: iOS
(19, 3), -- UX 스터디: 디자이너
(19, 8), -- UX 스터디: 기획자
(20, 7), -- 프로덕트 워크숍: PM
(20, 8), -- 프로덕트 워크숍: 기획자
(20, 3), -- 프로덕트 워크숍: 디자이너

-- 게시글 21~25에 필요한 포지션
(21, 2), -- 마이크로서비스 프로젝트: 백엔드
(21, 6), -- 마이크로서비스 프로젝트: 데브옵스
(22, 6), -- Kubernetes 스터디: 데브옵스
(23, 9), -- 마케팅 프로젝트: 마케터
(23, 8), -- 마케팅 프로젝트: 기획자
(24, 9), -- 그로스해킹 스터디: 마케터
(25, 7), -- 애자일 워크숍: PM
(25, 8), -- 애자일 워크숍: 기획자
(26, 7), -- 로드맵 스터디: PM
(26, 8), -- 로드맵 스터디: 기획자

-- 게시글 26~30에 필요한 포지션
(27, 1), -- 게임 개발 프로젝트: 프런트엔드
(27, 3), -- 게임 개발 프로젝트: 디자이너
(27, 4), -- 게임 개발 프로젝트: iOS
(27, 5), -- 게임 개발 프로젝트: 안드로이드
(28, 1), -- UI/UX 스터디: 프런트엔드
(28, 3), -- UI/UX 스터디: 디자이너
(29, 3), -- AR/VR 프로젝트: 디자이너
(29, 8), -- AR/VR 프로젝트: 기획자
(30, 8), -- 게임 기획 스터디: 기획자
(31, 1), -- GraphQL 프로젝트: 프런트엔드
(31, 2), -- GraphQL 프로젝트: 백엔드
(32, 1), -- 데이터 시각화 프로젝트: 프런트엔드
(32, 2);
-- 데이터 시각화 프로젝트: 백엔드

-- 6. PostTechStack 데이터 생성 (모든 게시글에 필요한 기술 스택)
INSERT INTO post_tech_stack (post_id, tech_stack_id)
VALUES
-- 게시글 1~5에 필요한 기술 스택
(1, 1),   -- 리액트 프로젝트: JavaScript
(1, 2),   -- 리액트 프로젝트: TypeScript
(1, 3),   -- 리액트 프로젝트: React
(1, 8),   -- 리액트 프로젝트: Nextjs
(1, 5),   -- 리액트 프로젝트: Nodejs
(1, 17),  -- 리액트 프로젝트: MySQL
(2, 2),   -- 타입스크립트 스터디: TypeScript
(2, 3),   -- 타입스크립트 스터디: React
(2, 4),   -- 타입스크립트 스터디: Vue
(2, 5),   -- 타입스크립트 스터디: Nodejs
(3, 6),   -- 스프링 백엔드 프로젝트: Spring
(3, 7),   -- 스프링 백엔드 프로젝트: Java
(3, 17),  -- 스프링 백엔드 프로젝트: MySQL
(3, 27),  -- 스프링 백엔드 프로젝트: Docker
(4, 7),   -- 자바 스터디: Java
(4, 6),   -- 자바 스터디: Spring
(5, 15),  -- iOS 앱 프로젝트: Swift
(5, 21),  -- iOS 앱 프로젝트: Firebase

-- 게시글 6~10에 필요한 기술 스택
(6, 29),  -- 서비스 기획 스터디: Figma
(7, 25),  -- DevOps 프로젝트: AWS
(7, 26),  -- DevOps 프로젝트: Kubernetes
(7, 27),  -- DevOps 프로젝트: Docker
(7, 28),  -- DevOps 프로젝트: Git
(8, 29),  -- 마케팅 스터디: Figma (마케팅 관련 툴)
(9, 29),  -- PM 스터디: Figma
(10, 1),  -- React Native 프로젝트: JavaScript
(10, 2),  -- React Native 프로젝트: TypeScript
(10, 22), -- React Native 프로젝트: ReactNative
(10, 21), -- React Native 프로젝트: Firebase

-- 게시글 11~15에 필요한 기술 스택
(11, 23), -- Unity 게임 프로젝트: Unity
(11, 12), -- Unity 게임 프로젝트: C
(12, 1),  -- 풀스택 스터디: JavaScript
(12, 2),  -- 풀스택 스터디: TypeScript
(12, 3),  -- 풀스택 스터디: React
(12, 5),  -- 풀스택 스터디: Nodejs
(12, 10), -- 풀스택 스터디: Express
(12, 17), -- 풀스택 스터디: MySQL
(13, 1),  -- 웹 기술 스터디: JavaScript
(13, 2),  -- 웹 기술 스터디: TypeScript
(13, 3),  -- 웹 기술 스터디: React
(13, 8),  -- 웹 기술 스터디: Nextjs
(14, 1),  -- 포트폴리오 프로젝트: JavaScript
(14, 3),  -- 포트폴리오 프로젝트: React
(14, 29), -- 포트폴리오 프로젝트: Figma
(15, 25), -- 클라우드 스터디: AWS
(15, 26), -- 클라우드 스터디: Kubernetes
(15, 27), -- 클라우드 스터디: Docker

-- 게시글 16~20에 필요한 기술 스택
(16, 6),  -- JPA 스터디: Spring
(16, 7),  -- JPA 스터디: Java
(16, 17), -- JPA 스터디: MySQL
(17, 15), -- 금융 앱 프로젝트: Swift
(17, 21), -- 금융 앱 프로젝트: Firebase
(17, 17), -- 금융 앱 프로젝트: MySQL
(18, 15), -- Swift 스터디: Swift
(19, 29), -- UX 스터디: Figma
(19, 30), -- UX 스터디: Zeplin
(20, 29), -- 프로덕트 워크숍: Figma

-- 게시글 21~25에 필요한 기술 스택
(21, 6),  -- 마이크로서비스 프로젝트: Spring
(21, 25), -- 마이크로서비스 프로젝트: AWS
(21, 26), -- 마이크로서비스 프로젝트: Kubernetes
(21, 27), -- 마이크로서비스 프로젝트: Docker
(22, 26), -- Kubernetes 스터디: Kubernetes
(22, 27), -- Kubernetes 스터디: Docker
(22, 28), -- Kubernetes 스터디: Git
(23, 29), -- 마케팅 프로젝트: Figma (디자인 툴)
(24, 1),  -- 그로스해킹 스터디: JavaScript
(24, 13), -- 그로스해킹 스터디: Python
(25, 28), -- 애자일 워크숍: Git
(26, 29), -- 로드맵 스터디: Figma

-- 게시글 26~30에 필요한 기술 스택
(27, 22), -- 게임 개발 프로젝트: ReactNative
(27, 23), -- 게임 개발 프로젝트: Unity
(27, 1),  -- 게임 개발 프로젝트: JavaScript
(27, 2),  -- 게임 개발 프로젝트: TypeScript
(28, 22), -- UI/UX 스터디: ReactNative
(28, 29), -- UI/UX 스터디: Figma
(29, 23), -- AR/VR 프로젝트: Unity
(29, 12), -- AR/VR 프로젝트: C
(30, 23), -- 게임 기획 스터디: Unity
(30, 29), -- 게임 기획 스터디: Figma
(31, 1),  -- GraphQL 프로젝트: JavaScript
(31, 2),  -- GraphQL 프로젝트: TypeScript
(31, 3),  -- GraphQL 프로젝트: React
(31, 5),  -- GraphQL 프로젝트: Nodejs
(31, 20), -- GraphQL 프로젝트: GraphQL
(32, 1),  -- 데이터 시각화 프로젝트: JavaScript
(32, 3),  -- 데이터 시각화 프로젝트: React
(32, 13);
-- 데이터 시각화 프로젝트: Python

-- 1. 몇 개의 프로젝트 상태를 COMPLETED로 변경 (완료된 프로젝트)
UPDATE post
SET status      = 'COMPLETED',
    culture_fit = 'COMMUNICATIVE'
WHERE id IN (1, 5, 9);
UPDATE post
SET status      = 'COMPLETED',
    culture_fit = 'AUTONOMOUS'
WHERE id IN (3, 8, 12);
UPDATE post
SET status      = 'COMPLETED',
    culture_fit = 'PLANNER'
WHERE id IN (4, 10, 15);
UPDATE post
SET status      = 'COMPLETED',
    culture_fit = 'PRACTICAL'
WHERE id IN (7, 11, 20);

-- 2. 완료된 프로젝트에 GitHub 링크 추가 (Portfolio 엔티티용)
INSERT INTO portfolio (member_id, post_id, post_title, post_link, average_score, recruit_type, created_at)
VALUES (1, 1, '리액트 기반 웹 프로젝트 팀원 모집합니다', 'https://github.com/kimsy/react-project', 4.7, 'PROJECT', NOW()),
       (3, 1, '리액트 기반 웹 프로젝트 팀원 모집합니다', 'https://github.com/songcw/react-contribution', 4.5, 'PROJECT', NOW()),
       (5, 1, '리액트 기반 웹 프로젝트 팀원 모집합니다', 'https://github.com/jangjm/react-project-devops', 4.8, 'PROJECT', NOW()),
       (2, 3, '스프링 백엔드 개발자 모집', 'https://github.com/seojw/spring-backend', 4.6, 'PROJECT', NOW()),
       (1, 3, '스프링 백엔드 개발자 모집', 'https://github.com/kimsy/spring-contribution', 4.3, 'PROJECT', NOW()),
       (5, 3, '스프링 백엔드 개발자 모집', 'https://github.com/jangjm/spring-devops-config', 4.9, 'PROJECT', NOW()),
       (3, 5, 'iOS 앱 개발 프로젝트 함께하실 분', 'https://github.com/songcw/ios-app-main', 4.5, 'PROJECT', NOW()),
       (2, 5, 'iOS 앱 개발 프로젝트 함께하실 분', 'https://github.com/seojw/ios-app-backend', 4.4, 'PROJECT', NOW()),
       (4, 5, 'iOS 앱 개발 프로젝트 함께하실 분', 'https://github.com/eunnh/ios-app-planning', 4.6, 'PROJECT', NOW()),
       (5, 7, 'DevOps 관련 프로젝트 팀원 구합니다', 'https://github.com/jangjm/devops-project', 4.9, 'PROJECT', NOW()),
       (2, 7, 'DevOps 관련 프로젝트 팀원 구합니다', 'https://github.com/seojw/devops-backend', 4.7, 'PROJECT', NOW()),
       (10, 7, 'DevOps 관련 프로젝트 팀원 구합니다', 'https://github.com/goyn/devops-fullstack', 4.5, 'PROJECT', NOW()),
       (2, 4, '자바 스터디 그룹 참여자 모집', 'https://github.com/seojw/java-study', 4.2, 'STUDY', NOW()),
       (7, 4, '자바 스터디 그룹 참여자 모집', 'https://github.com/kimmj/java-study-contribution', 4.4, 'STUDY', NOW()),
       (10, 4, '자바 스터디 그룹 참여자 모집', 'https://github.com/goyj/java-study-notes', 4.3, 'STUDY', NOW()),
       (7, 8, '마케팅 전략 수립 스터디', 'https://github.com/kimmj/marketing-study', 4.5, 'STUDY', NOW()),
       (4, 8, '마케팅 전략 수립 스터디', 'https://github.com/eunnh/marketing-study-notes', 4.3, 'STUDY', NOW()),
       (4, 9, '프로덕트 매니저 역량 강화 스터디', 'https://github.com/eunnh/pm-study', 4.6, 'STUDY', NOW()),
       (8, 9, '프로덕트 매니저 역량 강화 스터디', 'https://github.com/jangwy/pm-study-contribution', 4.4, 'STUDY', NOW());

-- 3. 피어 리뷰 데이터 추가
-- 프로젝트 1에 대한 피어 리뷰
INSERT INTO peer_review (reviewer_member_id, reviewee_member_id, post_id, collaboration_score, technical_score,
                         work_again_score, average_score, review_comment, review_date)
VALUES
-- 멤버 3이 멤버 1을 리뷰
(3, 1, 1, 5, 4, 5, 4.7, '프로젝트 리더로서 팀을 잘 이끌어주었습니다. 소통이 원활했고 기술적인 문제 해결 능력도 뛰어났습니다.', NOW()),
-- 멤버 5가 멤버 1을 리뷰
(5, 1, 1, 5, 5, 5, 5.0, '프로젝트 일정 관리와 코드 품질 관리가 훌륭했습니다. 다음에도 함께 일하고 싶은 개발자입니다.', NOW()),
-- 멤버 1이 멤버 3을 리뷰
(1, 3, 1, 4, 5, 5, 4.7, '기술적 역량이 뛰어나고 새로운 아이디어 제안이 많았습니다. 코드 품질도 매우 좋았습니다.', NOW()),
-- 멤버 5가 멤버 3을 리뷰
(5, 3, 1, 4, 5, 4, 4.3, '혁신적인 접근 방식으로 문제를 해결하는 능력이 인상적이었습니다.', NOW()),
-- 멤버 1이 멤버 5을 리뷰
(1, 5, 1, 5, 5, 5, 5.0, 'DevOps 관련 지식이 풍부하여 배포 과정이 매우 순조로웠습니다. 협업 태도도 훌륭했습니다.', NOW()),
-- 멤버 3이 멤버 5을 리뷰
(3, 5, 1, 5, 5, 4, 4.7, '인프라 구축 및 유지 관리 능력이 뛰어나고, 문제 발생 시 빠른 대응이 인상적이었습니다.', NOW()),

-- 프로젝트 3에 대한 피어 리뷰
-- 멤버 1이 멤버 2를 리뷰
(1, 2, 3, 5, 5, 5, 5.0, '백엔드 아키텍처 설계가 탁월했고, 명확한 API 문서화가 프로젝트 진행에 큰 도움이 되었습니다.', NOW()),
-- 멤버 5가 멤버 2를 리뷰
(5, 2, 3, 4, 5, 5, 4.7, '효율적인 데이터베이스 설계와 안정적인 서버 구축 능력이 인상적이었습니다.', NOW()),
-- 멤버 2가 멤버 1을 리뷰
(2, 1, 3, 4, 4, 5, 4.3, '프론트엔드와 백엔드 간 인터페이스 이해도가 높아 협업이 원활했습니다.', NOW()),
-- 멤버 5가 멤버 1을 리뷰
(5, 1, 3, 5, 4, 4, 4.3, '적극적인 참여 태도와 효과적인 커뮤니케이션 능력이 돋보였습니다.', NOW()),
-- 멤버 2가 멤버 5을 리뷰
(2, 5, 3, 5, 5, 5, 5.0, 'CI/CD 파이프라인 구축으로 개발 생산성이 크게 향상되었습니다. 최신 기술 트렌드에 대한 지식도 풍부했습니다.', NOW()),
-- 멤버 1이 멤버 5을 리뷰
(1, 5, 3, 5, 5, 4, 4.7, '클라우드 인프라 최적화와 보안 설정에 대한 전문성이 인상적이었습니다.', NOW()),

-- 프로젝트 5에 대한 피어 리뷰
-- 멤버 2가 멤버 3을 리뷰
(2, 3, 5, 5, 5, 4, 4.7, 'iOS 앱 개발에 대한 전문성이 돋보였고, UI/UX에 대한 세심한 접근이 인상적이었습니다.', NOW()),
-- 멤버 4가 멤버 3을 리뷰
(4, 3, 5, 4, 5, 4, 4.3, '사용자 중심의 개발 방식과 효율적인 코드 구현 능력이 뛰어났습니다.', NOW()),
-- 멤버 3이 멤버 2을 리뷰
(3, 2, 5, 5, 4, 5, 4.7, '안정적인 백엔드 API가 앱 개발에 큰 도움이 되었습니다. 문제 해결 속도도 빨랐습니다.', NOW()),
-- 멤버 4가 멤버 2을 리뷰
(4, 2, 5, 4, 5, 4, 4.3, '데이터 모델링과 서버 성능 최적화에 대한 전문성이 인상적이었습니다.', NOW()),
-- 멤버 3이 멤버 4을 리뷰
(3, 4, 5, 4, 4, 5, 4.3, '앱의 기획 방향 설정과 사용자 요구사항 분석이 매우 체계적이었습니다.', NOW()),
-- 멤버 2가 멤버 4을 리뷰
(2, 4, 5, 5, 4, 4, 4.3, '명확한 요구사항 정의와 효율적인 테스트 케이스 작성이 프로젝트 품질 향상에 크게 기여했습니다.', NOW()),

-- 프로젝트 7에 대한 피어 리뷰
-- 멤버 2가 멤버 5을 리뷰
(2, 5, 7, 5, 5, 5, 5.0, '클라우드 아키텍처 설계와 컨테이너화 전략이 뛰어났습니다. 기술적 지식이 매우 풍부했습니다.', NOW()),
-- 멤버 10가 멤버 5을 리뷰
(10, 5, 7, 5, 5, 5, 5.0, '복잡한 인프라 문제를 효율적으로 해결하는 능력이 인상적이었습니다. 최신 DevOps 도구 활용 능력도 뛰어났습니다.', NOW()),
-- 멤버 5가 멤버 2을 리뷰
(5, 2, 7, 4, 5, 5, 4.7, '마이크로서비스 아키텍처에 대한 이해도가 높고, 확장 가능한 백엔드 구현 능력이 돋보였습니다.', NOW()),
-- 멤버 10가 멤버 2을 리뷰
(10, 2, 7, 5, 4, 5, 4.7, '효율적인 API 설계와 안정적인 서비스 구현으로 프로젝트 성공에 크게 기여했습니다.', NOW()),
-- 멤버 5가 멤버 10을 리뷰
(5, 10, 7, 5, 5, 4, 4.7, '풀스택 개발 능력이 뛰어나고, 프론트엔드와 백엔드를 아우르는 종합적인 시각이 프로젝트에 큰 도움이 되었습니다.', NOW()),
-- 멤버 2가 멤버 10을 리뷰
(2, 10, 7, 4, 5, 5, 4.7, '기술적 문제 해결 능력과 효율적인 코드 작성 능력이 인상적이었습니다. 협업 태도도 매우 좋았습니다.', NOW());

-- 4. 몇 개의 프로젝트 컬처핏 타입 업데이트
UPDATE post
SET culture_fit = 'AUTONOMOUS'
WHERE id IN (2, 13, 26);
UPDATE post
SET culture_fit = 'PLANNER'
WHERE id IN (6, 16, 21);
UPDATE post
SET culture_fit = 'COMMUNICATIVE'
WHERE id IN (17, 22, 27);
UPDATE post
SET culture_fit = 'PRACTICAL'
WHERE id IN (14, 23, 28);
UPDATE post
SET culture_fit = 'HARMONY'
WHERE id IN (18, 24, 29);
UPDATE post
SET culture_fit = 'DIRECTIVE'
WHERE id IN (19, 25, 30);