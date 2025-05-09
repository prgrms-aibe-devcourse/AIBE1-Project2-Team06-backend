INSERT INTO positions (id, name) VALUES (1, '프런트엔드');
INSERT INTO positions (id, name) VALUES (2, '백엔드');
INSERT INTO positions (id, name) VALUES (3, '디자이너');
INSERT INTO positions (id, name) VALUES (4, 'IOS');
INSERT INTO positions (id, name) VALUES (5, '안드로이드');
INSERT INTO positions (id, name) VALUES (6, '데브옵스');
INSERT INTO positions (id, name) VALUES (7, 'PM');
INSERT INTO positions (id, name) VALUES (8, '기획자');
INSERT INTO positions (id, name) VALUES (9, '마케터');

INSERT INTO tech_stack (name) VALUES ('JavaScript');
INSERT INTO tech_stack (name) VALUES ('TypeScript');
INSERT INTO tech_stack (name) VALUES ('React');
INSERT INTO tech_stack (name) VALUES ('Vue');
INSERT INTO tech_stack (name) VALUES ('Nodejs');
INSERT INTO tech_stack (name) VALUES ('Spring');
INSERT INTO tech_stack (name) VALUES ('Java');
INSERT INTO tech_stack (name) VALUES ('Nextjs');
INSERT INTO tech_stack (name) VALUES ('Nestjs');
INSERT INTO tech_stack (name) VALUES ('Express');
INSERT INTO tech_stack (name) VALUES ('Go');
INSERT INTO tech_stack (name) VALUES ('C');
INSERT INTO tech_stack (name) VALUES ('Python');
INSERT INTO tech_stack (name) VALUES ('Django');
INSERT INTO tech_stack (name) VALUES ('Swift');
INSERT INTO tech_stack (name) VALUES ('Kotlin');
INSERT INTO tech_stack (name) VALUES ('MySQL');
INSERT INTO tech_stack (name) VALUES ('MongoDB');
INSERT INTO tech_stack (name) VALUES ('php');
INSERT INTO tech_stack (name) VALUES ('GraphQL');
INSERT INTO tech_stack (name) VALUES ('Firebase');
INSERT INTO tech_stack (name) VALUES ('ReactNative');
INSERT INTO tech_stack (name) VALUES ('Unity');
INSERT INTO tech_stack (name) VALUES ('Flutter');
INSERT INTO tech_stack (name) VALUES ('AWS');
INSERT INTO tech_stack (name) VALUES ('Kubernetes');
INSERT INTO tech_stack (name) VALUES ('Docker');
INSERT INTO tech_stack (name) VALUES ('Git');
INSERT INTO tech_stack (name) VALUES ('Figma');
INSERT INTO tech_stack (name) VALUES ('Zeplin');
INSERT INTO tech_stack (name) VALUES ('Jest');
INSERT INTO tech_stack (name) VALUES ('Svelte');

-- Member관련 Mock Data
INSERT INTO member (
    public_id, auth_id, provider, nickname, profile_image_url,
    career, short_description, created_at, updated_at
) VALUES
      (UUID(), 'kakao_1001', 'kakao', '김시용', 'https://picsum.photos/id/1/200', '1 year', '프론트엔드 주니어', NOW(), NOW()),
      (UUID(), 'kakao_1002', 'kakao', '서주원', 'https://picsum.photos/id/2/200', '3 years', '백엔드 개발자', NOW(), NOW()),
      (UUID(), 'kakao_1003', 'kakao', '송창욱', 'https://picsum.photos/id/3/200', '2 years', 'IOS 앱 개발자', NOW(), NOW()),
      (UUID(), 'kakao_1004', 'kakao', '은나현', 'https://picsum.photos/id/4/200', '4 years', '기획 전문가', NOW(), NOW()),
      (UUID(), 'kakao_1005', 'kakao', '장정명', 'https://picsum.photos/id/5/200', '5 years', 'DevOps 엔지니어', NOW(), NOW()),
      (UUID(), 'kakao_1006', 'kakao', '유지민', 'https://picsum.photos/id/6/200', '1 year', '마케팅 주니어', NOW(), NOW()),
      (UUID(), 'kakao_1007', 'kakao', '김민정', 'https://picsum.photos/id/7/200', '6 years', 'PM / 기획 겸직', NOW(), NOW()),
      (UUID(), 'kakao_1008', 'kakao', '장원영', 'https://picsum.photos/id/8/200', '3 years', 'React Native 개발자', NOW(), NOW()),
      (UUID(), 'kakao_1009', 'kakao', '이지은', 'https://picsum.photos/id/9/200', '2 years', 'Unity 기반 게임 개발자', NOW(), NOW()),
      (UUID(), 'kakao_1010', 'kakao', '고윤정', 'https://picsum.photos/id/10/200', '4 years', '풀스택 개발자', NOW(), NOW());

INSERT INTO member_position (member_id, position_id) VALUES
 (1, 1),  -- 프런트엔드
 (2, 2),  -- 백엔드
 (3, 4),  -- IOS
 (4, 8),  -- 기획자
 (5, 6),  -- 데브옵스
 (6, 9),  -- 마케터
 (7, 7),  -- PM
 (8, 1),  -- 프런트엔드 (React Native)
 (9, 5),  -- 안드로이드 (Unity 기반 게임)
 (10, 2); -- 백엔드

INSERT INTO member_tech_stack (member_id, tech_stack_id) VALUES
 (1, 1), (1, 3), (1, 8),          -- JavaScript, React, Next.js
 (2, 6), (2, 7), (2, 17),         -- Spring, Java, MySQL
 (3, 15), (3, 17), (3, 25),       -- Swift, MySQL, AWS
 (4, 29), (4, 30),                -- Figma, Zeplin
 (5, 25), (5, 26), (5, 27),       -- AWS, Kubernetes, Docker
 (6, 28), (6, 24),                -- Git, Flutter
 (7, 20), (7, 29),                -- GraphQL, Figma
 (8, 21), (8, 3), (8, 8),         -- ReactNative, React, Nextjs
 (9, 22), (9, 11), (9, 28),       -- Unity, Go, Git
 (10, 2), (10, 5), (10, 6), (10, 17); -- TypeScript, Nodejs, Spring, MySQL
