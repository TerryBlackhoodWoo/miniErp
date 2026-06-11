# miniERP

> 면세점 도메인 기반 미니 ERP 시스템

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)](https://www.postgresql.org/)
[![React](https://img.shields.io/badge/React-JSX-61dafb)](https://react.dev/)

---

## 프로젝트 소개

면세점 MD/SCM 실무 경험을 바탕으로 설계한 ERP 포트폴리오 프로젝트입니다.
협력사-브랜드-상품 마스터 관리부터 발주/입고, 재고 수불, 판매, 정산까지
실제 면세점 운영 흐름을 그대로 구현합니다.

### 기획 배경
- 실무에서 경험한 Excel 기반 재고관리의 비효율 (수동 이중입력, 이력 미추적)
- 백화점 POS API 단일 키 사용으로 인한 매장별 매출 분류 불가 문제
- GWP/BOM 재고 미추적으로 인한 행사 중 증정품 소진 문제
- 위 문제들을 해결하는 구조로 직접 설계

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot 4.0.6, Spring Data JPA, Spring Security |
| Frontend | React, JSX (개발 예정) |
| Database | PostgreSQL 17 |
| Build | Gradle |
| 배포 | Supabase (DB), Railway (Backend), Vercel (Frontend) |

---

## ERD

> 인터랙티브 ERD: [miniERP_ERD_v2.html](./docs/miniERP_ERD_v2.html)

### 테이블 구성 (14개)

**상품 영역**
- `vendor` 협력사 / `brand` 브랜드 / `product` 상품
- `product_document` 상품 서류 (수입필증, BSE, KC인증)
- `bom` BOM 구성 (세트/GWP/샘플)

**물류 영역**
- `store` 지점 / `warehouse` 창고 / `warehouse_store` 창고-지점 매핑
- `gwp` GWP 증정품 / `promotion` 프로모션
- `purchase_order` 발주/입고 / `inventory` 통합 수불부

**판매/정산 영역**
- `sales` 판매 (온/오프라인 채널 구분)
- `settlement` 월별 정산 (면세점 마감 내역서 구조)

---

## 핵심 설계 포인트

### 1. 원가 계산 유연성
```
계약 형태에 따라 원가 기준 선택 가능
- 완사입: cost_price (매입원가 직접 입력)
- 위탁: supply_cost (공급원가 직접 입력)
- 미입력 시: retail_price or sale_price × supply_rate 자동 계산
             (cost_base 컬럼으로 기준 선택)
```

### 2. LOT 기반 재고 관리
```
입고 시 lot_no + expire_date 기록
→ 유통기한 임박 알림 가능
→ 리콜 발생 시 해당 LOT 즉시 추적
→ current_stock View로 실시간 현재고 조회
```

### 3. 채널별 판매 추적
```
sales.channel = 'ONLINE' | 'OFFLINE'
sales.order_no = 온라인 주문번호 (네이버, 쿠팡 등)
→ 플랫폼 API 연동 확장 가능 구조
```

### 4. 면세점 정산 구조
```
월마감 시 스냅샷 저장 방식
기초재고 + 입고 - 반출 = 기말재고
판매금액 → 공급가액 → 부가세 → 세금계산서 → 실지급액
```

---

## 시작하기

### 사전 요구사항
- Java 17+
- PostgreSQL 17+
- Gradle

### 설치 및 실행

```bash
# 레포 클론
git clone https://github.com/TerryBlackhoodWoo/miniErp.git
cd miniErp

# DB 생성 (DBeaver 또는 psql)
CREATE DATABASE minierp;

# DDL 실행
psql -U postgres -d minierp -f docs/miniERP_final_v2.sql

# application.yml 설정
# src/main/resources/application.yml 생성 후 DB 비밀번호 입력
# (application.yml은 .gitignore에 포함되어 있어 별도 생성 필요)

# 서버 실행
./gradlew bootRun
```

### 접속
```
http://localhost:8080
```

---

## API 엔드포인트

> 현재 GET(전체조회) / POST(등록) 구현 완료

| 도메인 | GET | POST |
|--------|-----|------|
| 협력사 | `GET /api/vendors` | `POST /api/vendors` |
| 브랜드 | `GET /api/brands` | `POST /api/brands` |
| 상품 | `GET /api/products` | `POST /api/products` |
| 상품서류 | `GET /api/product-documents` | `POST /api/product-documents` |
| 지점 | `GET /api/stores` | `POST /api/stores` |
| 창고 | `GET /api/warehouses` | `POST /api/warehouses` |
| BOM | `GET /api/boms` | `POST /api/boms` |
| GWP | `GET /api/gwps` | `POST /api/gwps` |
| 프로모션 | `GET /api/promotions` | `POST /api/promotions` |
| 발주/입고 | `GET /api/purchase-orders` | `POST /api/purchase-orders` |
| 수불부 | `GET /api/inventories` | `POST /api/inventories` |
| 판매 | `GET /api/sales` | `POST /api/sales` |
| 정산 | `GET /api/settlements` | `POST /api/settlements` |

---

## 프로젝트 구조

```
miniERP/
├── src/
│   ├── main/
│   │   ├── java/com/minierp/minierp/
│   │   │   ├── entity/          # JPA Entity (13개)
│   │   │   ├── repository/      # JpaRepository (13개)
│   │   │   ├── service/         # 비즈니스 로직 (예정)
│   │   │   ├── controller/      # REST API (13개)
│   │   │   └── SecurityConfig   # Spring Security 설정
│   │   └── resources/
│   │       └── application.yml  # (gitignore 제외 - 직접 생성 필요)
│   └── test/
├── docs/
│   ├── miniERP_ERD_v2.html      # 인터랙티브 ERD
│   └── miniERP_final_v2.sql     # DDL
├── build.gradle
└── README.md
```

---

## 개발 현황

- [x] 프로젝트 초기 설정 (Spring Boot + PostgreSQL)
- [x] ERD 설계 완료 (14개 테이블)
- [x] DB DDL 작성
- [x] Entity 클래스 작성 (13개)
- [x] Repository 작성 (13개 - JpaRepository)
- [x] REST API 개발 (13개 Controller - GET/POST)
- [x] Spring Security 설정 (개발용 전체 허용)
- [x] PUT/DELETE API 추가
- [ ] Service 레이어 비즈니스 로직
- [ ] React 프론트엔드
- [ ] 배포 (Supabase + Railway + Vercel)

---

## 관련 프로젝트

| 프로젝트 | 설명 | 스택 |
|---------|------|------|
| [SOHOBI](https://github.com/TerryBlackhoodWoo/sohobi) | 상권 분석 플랫폼 | Python, FastAPI, React |
| [VOXScript](https://github.com/TerryBlackhoodWoo/voxscript) | 영상 트랜스크립트 도구 | Python, Electron, OpenAI |

---

## 향후 확장 계획
- 네이버 스마트스토어 API 연동 (온라인 매출 자동 집계)
- 백화점 POS API 연동 (매장별 매출 자동 분류)
- 유통기한 임박 알림 기능
- GWP-BOM 연동 자동 재고 차감
- 외화(USD/JPY) 가격 관리 테이블 추가