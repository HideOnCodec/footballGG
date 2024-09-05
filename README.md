<div align="center">
  
  ![logo](https://github.com/user-attachments/assets/f375018d-5ea9-41cf-a980-3bee3154cf27)
</div>

## 1. 서비스 소개

**해외축구 커뮤니티 웹 서비스 : http://footballgg.shop/**

- **자신이 좋아하는 리그나 팀들의 경기 일정을 한눈에 보여주는 서비스**
- **해외축구 하나에 집중된 소통 커뮤니티**
    - 국내의 축구 커뮤니티는 대부분 대형 커뮤니티 안에 소속되거나 네이버 카페가 대부분인 문제점 보완
      
## 2. **백엔드(2024.01.01~)**

- SpringBoot(JPA, ThymeLeaf, SpringSequrity + JWT) , MySQL
- Docker, Redis, GithubAction, Nginx
- **깃허브** : [HideOnCodec/footballGG: 해축지지 (github.com)](https://github.com/HideOnCodec/footballGG)

## 3. 구현

- ERD 및 서버 아키텍처 설계, CICD 구축
    - githubAction 이용, 스프링부트 서버와 Redis 서버 Docker 이미지 빌드 후 Docker Hub에 Push&Pull 수행
    - Nginx를 이용한 블루/그린 무중단 배포 구현, AWS EC2, S3, RDS 연동

![해축지지 drawio](https://github.com/user-attachments/assets/14446428-bc43-44e8-b5c6-ab2d4dc7be96)


<img width="609" alt="해축지지 ERD" src="https://github.com/user-attachments/assets/31cea22e-bdd4-4e18-8892-0f8e3ede96f7">


- 이메일 회원가입, 로그인 API
    - SpringBoot Security, JWT 토큰 반환, Redis를 이용한 이메일 인증코드 전송 기능, 중복 이메일 방지
- AWS S3 파일 업로드(삭제) 및 게시글 CRUD API 구현
    - 블로그처럼 글 사이 이미지 첨부 가능한 게시글 작성 API 구현
    - 게시글 작성 전 이미지 파일을 미리 업로드하고 나중에 게시글과 join하는 로직 → 게시글 업로드 속도 ⬆️
    - 사용자가 게시글 작성 중 취소할 시 이미지 삭제하는 스케줄링 서비스 구현
- 게시글 좋아요 및 최근 일주일 내의 인기 게시글 조회 API 구현
- WebClient를 이용한 해외 축구 API 연동 → 리그별 해외 축구 일정 조회 API 구현
- 타임리프 및 부트스트랩을 이용하여 프론트 구현
    - 로그인 및 회원가입, 이메일 인증 화면 구현
    - 커뮤니티 게시판 페이지 구현
    - 텍스트 에디터 SummerNote 이용한 게시글 작성 구현
