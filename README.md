# EUM - 팀 매칭 플랫폼 백엔드

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?style=for-the-badge&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326ce5.svg?&style=for-the-badge&logo=kubernetes&logoColor=white)

</div>

## 📋 프로젝트 소개

EUM은 개발자들을 위한 팀 빌딩 플랫폼입니다. AI 기반 컬처핏 분석을 통해 팀원들의 협업 성향을 파악하고, 프로젝트와 스터디를 위한 최적의 팀 매칭을 지원합니다.
![Main Page](https://github.com/user-attachments/assets/70d0f17f-6c60-4223-bb3c-7b4fb97d2f6f)

### 주요 기능
- 🤝 **프로젝트/스터디 모집 및 참여**
- 🤖 **AI 기반 컬처핏 타입 분석** (Gemini API 활용)
- ⭐ **팀원 간 피어 리뷰 시스템**
- 📁 **포트폴리오 관리**
- 🔐 **카카오 소셜 로그인**

## 🛠 기술 스택

### Backend
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- Spring WebFlux (AI 통신)
- JWT Authentication

### Database
- MySQL (Production)
- H2 Database (Development)

### AI Integration
- Google Gemini API
- 비동기 처리를 위한 WebClient

### DevOps
- Docker
- Kubernetes
- GitHub Actions (CI/CD)
- AWS EC2

## 🏗 프로젝트 구조

```
eum/
├── src/main/java/com/eum/
│   ├── ai/                # AI 컬처핏 분석 모듈
│   ├── member/            # 회원 관리 모듈
│   ├── post/              # 게시글 및 포트폴리오 관리
│   ├── review/            # 피어 리뷰 시스템
│   └── global/            # 공통 설정 및 예외 처리
├── src/main/resources/
│   ├── application.yml    # 애플리케이션 설정
│   └── data-*.sql        # 초기 데이터 설정
└── build.gradle          # 의존성 관리
```

## 🚀 시작하기

### 필수 요구사항
- JDK 17 이상
- Docker & Docker Compose
- MySQL 8.0 이상

## 📡 API 엔드포인트

### 인증
- `POST /api/v1/login` - 카카오 로그인
- `POST /api/v1/validate-token` - JWT 토큰 검증

### 회원
- `GET /api/v1/members/profile/me` - 내 프로필 조회
- `PUT /api/v1/members/profile` - 프로필 수정
- `GET /api/v1/members/profile/{nickname}` - 특정 회원 프로필 조회

### 게시글
- `GET /api/v1/posts` - 게시글 목록 조회 (필터링 지원)
- `POST /api/v1/posts` - 게시글 작성
- `GET /api/v1/posts/{postId}` - 게시글 상세 조회
- `PUT /api/v1/posts/{postId}` - 게시글 수정
- `DELETE /api/v1/posts/{postId}` - 게시글 삭제
- `PATCH /api/v1/posts/{postId}/complete` - 프로젝트 완료 처리

### 팀원 관리
- `GET /api/v1/posts/{postId}/members` - 팀원 목록 조회
- `POST /api/v1/posts/{postId}/members` - 팀원 추가/수정

### 컬처핏 AI
- `POST /api/v1/culture-fit/{postId}/recommend` - 게시글 컬처핏 추천
- `POST /api/v1/culture-fit/preview` - 개인 컬처핏 미리보기

### 리뷰
- `POST /api/v1/peer-reviews` - 피어 리뷰 작성
- `GET /api/v1/peer-reviews/member/{publicId}/score` - 회원 리뷰 점수 조회
- `GET /api/v1/peer-reviews/member/{publicId}/comments` - 회원 리뷰 코멘트 조회

### 포트폴리오
- `GET /api/v1/portfolios/my` - 내 포트폴리오 조회
- `GET /api/v1/portfolios/{publicId}` - 특정 회원 포트폴리오 조회
- `GET /api/v1/portfolios/{portfolioId}/reviews` - 포트폴리오 관련 리뷰 조회

## 🤖 컬처핏 타입

EUM은 6가지 협업 스타일을 기반으로 팀원들의 컬처핏을 분석합니다:

- **AUTONOMOUS (자율형)**: 독립적이고 자기주도적인 업무 스타일
- **PLANNER (계획형)**: 체계적이고 신중한 계획 중심 스타일
- **COMMUNICATIVE (소통 협업형)**: 팀원과의 활발한 소통과 협력 중시
- **PRACTICAL (실용 협업형)**: 효율성과 실행력 중심의 협업
- **HARMONY (조화 중시형)**: 팀원 간 관계와 화합을 중시
- **DIRECTIVE (지시 기반형)**: 명확한 체계와 역할 분담 선호

## 👥 팀 구성

| <img src="https://avatars.githubusercontent.com/u/90291431?v=4" width=150px> | <img src="https://avatars.githubusercontent.com/u/113281522?v=4" width=150px> | <img src="https://avatars.githubusercontent.com/u/90672619?v=4" width=150px> | <img src="https://avatars.githubusercontent.com/u/82145661?v=4" width=150px> | <img src="https://avatars.githubusercontent.com/u/140879889?v=4" width=150px> |
|:-----------------------------------------------------------------------:|:----------------------------------------------------------------------:|:--------------------------------------------------------------------------:|:-----------------------------------------------------------------------:|:---------------------------------------------------------------------:|
|                                   서주원                                   |                                  김시용                                   |                                    송창욱                                     |                                   은나현                                   |                                  장정명                                  |
|               [Joowon-Seo](https://github.com/Joowon-Seo)               |               [KimS1Yong](https://github.com/songcw8)                |                   [songcw8](https://github.com/songcw8)                    |               [nan0silver](https://github.com/nan0silver)               |             [jungmyung16](https://github.com/jungmyung16)             |
|                       팀장 / 프로젝트 총괄 / 프론트엔드 개발·배포                        |                      소셜 로그인 연동/멤버 프로필 설정 및 조회 기능                       |                    게시글 CRUD / 검색&필터링 / 게시글 멤버 관리 시스템 개발                    |                   AI 기반 컬쳐핏 추천 및 평가 시스템 개발 / 포트폴리오 생성                   |                        배포 인프라(CI/CD 구축) 및 문서화                         |

## 📝 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 🤝 기여하기

프로젝트에 기여하고 싶으시다면:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 [이메일 주소] 또는 GitHub Issues를 통해 연락주세요.

---

<div align="center">
Made with ❤️ by Team EUM
</div>