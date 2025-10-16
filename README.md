# Manjee Android Basic Project

## TL;DR
- Jetpack Compose + Material 3 기반의 도서 탐색/관리 앱으로 Google Books API를 연동합니다.
- Room + Paging 3 RemoteMediator로 검색 결과를 오프라인 캐시하며 즐겨찾기·정렬이 가능한 개인 라이브러리를 제공합니다.
- Hilt DI와 3계층(앱/UI · 도메인 · 데이터) 모듈 구조로 테스트 가능한 클린 아키텍처를 구현했습니다.
- DataStore(proto)로 앱 테마·언어 설정을 영속화하고, Compose 네비게이션으로 탭/상세 라우팅을 구성했습니다.

## Table of Contents
- [프로젝트 목표](#프로젝트-목표)
- [주요 기능](#주요-기능)
- [아키텍처 한눈에 보기](#아키텍처-한눈에-보기)
- [모듈 구조](#모듈-구조)
- [데이터 흐름과 저장소](#데이터-흐름과-저장소)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [빌드 & 테스트 명령](#빌드--테스트-명령)
- [품질 전략](#품질-전략)
- [코딩 컨벤션 & 가이드](#코딩-컨벤션--가이드)
- [환경 및 설정](#환경-및-설정)
- [향후 개선 아이디어](#향후-개선-아이디어)
- [License](#license)

## 프로젝트 목표
- **무엇을:** 도서 검색·상세·즐겨찾기·개인화 설정까지 아우르는 Compose 기반 Android 레퍼런스 앱을 구축합니다.
- **왜:** 기업 사전 과제에서도 통과 가능한 수준의 구조/품질을 갖춘 샘플을 제공해, 실서비스 수준의 아키텍처와 구현 방법을 제시합니다.

## 주요 기능
- **검색 & 페이징:** Google Books API `volumes` 엔드포인트와 Paging 3를 이용해 무한 스크롤 검색을 제공합니다.
- **도서 상세:** Room 캐시를 기반으로 빠른 상세 조회와 표지, 작가, 평점, 링크 정보를 노출합니다.
- **즐겨찾기 라이브러리:** 즐겨찾기 추가/삭제, 최신순·제목순 정렬, 상세 화면 진입이 가능한 라이브러리 탭을 제공합니다.
- **개인화 설정:** 다크/라이트 테마 토글, 한국어·영어 언어 전환을 지원하고 시스템 Locale과 동기화합니다.
- **오프라인 최적화:** RemoteMediator를 통한 캐시 동기화, Room + DataStore를 활용한 로컬 퍼시스턴스를 제공합니다.
- **접근성 & 국제화:** 다국어 리소스(`values`, `values-ko`)와 RTL 레이아웃 대응, 동적 Locale 적용을 지원합니다.

## 아키텍처 한눈에 보기
```text
Compose UI (app)
  ↓ state
ViewModel (app)
  ↓ invokes
UseCase (domain)
  ↓ via interface
Repository (domain)
  ↓ implemented by
RepositoryImpl (data)
  ↙            ↘
Room Database    Retrofit + DataStore
```
- UI 계층은 Hilt로 주입된 ViewModel을 통해 상태를 수신하고, UI 이벤트를 UseCase로 전달합니다.
- 도메인 계층은 순수 Kotlin 모듈로, 비즈니스 규칙과 인터페이스(`BookRepository`, `LikedBooksRepository`, `UserSettingsRepository`)를 정의합니다.
- 데이터 계층은 네트워크/DB/DataStore 구현과 DI 모듈을 제공하며, Coroutine + Flow로 비동기 스트림을 처리합니다.

## 모듈 구조
| Module | Type | Responsibility | Notable Dependencies |
| --- | --- | --- | --- |
| `app` | Android Application | Compose UI, Navigation, ViewModel, Hilt entry points, 테마/언어 처리 | Material3, Navigation Compose, Hilt, Paging Compose, Coil |
| `domain` | Kotlin JVM Library | 도메인 모델, `UseCase`, `Repository` 인터페이스, `DataResource` 상태 래퍼 | Kotlinx Coroutines, Paging Common |
| `data` | Android Library | Retrofit API, Room DB, DataStore(proto), Repository 구현, RemoteMediator, Hilt Module | Retrofit/OkHttp, Room, Paging Runtime, DataStore Proto, Hilt |

## 데이터 흐름과 저장소
- **네트워크:** `BookApi` (`https://www.googleapis.com/books/v1/`)가 검색 결과를 반환하며, HTTP 로깅 인터셉터로 개발 단계 디버깅을 지원합니다.
- **캐시:** `AppDatabase`는 도서(`BookEntity`), 즐겨찾기(`FavoriteBookEntity`), Remote Key 테이블을 관리하고 마이그레이션(`MIGRATION_1_2`, `MIGRATION_2_3`)을 제공합니다.
- **동기화:** `BookRemoteMediator`가 검색어별 페이지를 Room에 병합하고, 오프라인 조회 시 캐시를 제공합니다.
- **즐겨찾기:** `LikedBooksRepositoryImpl`이 즐겨찾기 DAO를 래핑해 Flow로 ID/목록을 제공합니다.
- **설정:** `UserSettingsRepositoryImpl`은 DataStore(proto)로 테마·언어를 저장하며, 손상 방지를 위해 `UserSettingsSerializer`에서 예외를 다룹니다.
- **에러/상태:** `DataResource` sealed class로 성공/로딩/에러 상태를 래핑할 수 있어 일관된 오류 처리와 UI 피드백에 활용 가능합니다.

## 기술 스택
- **언어 & 툴체인:** Kotlin 1.9.22, Gradle 8.10.2 Wrapper, Android Gradle Plugin 8.8.0.
- **UI:** Jetpack Compose (BOM 2025.01.01), Material3, Navigation Compose, Coil 이미지 로딩.
- **DI:** Hilt (`SingletonComponent` 모듈로 네트워크/DB/DataStore 주입).
- **데이터:** Retrofit 2.11 + OkHttp 4.12, Room 2.6.1, DataStore(proto) 1.1.2, Paging 3.3.6 RemoteMediator.
- **동시성:** Kotlin Coroutines + Flow, `stateIn`/`cachedIn`으로 lifecycle-aware 스트림 관리.
- **테스트:** JUnit4, kotlinx-coroutines-test, Room/AndroidX 테스트 러너, Compose UITest 의존성 준비.

## 시작하기
1. **필수 요구 사항**
   - Android Studio Koala 이상, JDK 17 (Android Gradle Plugin 8.8 요구).
   - Android SDK 24~35 설치.
2. **프로젝트 클론**
   ```bash
   git clone https://github.com/<your-account>/manjee-android-basic-project.git
   cd manjee-android-basic-project
   ```
3. **Android Studio에서 열기**: `File > Open...`으로 루트 선택 후 Gradle Sync를 완료합니다.
4. **로컬 속성 구성 (선택)**: `local.properties`에 SDK 경로가 올바른지 확인합니다. Google Books 공개 API는 키 없이 동작하므로 추가 설정은 필요 없습니다.

## 빌드 & 테스트 명령
```bash
# Debug 빌드 생성
./gradlew :app:assembleDebug

# 전체 JVM 단위 테스트
./gradlew test

# 도메인 모듈 단위 테스트만
./gradlew domain:test

# 데이터/DB 계층 계측 테스트 (에뮬레이터 필요)
./gradlew connectedAndroidTest

# 정적 분석 및 Lint
./gradlew lint
```

## 품질 전략
- **유닛 테스트:** ViewModel, Repository 핵심 로직에 대해 성공·실패 경로를 검증 (`BookViewModelTest`, `LibraryViewModelTest`, `UserSettingsRepositoryImplTest`).
- **계측 테스트:** Room 마이그레이션 안정성을 `AppDatabaseMigrationTest`로 검증하고, DAO 동작을 `FavoriteBookDaoTest`로 보장합니다.
- **Dispatcher 관리:** `MainDispatcherRule`로 테스트에서 메인 디스패처를 제어해 안정적인 Coroutine 실행을 보장합니다.
- **DI 구성 검증:** Hilt 모듈을 모듈 단위로 분리하여 테스트 더블로 치환하기 쉽도록 했습니다.

## 코딩 컨벤션 & 가이드
- Kotlin 공식 스타일, 4-space indent, 멀티라인 trailing comma 유지.
- Compose 컴포저블은 PascalCase(`BookDetailScreen`), ViewModel은 `ViewModel` 접미사를 사용합니다.
- Repository/UseCase는 도메인 명명 규칙(`VerbNounUseCase`)을 따르고, Hilt 모듈로 의존성을 주입합니다.
- Proto schema(`user_settings.proto`) 및 Room 엔티티 변경 시 마이그레이션과 테스트를 동시에 추가합니다.

## 환경 및 설정
- **API Base URL:** `BookApi.BASE_URL = https://www.googleapis.com/books/v1/`.
- **DataStore 파일:** `user_settings.pb` (앱 전용 내부 저장소, proto 포맷).
- **Room DB 파일:** `book_database`, 다운그레이드 시 파괴적 마이그레이션(`fallbackToDestructiveMigrationOnDowngrade`).
- **Locale 동기화:** `MainActivity`에서 `AppCompatDelegate.setApplicationLocales`로 시스템 Locale을 동적으로 변경합니다.
- **보안:** 네트워크 통신은 HTTPS를 강제하고, 이미지 URL은 RemoteMediator에서 `https`로 정규화합니다.

## 향후 개선 아이디어
- **검색 UX 강화:** 최근 검색어/추천 키워드 저장, 에러/빈 상태 전용 UI.
- **오프라인 정책:** 캐시 만료 정책 및 백그라운드 동기화 작업(WorkManager) 추가.
- **테스트 확장:** Compose UI 테스트, 네트워크 모킹을 위한 MockWebServer 도입.
- **CI 파이프라인:** GitHub Actions로 빌드·테스트·Lint 자동화 및 코드 커버리지 리포트 생성.
- **분석/로깅:** Firebase Analytics 또는 Crashlytics 연동으로 사용자 행동 및 크래시 추적.

## License
이 프로젝트는 [Apache License 2.0](LICENSE) 하에 배포됩니다.
