# miniERP

> 면세점 도메인 기반 미니 ERP 시스템

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)](https://www.postgresql.org/)
[![React](https://img.shields.io/badge/React-19-61dafb)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-8-646cff)](https://vitejs.dev/)

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
| Backend | Java 17, Spring Boot 4.0.6, Spring Data JPA, Spring Security 7 |
| Frontend | React 19, Vite 8, Axios, React Router |
| Database | PostgreSQL 17 |
| Auth | JWT (jjwt 0.12.6), BCrypt |
| Build | Gradle |
| 배포 | Supabase (DB), Railway (Backend), Vercel (Frontend) |

---

## ERD

> 인터랙티브 ERD: [miniERP_ERD_v4.html](./docs/miniERP_ERD_v4.html)

### 테이블 구성 (15개)

**인증 영역**
- `app_user` 시스템 사용자 (JWT 인증)

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

### 3. 파레트 단위 물류비 자동계산
```
product: qty_per_box (박스당 EA) + box_per_pallet (파레트당 박스)
warehouse: cost_per_pallet (파레트당 물류비)

입고수량 입력 시:
box_count     = CEIL(received_qty ÷ qty_per_box)
pallet_count  = CEIL(box_count ÷ box_per_pallet)
logistics_cost = pallet_count × cost_per_pallet  ← 자동계산
```

### 4. 채널별 판매 추적
```
sales.channel = 'ONLINE' | 'OFFLINE'
sales.order_no = 온라인 주문번호 (네이버, 쿠팡 등)
→ 플랫폼 API 연동 확장 가능 구조
```

### 5. 면세점 정산 구조
```
월마감 시 스냅샷 저장 방식
기초재고 + 입고 - 반출 = 기말재고
판매금액 → 공급가액 → 부가세 → 세금계산서 → 실지급액
```

### 6. 영업이익 계산 구조
```
매출액   = sale_qty × sale_price
매출원가 = sale_qty × (cost_price or supply_cost)
물류비   = 해당 기간 purchase_order.logistics_cost 합계
─────────────────────────────────────────────────
영업이익 = 매출액 - 매출원가 - 물류비
```

---

## 시작하기

### 사전 요구사항
- Java 17+
- PostgreSQL 17+
- Gradle
- Node.js 18+ / Yarn

### 백엔드 실행

```bash
# 레포 클론
git clone https://github.com/TerryBlackhoodWoo/miniErp.git
cd miniErp

# DB 생성 (DBeaver 또는 psql)
CREATE DATABASE minierp;

# DDL 실행
psql -U postgres -d minierp -f docs/miniERP_final_v3.sql

# application.yml 설정 (gitignore 제외 - 직접 생성 필요)
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/minierp
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

server:
  port: 8080

jwt:
  secret: your-secret-key-32-chars-minimum!!

# 서버 실행
./gradlew bootRun
```

### 프론트엔드 실행

```bash
cd miniERP_frontend
yarn install
yarn dev
```

### 접속
```
http://localhost:5173
```

> 기본 계정: 별도 app_user INSERT 필요 (BCrypt 해시 사용)

---

## API 엔드포인트

### 인증
| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| POST | `/api/auth/login` | 로그인 → JWT 토큰 발급 |

> 인증 이후 모든 요청에 `Authorization: Bearer {token}` 헤더 필요

### 마스터 데이터
| 도메인 | GET | POST | PUT | DELETE |
|--------|-----|------|-----|--------|
| 협력사 | `GET /api/vendors` | `POST /api/vendors` | `PUT /api/vendors/{id}` | `DELETE /api/vendors/{id}` |
| 브랜드 | `GET /api/brands` | `POST /api/brands` | `PUT /api/brands/{id}` | `DELETE /api/brands/{id}` |
| 상품 | `GET /api/products` | `POST /api/products` | `PUT /api/products/{id}` | `DELETE /api/products/{id}` |
| 지점 | `GET /api/stores` | `POST /api/stores` | `PUT /api/stores/{id}` | `DELETE /api/stores/{id}` |
| 창고 | `GET /api/warehouses` | `POST /api/warehouses` | `PUT /api/warehouses/{id}` | `DELETE /api/warehouses/{id}` |

### 상품 이미지
| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| POST | `/api/products/{id}/image` | 상품 이미지 업로드 (multipart) → `image_url` 자동 갱신 |

> 업로드된 이미지는 `/uploads/products/`에 저장되며 `/uploads/**` 정적 리소스로 서빙됩니다.

### 운영
| 도메인 | GET | POST | 기타 |
|--------|-----|------|------|
| 상품서류 | `GET /api/product-documents` | `POST /api/product-documents` | |
| BOM | `GET /api/boms` | `POST /api/boms` | |
| GWP | `GET /api/gwps` | `POST /api/gwps` | |
| 프로모션 | `GET /api/promotions` | `POST /api/promotions` | |
| 발주/입고 | `GET /api/purchase-orders` | `POST /api/purchase-orders` (등록, status=PENDING) | `POST /api/purchase-orders/{id}/receive` (입고 처리 — LOT/유통기한/물류메모 입력 → 박스/파레트/물류비 자동계산 → `inventory` IN 생성, `@Transactional`) |
| 수불부 | `GET /api/inventories` | `POST /api/inventories` | |
| 판매 | `GET /api/sales` | `POST /api/sales` | |
| 정산 | `GET /api/settlements` | `POST /api/settlements` | |

> **발주/입고 `is_direct`**: `false`(기본)이면 입고 시 `inventory`에 "협력사→창고"(`store_id=NULL`)와 "창고→지점"(`store_id`=배정지점) 2건이 생성됩니다. `true`(직배송)이면 "협력사→지점" 1건만 생성됩니다.

---

## 프로젝트 구조

```
miniERP/
├── src/
│   ├── main/
│   │   ├── java/com/minierp/minierp/
│   │   │   ├── auth/            # JWT 인증 (JwtUtil, JwtFilter, AuthController)
│   │   │   ├── entity/          # JPA Entity (15개)
│   │   │   ├── repository/      # JpaRepository (15개)
│   │   │   ├── service/         # 비즈니스 로직 (PurchaseOrderService)
│   │   │   ├── controller/      # REST API (14개)
│   │   │   ├── SecurityConfig   # Spring Security + CORS 설정
│   │   │   └── WebConfig        # 정적 리소스 매핑 (/uploads/** → uploads/)
│   │   └── resources/
│   │       └── application.yml  # (gitignore 제외 - 직접 생성 필요)
│   └── test/
├── uploads/
│   └── products/                # 업로드된 상품 이미지 (gitignore 제외)
├── docs/
│   ├── miniERP_ERD_v4.html       # 인터랙티브 ERD
│   └── miniERP_final_v3.sql     # DDL
├── build.gradle
└── README.md

miniERP_frontend/
├── src/
│   ├── api/
│   │   └── axios.js              # axios 인스턴스 (JWT 자동 첨부)
│   ├── components/
│   │   ├── ui.jsx                # 공용 컴포넌트 (Modal, Field, Badge 등)
│   │   ├── index.js               # 팝업 컴포넌트 re-export
│   │   ├── shared.jsx             # genCode, PopupFooter (공용)
│   │   └── popup/
│   │       ├── ProductRegisterPopup.jsx
│   │       ├── BrandRegisterPopup.jsx
│   │       ├── VendorRegisterPopup.jsx
│   │       ├── WarehouseRegisterPopup.jsx
│   │       ├── StoreRegisterPopup.jsx
│   │       ├── PurchaseOrderRegisterPopup.jsx  # 발주 등록 (직배송 체크)
│   │       └── ReceivePopup.jsx                # 입고 처리 (LOT/유통기한/물류메모)
│   ├── pages/
│   │   ├── Login.jsx              # 로그인
│   │   ├── Dashboard.jsx          # 대시보드
│   │   ├── MasterData.jsx         # 기초정보 관리 (탭 전환 + 데이터 fetch)
│   │   ├── master-tabs/
│   │   │   ├── ProductTab.jsx     # 상품 탭 (테이블 + CRUD)
│   │   │   ├── BrandTab.jsx       # 브랜드 탭
│   │   │   ├── VendorTab.jsx      # 협력사 탭
│   │   │   ├── WarehouseTab.jsx   # 창고 탭
│   │   │   └── StoreTab.jsx       # 지점 탭
│   │   ├── PurchaseOrder.jsx      # 발주·입고 페이지 (데이터 fetch + 상태 관리)
│   │   ├── purchase-tabs/
│   │   │   └── PurchaseOrderTab.jsx  # 발주 목록 + 등록/입고 팝업
│   │   ├── Inventory.jsx          # 재고 수불 (더미 데이터, v0.0.6에서 current_stock 연동 예정)
│   │   └── Settlement.jsx         # 정산
│   ├── App.jsx
│   └── index.css
└── package.json
```
└── package.json
```

---

## 개발 현황

### v0.0.1 - 기반 구축
- [x] 프로젝트 초기 설정 (Spring Boot + PostgreSQL)
- [x] ERD 설계 완료 (15개 테이블)
- [x] DB DDL 작성
- [x] Entity / Repository / Controller 작성
- [x] PUT / DELETE API 추가
- [x] React 프론트엔드 기본 구조 (더미 데이터 기반)
- [x] 대시보드 / 상품 / 재고 / 정산 UI

### v0.0.2 - 인증 + 마스터 데이터 연동
- [x] JWT 인증 구현 (JwtUtil / JwtFilter / AuthController)
- [x] `app_user` 테이블 + BCrypt 계정 관리
- [x] Spring Security JWT 방식 전환 + CORS 설정
- [x] 프론트 로그인 페이지 + 토큰 저장 / 자동 로그아웃
- [x] axios 인스턴스 (JWT 자동 첨부)
- [x] 상품 / 브랜드 / 협력사 / 창고 마스터 실DB 연동
- [x] 브랜드 등록 팝업 추가
- [x] `warehouse` 테이블 컬럼 확장 (type / location / manager / phone / status)

### v0.0.3 - 기초정보 고도화 1 (물류비 + 입수단위 + 지점)
- [x] 지점(`store`) 탭 추가 및 등록 팝업
- [x] `product` 컬럼 추가 — `qty_per_box` (박스당 EA), `box_per_pallet` (파레트당 박스), `image_url`
- [x] `warehouse` 컬럼 추가 — `cost_per_pallet` (파레트당 물류비)
- [x] `purchase_order` 컬럼 추가 — LOT (`lot_no`, `expire_date`), 물류비 자동계산 (`box_count`, `pallet_count`, `logistics_cost`), 실입고 (`received_qty`, `received_at`)
- [x] 상품 등록 팝업 — 박스/파레트 입수단위 필드 추가
- [x] 창고 등록 팝업 — 파레트 단가 필드 추가
- [x] 상품 테이블 컬럼 개편 (용량, 매입/공급/제조원가 분리 표시)

### v0.0.4 - 기초정보 고도화 2 (수정 기능 + 이미지 업로드)
- [x] 등록 팝업 컴포넌트 분리 (`components/popup/*`, `shared.jsx`, `index.js`)
- [x] `MasterData` 페이지 분리 — 탭별 컴포넌트(`master-tabs/*`)로 구조 정리
- [x] 상품 / 브랜드 / 협력사 / 창고 / 지점 수정 기능 (row 클릭 → 수정 팝업, PK 필드 비활성화)
- [x] `product` 컬럼 추가 — `tax_free` (면세/과세 여부)
- [x] 상품 테이블 — 면세/과세 Badge, 입수단위(박스×파레트) 컬럼 표시
- [x] 상품 이미지 업로드 실연동 (`POST /api/products/{id}/image`, multipart)
- [x] `ImageBox` 실연동 — 등록/수정 시 파일 업로드, 기존 이미지 미리보기 (`value`/`onFileSelect` props, `useEffect` 동기화)
- [x] `WebConfig` 신규 추가 — 정적 리소스 매핑 (`/uploads/**` → `uploads/` 디렉토리)
- [x] `SecurityConfig` — `/uploads/**` permitAll 추가
- [x] multipart 업로드 용량 설정 (10MB)

### v0.0.5 - 발주 · 입고
- [x] `PurchaseOrderService` 신규 — Service 레이어 비즈니스 로직 도입
- [x] 발주 등록 화면 (`PurchaseOrder.jsx` + `purchase-tabs/PurchaseOrderTab.jsx` + `PurchaseOrderRegisterPopup.jsx`)
- [x] 입고 처리 (`POST /api/purchase-orders/{id}/receive`) — LOT/유통기한/물류메모 입력
- [x] 박스/파레트/물류비 자동계산 (`qty_per_box`, `box_per_pallet`, `cost_per_pallet` 기준)
- [x] 입고 시 `inventory` 자동 생성 (IN 수불, `@Transactional`)
- [x] `purchase_order` 컬럼 추가 — `is_direct` (직배송 여부)
- [x] 직배송 분기 처리 — 일반 입고는 "협력사→창고"+"창고→지점" 2건, 직배송은 "협력사→지점" 1건 생성
- [x] ERD v4 / DDL v3 정리 (ALTER 병합, `tax_free`/`is_direct` 반영, 좌표 재배치)

### v0.0.6 - 재고 조회 + 발주/입고 구조 재설계
- [x] `current_stock` View용 읽기 전용 Entity/Repository/Controller (`GET /api/current-stock`)
- [x] `Inventory.jsx` 더미 → 실DB 연동 (`GET /api/inventories/ledger` — Inventory+Product+Warehouse+Store join DTO)
- [x] **발주/입고/배분 구조 재설계 (v0.0.5 버그 수정 포함)**
  - 기존 v0.0.5 `/receive`가 "협력사→창고"+"창고→지점" 2건을 동시 생성하면서 OUT 누락 → 재고 중복 집계 버그 발견
  - `is_direct`(직배송) 분기 완전 제거, 모든 발주를 **2단계 흐름**으로 통일
  - 1단계: 발주 등록 (`PENDING`) — 지점 요청, 경유 창고 지정
  - 2단계: 입고 처리 (`POST /api/purchase-orders/{id}/receive`) — 협력사→창고 IN 1건, `status=RECEIVED`
  - 3단계: 배분 승인 (`POST /api/purchase-orders/{id}/allocate`, 신규) — 창고 OUT + 지점 IN 2건, `status=COMPLETED`
- [x] 프론트 — 발주·입고 화면 3단 구조로 분리
  - 입고 대기 (`ReceiveTab.jsx`, `PENDING` 목록 + `ReceivePopup`)
  - 배분 대기 (`AllocateTab.jsx` 신규, `RECEIVED` 목록 + 배분 승인)
  - 발주 목록 (`PurchaseOrderTab.jsx`, 전체 + 상태별 Badge: 입고대기/배분대기/완료)
  - `PurchaseOrderRegisterPopup.jsx` — 직배송 체크박스 제거, "입고 창고" → "경유 창고"로 라벨 변경
  - `App.jsx` — `po` 페이지 중복 렌더링(`ComingSoon` + `PurchaseOrder`) 버그 수정
- [x] `purchase_order.is_direct` 컬럼 완전 제거 (Entity / DDL / DB)
- [x] `inventory` 테스트 데이터 정리 (v0.0.5 시점 잘못 생성된 row 제거, 새 흐름으로 재시작)

### v0.0.7 - 판매 (다음 작업)
- [ ] 재고 조회 화면 (`Inventory.jsx` 하단 — 수불 이력과 별도로 "현재고" 요약 뷰, `current_stock` 기반, 지점/창고/상품별)
- [ ] 판매 등록 (`sales` 테이블, ONLINE/OFFLINE 채널)
- [ ] 판매 시 `current_stock` 조회 → 재고 부족 검증 → `inventory` OUT 생성 (`@Transactional`)
- [ ] FIFO LOT 선택 (`CurrentStockRepository.findAvailableLotsForSale`)

### v1.0.0 - 배포
- [ ] Supabase (DB) + Railway (Backend) + Vercel (Frontend)

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