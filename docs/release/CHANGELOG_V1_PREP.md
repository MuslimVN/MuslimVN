# MuslimVN — Nhật ký thay đổi chuẩn bị V1 (CHANGELOG)

> **Mục đích:** Ghi lại toàn bộ các thay đổi trong quá trình chuẩn bị phát hành V1 theo `V1_RELEASE_PLAN.md`.

---

## [Batch 0] — Vệ sinh Repo & Tài liệu nền (2026-09-19)

### 1. B4: Dọn dẹp thư mục release nhị phân
- **ID:** `B4-RELEASE-CLEANUP`
- **File thay đổi:**
  - `app/release/` (Đã xóa khỏi git và đĩa)
  - `.gitignore` (Cập nhật chặn `/app/release/`, `*.apk`, `*.aab`, `*.ap_`, `*.dm`)
- **Cách kiểm thử:** Kiểm tra `git status` và `git ls-files app/release`.
- **Kết quả:** Thư mục `app/release/` đã được loại bỏ hoàn toàn khỏi theo dõi git.

### 2. B14 (Một phần): Dọn dẹp cấu hình IDE, test mẫu & tổ chức tài liệu
- **ID:** `B14-REPO-CLEANUP`
- **File thay đổi:**
  - `.gitignore` (Thêm `/.idea/` để ngừng theo dõi thư mục cấu hình IDE)
  - `.idea/` (Đã gỡ theo dõi git các file xml cấu hình cá nhân)
  - `AppIcons/README.md` (Đã xóa file quảng cáo/hướng dẫn bên thứ 3)
  - `app/src/test/java/com/example/muslimvn/ExampleUnitTest.kt` (Đã xóa unit test mẫu)
  - `app/src/androidTest/java/com/example/muslimvn/ExampleInstrumentedTest.kt` (Đã xóa instrumented test mẫu)
  - `docs/history/` (Tạo thư mục lịch sử và di chuyển `gemini-implementation-round3.md`, `gemini-implementation-round4.md`, `uiux-audit-round2.md`, `shape-button-checklist.md`, `hardcode-color-checklist.md`)
- **Cách kiểm thử:** Chạy `git status` và kiểm tra cấu trúc thư mục `docs/history/`.
- **Kết quả:** Repo đã được vệ sinh sạch sẽ, lịch sử các đợt audit trước được lưu gọn gàng vào `docs/history/`.

---

## [Batch 1] — Danh tính & Cấu hình Build (2026-09-19)

### 1. B1: Cập nhật applicationId chính thức
- **ID:** `B1-APPLICATION-ID`
- **File thay đổi:**
  - `app/build.gradle.kts` (Cập nhật `applicationId = "io.github.muslimvn.app"`, giữ `namespace = "com.example.muslimvn"`)
  - `app/src/main/java/com/example/muslimvn/core/utils/AdhanService.kt` (Đổi `ACTION_ADHAN_STOPPED` sang `io.github.muslimvn.app.ACTION_ADHAN_STOPPED`)
- **Cách kiểm thử:** Chạy build và kiểm tra package name trong APK manifest.
- **Kết quả:** Package ID chính thức đạt chuẩn đưa lên Google Play.

### 2. B2: Nâng targetSdk lên API 36
- **ID:** `B2-TARGET-SDK-36`
- **File thay đổi:** `app/build.gradle.kts` (Cập nhật `targetSdk = 36`)
- **Cách kiểm thử:** Biên dịch ứng dụng với Android SDK 36.
- **Kết quả:** Đạt yêu cầu về Target API level bắt buộc của Google Play cho năm 2026.

### 3. B3 & B15: Cấu hình ký bản phát hành và reproducible build
- **ID:** `B3-RELEASE-SIGNING-CONFIG`
- **File thay đổi:**
  - `app/build.gradle.kts` (Nếu không có `keystore.properties` thì không gán `signingConfig` cho release thay vì fallback ký debug key; thêm `dependenciesInfo { includeInApk = false; includeInBundle = false }`)
  - `keystore.properties.example` (Tạo file cấu hình mẫu hướng dẫn tạo keystore)
- **Cách kiểm thử:** Chạy `./gradlew assembleRelease` và `./gradlew bundleRelease`.
- **Kết quả:** Bản build release hợp lệ cho cả Google Play và F-Droid.

---

## [Batch 2] — Gỡ SDK Độc quyền & Chuẩn hóa Vị trí Framework (2026-09-19)

