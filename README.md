# 📖 우아한 레시피

- 기존에 존재하는 레시피 사이트들은 관련 재료를 구매하는 기능이 없습니다
- 기존에 존재하는 식품 이커머스 사이트들은 재료와 관련된 레시피를 제공하지 않습니다
- 우아한 남매들은 이러한 불편한 점을 모두 개선한 사이트로 다양한 레시피를 제공하고 그와 관련된 재료를 한번에 구매할 수 있습니다 😊

<br />

### 🌱 제공하는 기능

👉 [우아한 레시피 기능 보러가기](https://www.notion.so/Front-Rreview-7eb64f2ba6344b1e90dd8a6703fddeda)



### 📢 실행 방법

---

아래의 Environment Variable을 설정 후 실행

|      Environment Variable       |                         form                          |
| :-----------------------------: | :---------------------------------------------------: |
|      SPRING_DATASOURCE_URL      | jdbc:mysql://[EC2 Public IPv4 DNS]:3306/[Schema Name] |
|      SPRING_DATASOUCE_USER      |                         root                          |
|   SPRING_DATASOURCE_PASSWORD    |                         root                          |
|        JWT_SECRET_TOKEN         |                    aa.bb.cc.dd.ee                     |
|       CLOUD_AWS_S3_BUCKET       |                     S3 Bucket 명                      |
|     CLOUD_AWS_REGION_STATIC     |                        S3 리전                        |
| CLOUD_AWS_CREDENTIALS_ACCESSKEY |  AmazonS3FullAccess 권한이 있는 IAM 사용자 AccessKey  |
| CLOUD_AWS_CREDENTIALS_SECRETKEY |  AmazonS3FullAccess 권한이 있는 IAM 사용자 SecretKey  |
|             IMP_KEY             |                  123456789012345678                   |

<br />

### 🔗 계정 및 배포 주소

----

**HEAD 계정** : head / head1234

**ADMIN 계정** : admin / admin1234

**SELLER 계정** : seller1 / seller1234

**USER 계정** : user1 / user1234

배포 주소 : http://woowahan.store:8080/ 로 접속하여 우아한 레시피 실행

Swagger: http://woowahan.store:8080/swagger-ui/ 로 접속하여 Swagger 실행

<br />

### 💻 기술 스택 및 개발 환경

---

**언어 / 툴 **

- Java 11
- Intellij Ultimate
- Gradle

**Backend**

- Spring Boot 2.7.5
- Spring Data JPA
- Spring Security
- QueryDsl
- MySQL

**Frontend**

- Thymeleaf 2.7.5

**배포**

- GitLab Runner
- Docker
- EC2

<br />

### ✨ Service Architecture

![서비스 아키텍처](./assets/서비스 아키텍처.png)

<br />

### 🧩 ERD

![ERD](./assets/ERD.png)