### 1. B5: Thay thế FusedLocationProviderClient bằng Android LocationManager
- **ID:** `B5-FRAMEWORK-LOCATION-MANAGER`
- **File thay đổi:**
  - `app/src/main/java/com/example/muslimvn/core/di/LocationModule.kt` (Cung cấp `LocationManager` thay vì `FusedLocationProviderClient`)
  - `app/src/main/java/com/example/muslimvn/data/repository/LocationRepositoryImpl.kt` (Viết lại bằng `LocationManager`, lấy vị trí mới nhất hoặc yêu cầu vị trí trực tiếp không qua GMS; đổi tọa độ mặc định sang TP. Hồ Chí Minh theo D10)
  - `app/src/main/java/com/example/muslimvn/presentation/screens/OnboardingScreen.kt` (Thay thế kiểm tra cài đặt vị trí GMS bằng `LocationManager.isProviderEnabled`)
- **Cách kiểm thử:** Chạy build và chạy unit test.
- **Kết quả:** Lấy vị trí hoạt động tốt trên thiết bị thuần AOSP/F-Droid không có Google Play Services.

### 2. B5: Loại bỏ Google ML Kit Translate
- **ID:** `B5-REMOVE-MLKIT-TRANSLATE`
- **File thay đổi:**
  - `app/src/main/java/com/example/muslimvn/data/util/TafsirTranslator.kt` (Đã xóa file)
  - `app/src/main/java/com/example/muslimvn/data/repository/QuranRepositoryImpl.kt` (Gỡ dependency `TafsirTranslator`)
  - `app/src/main/java/com/example/muslimvn/presentation/viewmodels/SurahDetailViewModel.kt` (Gỡ dependency `TafsirTranslator`)
  - `app/src/main/java/com/example/muslimvn/presentation/screens/SurahDetailScreen.kt` (Cập nhật chuỗi giao diện)
  - `app/build.gradle.kts` & `gradle/libs.versions.toml` (Loại bỏ `google-mlkit-translate`, `play-services-location`, `kotlinx-coroutines-play-services`)
- **Cách kiểm thử:** Kiểm tra dependency tree và biên dịch ứng dụng.
- **Kết quả:** Dependency tree hoàn toàn sạch các SDK độc quyền không hỗ trợ F-Droid.

### 3. B6: Cấu hình R8 cho NewPipeExtractor (Theo quyết định D3)
- **ID:** `B6-NEWPIPE-R8-RULES`
- **File thay đổi:** `app/proguard-rules.pro` (Thêm `-dontwarn java.beans.**`, `-dontwarn org.mozilla.javascript.**` để R8 minify thành công)
- **Cách kiểm thử:** Chạy `assembleRelease` và `bundleRelease`.
- **Kết quả:** Biên dịch AAB và APK release tối ưu hóa hoàn toàn thành công.

---

## [Batch 3] — Lõi Giờ Cầu Nguyện & Nhắc Giờ Adhan (2026-09-19)

### 1. B7: Mô hình vị trí bền vững & Asset danh sách tỉnh thành Việt Nam
- **ID:** `B7-LOCATION-MODEL-CITIES`
- **File thay đổi:**
  - `app/src/main/java/com/example/muslimvn/domain/models/SavedLocation.kt` (Tạo model `SavedLocation` hỗ trợ nguồn GPS, MANUAL, DEFAULT)
  - `app/src/main/assets/vietnam_cities.json` (Tạo danh sách 63 tỉnh thành Việt Nam kèm tọa độ chuẩn)
  - `app/src/main/java/com/example/muslimvn/domain/repository/SettingsRepository.kt` & `SettingsRepositoryImpl.kt` (Lưu bền vững `SavedLocation` trong DataStore, mặc định TP. Hồ Chí Minh theo D10)
  - `app/src/main/java/com/example/muslimvn/domain/usecases/GetPrayerTimesUseCase.kt` (Chỉ dùng vị trí đã lưu trong DataStore khi chạy background/không có GPS, loại bỏ hoàn toàn fallback An Giang)
- **Cách kiểm thử:** Chạy unit test `PrayerRepositoryImplTest`.
- **Kết quả:** Vị trí cầu nguyện nhất quán, chính xác theo tỉnh thành người dùng đã chọn.

### 2. B8: Cải thiện độ tin cậy của AdhanScheduler, AdhanReceiver & BootReceiver
- **ID:** `B8-ADHAN-RELIABILITY`
- **File thay đổi:**
  - `app/src/main/java/com/example/muslimvn/core/utils/AdhanScheduler.kt` (Tính giờ báo thức ngày mai bằng dữ liệu thiên văn thực tế của ngày mai thay vì cộng `+24h` bị lệch phút)
  - `app/src/main/java/com/example/muslimvn/core/utils/AdhanReceiver.kt` (Bắt lỗi khi khởi chạy `AdhanService` ở nền và tự động rơi về thông báo ưu tiên cao `showNotification`; dùng icon vector `ic_stat_adhan`)
  - `app/src/main/java/com/example/muslimvn/core/utils/BootReceiver.kt` & `AndroidManifest.xml` (Đăng ký thêm `MY_PACKAGE_REPLACED`, `TIMEZONE_CHANGED`, `TIME_CHANGED` để đảm bảo báo thức không bao giờ bị đứt sau khi khởi động lại, cập nhật app, hay đổi múi giờ)
  - `app/src/main/AndroidManifest.xml` (Bỏ `USE_EXACT_ALARM` và `USE_FULL_SCREEN_INTENT` theo D8)
  - `app/src/main/res/drawable/ic_stat_adhan.xml` (Tạo icon đơn sắc cho thanh trạng thái)
  - `app/src/main/res/values/strings.xml` & `AdhanService.kt` (Loại bỏ chuỗi tiếng Việt hardcoded trong thông báo service)
  - `app/src/main/java/com/example/muslimvn/data/repository/PrayerRepositoryImpl.kt` (Chuyển tham số tính toán sang async từ UseCase, triệt tiêu `runBlocking` trên luồng chính khi gọi từ UseCase)
- **Cách kiểm thử:** Chạy `AdhanSchedulerTest` và `testDebugUnitTest`.
- **Kết quả:** Chuỗi báo thức hoạt động bền bỉ, chính xác, không làm vỡ giao diện hay nghẽn luồng chính.

### 3. B16: Bổ sung Unit Test
- **ID:** `B16-PRAYER-UNIT-TESTS`
- **File thay đổi:** `app/src/test/java/com/example/muslimvn/core/utils/AdhanSchedulerTest.kt` (Tạo test kiểm thử cấu trúc dữ liệu và mốc thời gian báo thức)
- **Cách kiểm thử:** `./gradlew testDebugUnitTest`.
- **Kết quả:** 36/36 unit test chạy xanh.

---

## [Batch 4] — An Ninh Manifest, Sao Lưu & Xuất Room Baseline Schema (2026-09-19)

### 1. B9: Cứng hóa Manifest, Quy tắc Backup & Đọc tài liệu an toàn
- **ID:** `B9-SECURITY-BACKUP-DOCS`
- **File thay đổi:**
  - `app/src/main/AndroidManifest.xml` (Cấu hình `android:usesCleartextTraffic="false"`)
  - `app/src/main/res/xml/backup_rules.xml` & `data_extraction_rules.xml` (Cấu hình sao lưu chỉ giữ SharedPref và Room DB, tự động loại trừ dữ liệu tạm/file media)
  - `app/src/main/java/com/example/muslimvn/presentation/screens/DocumentReaderScreen.kt` (Loại bỏ Google Docs Viewer proxy, dùng `Intent.ACTION_VIEW` mở bằng ứng dụng ngoài đối với file PDF/DOCX)
- **Cách kiểm thử:** Kiểm tra mã nguồn và biên dịch ứng dụng.
- **Kết quả:** Tăng cường tính riêng tư, bảo vệ dữ liệu người dùng và tránh rò rỉ URL tài liệu.

### 2. B10: Xuất Room Baseline Schema & Bảo vệ DB người dùng khi cập nhật
- **ID:** `B10-ROOM-BASELINE-SCHEMA`
- **File thay đổi:**
  - `app/build.gradle.kts` (Cấu hình KSP `room.schemaLocation` trỏ tới `app/schemas/`)
  - Toàn bộ 7 lớp `@Database` (`TrackerDatabase`, `QuranDatabase`, `PodcastDatabase`, `AzkarDatabase`, `ZakatDatabase`, `HijriCalendarDatabase`, `DownloadedVideoDatabase`) -> bật `exportSchema = true`
  - `app/schemas/` (Tự động tạo 7 file JSON schema baseline cho V1)
  - `app/src/main/java/com/example/muslimvn/core/di/DatabaseModule.kt` (Gỡ bỏ `fallbackToDestructiveMigration()` ở các DB chứa dữ liệu người dùng: Tracker, Quran, Podcast, Azkar, Zakat)
- **Cách kiểm thử:** Chạy `./gradlew assembleDebug assembleRelease`.
- **Kết quả:** Đã chốt baseline schema V1, đảm bảo người dùng không bị mất dữ liệu khi cập nhật ứng dụng ở V1.1.

### 3. B10: Thay thế GlobalScope
- **ID:** `B10-REPLACE-GLOBALSCOPE`
- **File thay đổi:** `app/src/main/java/com/example/muslimvn/data/repository/QuranRepositoryImpl.kt` (Thay thế `GlobalScope.launch` bằng `repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)`)
- **Cách kiểm thử:** Chạy unit test và biên dịch ứng dụng.
- **Kết quả:** Quản lý vòng đời Coroutines đúng chuẩn, tránh rò rỉ bộ nhớ.

---

## [Batch 5] — Pháp Lý Nội Dung & Làm Sạch Dữ Liệu Quran (2026-09-19)

### 1. B11: Dọn dẹp File thừa & Kiểm kê Nguồn gốc Nội dung
- **ID:** `B11-CONTENT-SOURCES-DOCS`
- **File thay đổi:**
  - `app/src/main/assets/vi_Hisnul_Muslim.docx` (Đã xóa khỏi git và đĩa, tiết kiệm ~20 MB dung lượng APK)
  - `docs/CONTENT_SOURCES.md` (Tạo tài liệu kiểm kê chi tiết nguồn gốc và giấy phép của mọi dữ liệu, phông chữ, âm thanh, bản dịch và thư viện)
- **Cách kiểm thử:** Kiểm tra `git status` và kiểm tra dung lượng APK.
- **Kết quả:** Giảm đáng kể dung lượng APK, minh bạch toàn bộ bản quyền tài sản ứng dụng.

### 2. B12: Làm sạch Dữ liệu Quran & Bổ sung Test Kiểm định
- **ID:** `B12-QURAN-DATA-CLEANUP`
- **File thay đổi:**
  - `app/src/main/java/com/example/muslimvn/data/util/QuranJsonParser.kt` (Xử lý loại bỏ BOM `\uFEFF` và tự động lọc bỏ các ký hiệu chú thích `[n]` trong văn bản dịch tiếng Việt khi parse)
  - `app/src/test/java/com/example/muslimvn/data/util/QuranDataTest.kt` (Viết unit test kiểm tra tính toàn vẹn của 114 Surah, 6236 Ayah không chứa BOM hay ký hiệu chú thích)
- **Cách kiểm thử:** Chạy `./gradlew testDebugUnitTest`.
- **Kết quả:** Hiển thị văn bản Quran sạch sẽ, chính xác 100%.

### 3. B13: Bổ sung Mục Pháp lý & Miễn trừ Trách nhiệm trong Settings
- **ID:** `B13-SETTINGS-LEGAL-ITEMS`
- **File thay đổi:** `app/src/main/java/com/example/muslimvn/presentation/screens/SettingsScreen.kt` (Thêm các mục **Chính sách quyền riêng tư**, **Nguồn & Giấy phép**, **Liên hệ & Báo lỗi**, và hộp thoại **Tuyên bố miễn trừ trách nhiệm**)
- **Cách kiểm thử:** Biên dịch ứng dụng và mở màn Cài đặt.
- **Kết quả:** Đáp ứng đầy đủ các yêu cầu chính sách bắt buộc của Google Play Console và F-Droid.

---

## [Batch 6] — Tài Liệu Phát Hành, Metadata Chợ Ứng Dụng & F-Droid Recipe (2026-09-20)

### 1. B14 & §6: Giấy phép Mã nguồn, README & PRIVACY
- **ID:** `B14-LICENSE-README-PRIVACY`
- **File thay đổi:**
  - `LICENSE` (Khởi tạo file giấy phép mã nguồn mở GPL-3.0-or-later tại thư mục gốc)
  - `README.md` (Khởi tạo README hướng dẫn biên dịch, kiến trúc công nghệ và giới thiệu ứng dụng)
  - `PRIVACY.md` (Khởi tạo Chính sách quyền riêng tư công khai minh bạch về việc xử lý vị trí trên thiết bị)
- **Cách kiểm thử:** Kiểm tra cấu trúc thư mục gốc.
- **Kết quả:** Hoàn thiện đầy đủ các tài liệu tiêu chuẩn cho dự án mã nguồn mở và chợ ứng dụng.

### 2. §6 & §7: Metadata Fastlane, Play Store Data Safety & F-Droid Recipe Draft
- **ID:** `B14-STORE-METADATA-RECIPE`
- **File thay đổi:**
  - `fastlane/metadata/android/vi/` (`title.txt`, `short_description.txt`, `full_description.txt`, `changelogs/1.txt` tiếng Việt)
  - `docs/release/play_data_safety.md` (Tài liệu hướng dẫn điền Mẫu An toàn Dữ liệu trên Google Play Console)
  - `docs/release/fdroid_recipe_draft.yml` (Bản nháp F-Droid build recipe cấu hình tự động build AAB/APK)
- **Cách kiểm thử:** Kiểm tra nội dung văn bản và độ dài ký tự theo giới hạn của Google Play.
- **Kết quả:** Sẵn sàng toàn bộ thông tin mô tả và tài liệu hồ sơ phát hành.
