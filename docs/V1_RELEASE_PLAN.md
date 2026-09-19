# MuslimVN — V1 Release Plan (Đóng scope + Google Play + F-Droid)

> **Dành cho:** Gemini trong Android Studio
> **Trạng thái dự án:** sau Round 4 (UI/UX polish đã xong)
> **Ngày lập:** 19/09/2026
> **Nguồn:** phân tích trực tiếp mã nguồn nhánh `master` (249 file Kotlin, ~33.000 dòng)
> **Mục tiêu:** phát hành **V1 đầu tay** lên **Google Play** và **F-Droid** từ **một codebase, một bản build, không product flavor**.

---

## 0. Cách dùng file này

### 0.1 Luật làm việc
1. **Bằng chứng trước, kết luận sau.** Phần §1–§2 là kết quả audit tĩnh của tôi (Claude). Trước khi sửa mục nào, **tự mở đúng file:dòng đã nêu để xác minh lại**. Nếu code đã khác → ghi chú và điều chỉnh, đừng sửa mù.
2. **Không tự quyết những việc thuộc về owner** — xem §3 (Quyết định của owner). Chưa có xác nhận thì **dừng ở Gate** tương ứng.
3. **Thu hẹp, không mở rộng.** Không thêm tính năng mới, không refactor kiến trúc, không nâng major version thư viện (trừ khi bắt buộc cho `targetSdk 36`).
4. **Mỗi lô (Batch) = một nhánh/commit nhỏ, build phải xanh** (`./gradlew assembleDebug testDebugUnitTest lint`) trước khi sang lô tiếp.
5. **Không xóa test, không tắt lint để "cho qua".** Không viết lại lịch sử git, không `push --force`.
6. **Không mở/phân tích file `app/release/app-release.apk`.** Owner xác nhận đó là bản test cũ, chỉ cần xóa (xem B4).
7. Ghi mọi thay đổi vào `docs/release/CHANGELOG_V1_PREP.md` (mỗi mục: ID, file đổi, cách kiểm thử, kết quả).

### 0.2 Giới hạn của audit này (Gemini phải kiểm chứng lại bằng build thật)
- Chưa chạy được Gradle/lint/test (môi trường audit không có mạng) → **mọi kết luận về build, lint, R8 đều chưa được xác nhận bằng máy**.
- Chưa chạy app trên thiết bị → các mục UI chỉ dựa trên đọc code.
- Không mở APK đã commit (theo yêu cầu owner).

### 0.3 Các Gate (điểm dừng bắt buộc)
| Gate | Khi nào | Việc phải làm |
|---|---|---|
| **G0** | Trước khi sửa bất kỳ dòng code nào | Owner điền §3. Chưa đủ → chỉ được làm Batch 0 |
| **G1** | Sau Batch 2 | Báo cáo dependency tree sạch; owner chạy thử app trên máy thật |
| **G2** | Sau Batch 4 | Bản build đủ điều kiện **đưa lên Closed Testing** (khởi động đồng hồ 14 ngày) |
| **G3** | Sau Batch 7 | Báo cáo Go/No-Go cuối cho Play và F-Droid |

---

## 1. Hiện trạng đã xác minh

### 1.1 Điểm tốt (giữ nguyên, đừng phá)
- Kiến trúc rõ ràng: Compose + Navigation 3 + Hilt + Room + DataStore + WorkManager + Media3.
- Giờ cầu nguyện **tính offline** bằng thư viện Adhan (`com.batoulapps.adhan`), có điều chỉnh phút, chọn Madhab, có màn "Chi tiết cách tính" kèm disclaimer fiqh.
- Round 1–4 đã hoàn tất: `enableEdgeToEdge()`, `enableOnBackInvokedCallback=true`, `NavigationSuiteScaffold`, `SwipeToDismissBox`, `Motion.kt`, tooltip, accessibility.
- `keystore.properties`, `*.jks`, `*.keystore` đã nằm trong `.gitignore`. R8 (`isMinifyEnabled`, `isShrinkResources`) đã bật.
- Font Amiri và Inter (SIL OFL) — hợp lệ cho cả Play và F-Droid.
- Zakat có disclaimer (`R.string.zakat_disclaimer`); Masjid đã được ẩn có chủ đích ("Temporarily hidden for v1 release scope" trong `MainNavigation.kt`).

### 1.2 Bản đồ tính năng và đề xuất phạm vi V1
Ký hiệu: **KEEP** = vào V1 · **FIX** = vào V1 nhưng phải sửa · **HIDE** = giữ code, gỡ khỏi UI/manifest/dependency · **CUT** = xóa khỏi V1 · **ASK** = chờ owner (§3).

| Tính năng | File chính | Nguồn dữ liệu | Cần mạng? | Đề xuất |
|---|---|---|---|---|
| Onboarding | `OnboardingScreen.kt` | — | Không | **FIX** (dùng Play Services, xem B5) |
| Giờ cầu nguyện + Home | `HomeScreen.kt`, `NextPrayerHero.kt`, `PrayerRepositoryImpl.kt` | Adhan lib (offline) | Không | **FIX** (vị trí, xem B7) |
| Nhắc giờ / Adhan | `AdhanScheduler.kt`, `AdhanReceiver.kt`, `AdhanService.kt`, `BootReceiver.kt`, `PrayerNotificationsScreen.kt` | Adhan lib + mp3 trong assets | Không | **FIX** (nặng nhất, xem B8) |
| Qibla | `QiblaScreen.kt`, `CompassSensorManager.kt` | Cảm biến + vị trí | Không | **FIX** (vị trí) |
| Quran (chữ + bản dịch) | `QuranScreen.kt`, `SurahDetailScreen.kt` | `quran_vi.json` (assets) | Không | **FIX** (dữ liệu, xem B12) |
| Quran audio (stream + tải) | `QuranDownloadWorker.kt`, `QuranAudioUrlBuilder.kt` | EveryAyah, Quran.com CDN | Có | **ASK** (D5) |
| Quran Mushaf (ảnh trang) | `MushafView.kt`, `MushafDownloadWorker.kt` | `android.quran.com/data/...` | Có | **HIDE** mặc định (D5) |
| Tafsir dịch bằng ML Kit | `TafsirTranslator.kt` | Quran.com API + ML Kit | Có | **CUT** (proprietary, B5) |
| Azkar | `AzkarScreen.kt` | `azkar_vi.json` (assets) | Không | **KEEP** (kiểm nguồn, B11) |
| 99 Danh xưng | `NamesOfAllahScreen.kt` | `NameAllah.json` | Không | **KEEP** |
| Lịch Hijri | `HijriCalendarScreen.kt` | Aladhan API + cache Room | Có | **KEEP** (chịu được offline) |
| Zakat | `ZakatScreen.kt`, `ZakatCalculationEngine` | Offline | Không | **KEEP** |
| Tracker | `TrackerScreen.kt` | Room | Không | **FIX** (mất dữ liệu khi update, B10) |
| Hadith hằng ngày | `DailyReminderViewerScreen.kt`, `HadithRepositoryImpl.kt` | hadeethenc.com | Có | **ASK** (kiểm ToS/attribution) |
| Podcast (Muslim Central) | `Podcast*Screen.kt`, `ScholarDetailScreen.kt` | RSS `rss.muslimcentral.com` | Có | **ASK** (D4) |
| Học giả VN — Mách Zên (tài liệu/âm thanh) | `MachZenScreen.kt` | IslamHouse API v3 | Có | **ASK** (D6) |
| Học giả VN — video YouTube | `VietnamScholarDetailScreen.kt`, `YoutubePlayer*`, `VideoDownloadManager.kt`, `DownloadedVideosScreen.kt` | **NewPipeExtractor** | Có | **CUT** mặc định (D3) |
| Xem tài liệu | `DocumentReaderScreen.kt` | Google Docs Viewer | Có | **FIX** (B9: gửi URL cho Google) |
| Danh sách Masjid | `MasjidListScreen.kt`, `assets/masjids/ho-chi-minh.json` | Asset | Không | **HIDE** (đã ẩn; dọn dead code) |
| `RoadmapCategoryScreen` (Kiến thức / Local VN) | `RoadmapData.kt` | — | — | **CUT** (mục rỗng/placeholder) |
| Settings | `SettingsScreen.kt` | — | — | **FIX** (thiếu Privacy/Licenses, B13) |

**Nút chết (dead click) cần dọn:** `onPrayerTimesClick = {}` và `onMasjidClick = {}` trong `MainNavigation.kt`; `SplashScreen.kt` và `YoutubePlayerScreen.kt` không được tham chiếu ở đâu.

### 1.3 Endpoint mạng (toàn bộ, đã grep)
| Host | Dùng cho | Rủi ro / việc cần làm |
|---|---|---|
| `api.aladhan.com` | Lịch Hijri | Khai trong Privacy; chịu offline |
| `api.quran.com/api/v4` | Verse timing, tafsir | Kiểm điều khoản sử dụng |
| `audio.qurancdn.com`, `everyayah.com` | Audio Quran | Kiểm điều khoản (D5) |
| `android.quran.com/data/width_*/page*.png` | Ảnh Mushaf (**hotlink + tải hàng loạt**) | Có rủi ro; HIDE (D5) |
| `api3.islamhouse.com/v3/paV29H2gm56kVLPy/...` | Tài liệu Mách Zên | Xác nhận `paV29H2gm56kVLPy` là token **công khai** trong tài liệu IslamHouse; nếu là key riêng → đưa ra `BuildConfig`/`local.properties` |
| `hadeethenc.com` | Hadith | Kiểm license + attribution |
| `rss.muslimcentral.com`, `artwork.muslimcentral.com` | Podcast | D4 |
| `youtube.com/feeds/videos.xml`, `img.youtube.com` | Danh sách video (RSS công khai) | Chấp nhận được |
| NewPipeExtractor → YouTube | Trích luồng + tải video | **Chặn phát hành Play** (B6) |
| `docs.google.com/viewer` | Xem tài liệu | Rò rỉ URL cho Google (B9) |
| `google.com/maps/search` | Masjid | Đang ẩn |

### 1.4 Dependency — phân loại theo nền tảng
| Dependency | Play | F-Droid | Ghi chú |
|---|---|---|---|
| `com.google.android.gms:play-services-location` | OK | **CHẶN** (NonFreeDep) | Thay bằng `LocationManager` |
| `kotlinx-coroutines-play-services` | OK | Liên quan | Chỉ để `.await()` Task GMS → bỏ theo |
| `com.google.mlkit:translate` | OK | **CHẶN** (NonFreeDep) | Bỏ tính năng dịch Tafsir |
| `NewPipeExtractor` (JitPack, **GPL-3.0**) | **RỦI RO CAO** | Chấp nhận được | Ràng buộc license của cả app nếu còn (D2) |
| `com.batoulapps.adhan` | OK | OK | MIT |
| Compose, Hilt, Room, Media3, Coil, Retrofit, OkHttp, Gson, Lottie, Paging, WorkManager, DataStore | OK | OK | Apache-2.0 |

Sau khi bỏ 3 mục đầu, chạy `./gradlew :app:dependencies --configuration releaseRuntimeClasspath` và **xác nhận không còn** `com.google.android.gms`, `com.google.mlkit`, `com.google.android.datatransport`, `com.google.firebase`, `org.schabi`, `org.mozilla:rhino`, `org.jsoup` (transitive).

### 1.5 Permission hiện tại (`AndroidManifest.xml`)
| Dòng | Permission | Nhận định |
|---|---|---|
| 5–6 | `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Chỉ cần **COARSE** cho giờ cầu nguyện + Qibla → bỏ FINE |
| 8 và 18 | `SCHEDULE_EXACT_ALARM` | **Khai báo trùng 2 lần** |
| 19 | `USE_EXACT_ALARM` | Play hạn chế cho app báo thức/lịch → rủi ro bị từ chối; xem D8 |
| 20 | `USE_FULL_SCREEN_INTENT` | Từ Android 14 Play chỉ mặc định cấp cho app gọi/báo thức; cần khai báo → xem D8 |
| còn lại | `POST_NOTIFICATIONS`, `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK`, `VIBRATE`, `INTERNET`, `ACCESS_NETWORK_STATE`, `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`, `WRITE_EXTERNAL_STORAGE(maxSdk=28)` | Hợp lý; cần **khai báo Foreground Service** trên Play Console (2 service `mediaPlayback`) |

### 1.6 Asset và bản quyền (dung lượng `assets/` ≈ 50 MB)
| Asset | Kích thước | Tình trạng |
|---|---|---|
| `vi_Hisnul_Muslim.docx` | **20 MB** | **Không được code tham chiếu** (chỉ `azkar_vi.json` được đọc) → lãng phí 20 MB APK; nghi là nguồn của Azkar → cần biết license |
| `audio/adhan/*.mp3` (7 file, ~17,8 MB) | 17,8 MB | Ghi âm của các muezzin có tên (Mishary Alafasy, Nasser Alqatami, Ahmed El Kourdi, Mansoor Az-Zahrani, Hamad Daghriry, Rabeh Ibn Darah…) → **license chưa rõ** |
| `quran_vi.json` | 3,3 MB | Bản dịch tiếng Việt, **không rõ dịch giả/license**; 152 ayah có `[n]` chú thích nhưng dữ liệu **không có nội dung chú thích** |
| `azkar_vi.json`, `NameAllah.json`, `scholars.json` | — | Nguồn chưa ghi |
| `images/Qari/*.webp` (8) | — | Ảnh người thật; license chưa rõ |
| `images/podcast/*.webp` (19) | — | Ảnh giảng viên nổi tiếng (Mufti Menk, Yasir Qadhi, Nouman Ali Khan, Omar Suleiman…); license chưa rõ |
| `images/featured_scholars_vietnam/*` (2) | 3,1 MB (Gosaly_Ahmad) | Ảnh người thật; chưa rõ quyền |
| `images/brand/muslim_central.webp` | — | Logo bên thứ ba |
| `images/hadith_card.png`, `praytime/*.jpg` | 1,5 MB | Nguồn ảnh chưa rõ |
| `lottiefiles/*.json`, `res/raw/lottie_*.json` | — | License từng animation chưa ghi |
| `AppIcons/` | 1,5 MB | Nguồn/tác giả icon chưa ghi (README trong thư mục là quảng cáo của appicon.co, không phải tài liệu dự án) |
| `res/font/amiri_*.ttf`, `inter_variable.ttf` | — | **OK** (SIL OFL 1.1) |

---

## 2. BLOCKER — phải xong trước khi phát hành

Nhãn: **P0** = chặn phát hành · **P1** = nên có · **P2** = sau V1.

### B1 — `applicationId` là `com.example.muslimvn` · P0
- **Bằng chứng:** `app/build.gradle.kts:24`. Play Console từ chối package bắt đầu bằng `com.example`.
- **Sửa:** đổi `applicationId` theo D1. **Giữ nguyên `namespace`** (`com.example.muslimvn`) để không phải rename 249 file — hai giá trị này độc lập trong Gradle. Kiểm tra mọi chuỗi hard-code (`ACTION_ADHAN_STOPPED` trong `AdhanService.kt`, `${applicationId}.androidx-startup`, action intent).
- **Xong khi:** `aapt2 dump badging` hiện package mới; app cài song song được với bản cũ.

### B2 — `targetSdk = 35` · P0
- **Bằng chứng:** `app/build.gradle.kts:26`. Từ **31/08/2026**, app mới và bản cập nhật phải target **API 36** (Play Console Help, mục *Target API level requirements*).
- **Sửa:** `targetSdk = 36` (`compileSdk = 37` đã ổn). Rà các thay đổi hành vi Android 16: edge-to-edge bắt buộc (đã có `enableEdgeToEdge()` — kiểm tra insets ở **mọi** màn hình), predictive back (đã bật), giới hạn khóa hướng màn hình trên màn ≥600dp (app không khóa hướng → ổn), quy tắc Foreground Service, lịch `AlarmManager`.
- **Xong khi:** chạy trên emulator API 36 không vỡ layout, back gesture đúng.

### B3 — Bản release ký bằng **debug key** khi thiếu keystore · P0
- **Bằng chứng:** `app/build.gradle.kts:48-54` (`signingConfigs.getByName("debug")`).
- **Rủi ro:** lỡ upload AAB ký debug lên Play; F-Droid cần build **không ký** để tự ký.
- **Sửa:** khi không có `keystore.properties` → **không gán signingConfig** (ra APK chưa ký), không dùng debug key. Thêm README hướng dẫn tạo `keystore.properties`.
- **Xong khi:** `./gradlew assembleRelease` chạy được không cần keystore; `bundleRelease` với keystore ra AAB đã ký.

### B4 — Thư mục `app/release/` đang nằm trong repo · P0
- **Bằng chứng:** `app/release/` chứa `app-release.apk`, `baselineProfiles/`, `output-metadata.json`. Owner xác nhận là **bản test cũ, quên xóa** (không phân tích nội dung).
- **Sửa:** `git rm -r app/release`; thêm vào `.gitignore`: `/app/release/`, `*.apk`, `*.aab`, `*.ap_`, `*.dm`. F-Droid từ chối repo có file nhị phân dựng sẵn.
- **Lưu ý:** APK vẫn còn trong lịch sử git (làm nặng repo). **Không** viết lại lịch sử khi chưa có phê duyệt của owner; chỉ đề xuất `git filter-repo` trong báo cáo cuối.

### B5 — SDK độc quyền của Google · P0 cho F-Droid
- **Bằng chứng:**
  - `play-services-location`: `build.gradle.kts:101`; dùng trong `LocationRepositoryImpl.kt:8-9`, `LocationModule.kt:4-5`, `OnboardingScreen.kt:69-75,483` (`SettingsClient`, `LocationSettingsRequest`, `ResolvableApiException`).
  - `kotlinx-coroutines-play-services`: `build.gradle.kts:114`.
  - ML Kit Translate: `build.gradle.kts:129`; `data/util/TafsirTranslator.kt`; được inject vào `QuranRepositoryImpl.kt` và `SurahDetailViewModel.kt`.
- **Sửa:**
  1. Viết lại `LocationRepositoryImpl` bằng `LocationManager` (framework): `getLastKnownLocation` qua `NETWORK_PROVIDER`/`PASSIVE_PROVIDER`; từ API 30 dùng `getCurrentLocation`, thấp hơn dùng `requestSingleUpdate` kèm timeout.
  2. Thay hộp thoại "bật vị trí" (`SettingsClient`) bằng intent `Settings.ACTION_LOCATION_SOURCE_SETTINGS`.
  3. Bỏ ML Kit và tính năng dịch Tafsir (ẩn nút/tab tương ứng; chạy `git grep -n "translator\|Tafsir"` để dọn hết).
  4. Bỏ `play-services-location` và `kotlinx-coroutines-play-services`.
- **Xong khi:** dependency tree sạch (mục §1.4); app vẫn lấy được vị trí trên máy **không có Google Play Services** (dùng emulator AOSP để thử).

### B6 — NewPipeExtractor: trích luồng YouTube và **tải video** · P0 cho Play (chờ D3)
- **Bằng chứng:** `build.gradle.kts:109`; `settings.gradle.kts:22` (JitPack); `MuslimApplication.kt` (`NewPipe.init`); `YoutubeRepositoryImpl.kt` (`StreamInfo.getInfo`, `getVideoStreamUrl`, `getAudioStreamUrl`); `VideoDownloadManager.kt`, `DownloadedVideosScreen.kt`, `DownloadOptionDialog.kt`.
- **Rủi ro:** trích xuất/tải nội dung YouTube ngoài API chính thức vi phạm điều khoản YouTube và chính sách sở hữu trí tuệ của Google Play → nguy cơ từ chối/gỡ app. Kênh `@gosalyahmad-Unofficial` là kênh **không chính thức** → còn vấn đề quyền nội dung.
- **Đề xuất mặc định (D3):**
  1. Xóa NewPipe, `NewPipeDownloader.kt`, `NewPipe.init(...)`, `VideoDownloadManager`, `DownloadedVideos*`, `YoutubePlayer*`, `DownloadOptionDialog`, `test/java/TestNewPipe.kt`, repo JitPack.
  2. Giữ danh sách video bằng **`YoutubeRssParser.kt` (đã có sẵn)** — RSS công khai của kênh — kèm thumbnail; bấm vào thì mở bằng `Intent.ACTION_VIEW` sang ứng dụng YouTube/trình duyệt.
  3. Khi bỏ NewPipe (GPL-3.0), ràng buộc license của app được nới ra (xem D2).
- **Xong khi:** `git grep -n "schabi\|newpipe"` không còn kết quả; luồng "học giả VN" vẫn dùng được.

### B7 — Vị trí mặc định ép về An Giang; **không có chọn thành phố thủ công** · P0 (độ chính xác tôn giáo)
- **Bằng chứng:** `GetPrayerTimesUseCase.kt:21-22` (`?: 10.7005`, `?: 105.1147`); `LocationRepositoryImpl.kt` (`DEFAULT_LATITUDE/LONGITUDE`). Không có bất kỳ UI chọn thành phố nào (`grep` "city/chọn thành phố" không có kết quả).
- **Hậu quả:** người từ chối quyền vị trí ở Hà Nội/Đà Nẵng nhận **giờ cầu nguyện của An Giang** mà không hề biết. Sai giờ với app tôn giáo là lỗi nghiêm trọng.
- **Sửa:**
  1. Thêm mô hình vị trí: `GPS | MANUAL | DEFAULT`, **lưu bền vững trong DataStore** (lat, lng, tên, nguồn, thời điểm cập nhật).
  2. Thêm màn hình chọn tỉnh/thành (asset JSON ~63 tỉnh kèm tọa độ; Gemini phải đối chiếu tọa độ với nguồn đáng tin cậy và ghi nguồn).
  3. Onboarding **buộc chọn** (GPS hoặc thủ công); mặc định theo D11.
  4. Luôn hiển thị nhãn "Vị trí: <tên> (GPS/thủ công/mặc định)" trên Home và màn cầu nguyện.
- **Xong khi:** từ chối quyền vị trí + chọn "Hà Nội" → giờ tính theo Hà Nội và hiển thị rõ.

### B8 — Độ tin cậy của nhắc giờ / Adhan · P0
Gồm các lỗi sau (tất cả đã đọc trong code):
1. **Vị trí trong nền bị rơi về An Giang.** `AdhanReceiver` và `BootReceiver` gọi `getPrayerTimesUseCase()` → gọi vị trí trực tiếp (Fused). Ở nền, không có quyền background location, kết quả là `null` → **rơi về tọa độ An Giang**. Sau lần báo thức đầu tiên, lịch kế tiếp có thể tính sai vị trí. **Sửa:** receiver/boot/worker **chỉ dùng vị trí đã lưu** (B7), không bao giờ truy vấn vị trí sống.
2. **Cộng +24h thay vì tính giờ ngày mai.** `AdhanScheduler.scheduleNextWithSettings` lấy giờ hôm nay rồi `+ ONE_DAY_MILLIS`; giờ cầu nguyện thay đổi ~1–2 phút mỗi ngày → lệch. **Sửa:** dùng giờ **thực tế của ngày mai** (repo đã tính `tomorrowFajr`; mở rộng cho cả 5 giờ).
3. **Chuỗi một báo thức dễ đứt.** Chỉ có duy nhất 1 alarm; nếu tiến trình bị kill trước khi chain tiếp, mất mọi nhắc nhở sau đó. **Sửa:** đặt lịch lại ở các thời điểm sau (mục 4) + `try/finally` bảo đảm luôn đặt alarm kế tiếp trong receiver, kể cả khi có lỗi.
4. **`BootReceiver` chỉ nghe `BOOT_COMPLETED`** (manifest dòng ~55–60). Thiếu: `MY_PACKAGE_REPLACED` (sau khi cập nhật app), `TIMEZONE_CHANGED`, `TIME_SET`; và cần lập lại lịch khi người dùng đổi vị trí/phương pháp tính/bật-tắt nhắc.
5. **Không có luồng xin `SCHEDULE_EXACT_ALARM`.** Không tìm thấy `ACTION_REQUEST_SCHEDULE_EXACT_ALARM` ở đâu. Hiện tại chỉ chạy được nhờ `USE_EXACT_ALARM` (tự cấp) — quyền mà Play có thể từ chối (D8). Nếu bỏ `USE_EXACT_ALARM` thì **bắt buộc** thêm luồng xin quyền + banner cảnh báo khi chưa được cấp.
6. **Khởi động Foreground Service từ nền có thể thất bại.** `AdhanReceiver.startAdhanService` → nếu không đủ điều kiện, exception chỉ được `Log.e`, **không phát tiếng và không có thông báo**. **Sửa:** bắt lỗi rồi **rơi về thông báo thường** (`showNotification`).
7. **Icon thông báo dùng launcher mipmap** (`AdhanReceiver.kt:86`, `AdhanService.kt:58`, `QuranDownloadWorker.kt:71`, `MushafDownloadWorker.kt:104`) → hiện ô trắng trên Android 5+. **Sửa:** tạo `ic_stat_*` vector đơn sắc.
8. **Chuỗi tiếng Việt hard-code trong thông báo** (`AdhanService.kt`: "Adhan: …", "Dừng Adhan") → chuyển vào `strings.xml`.
9. **`runBlocking` trên luồng chính:** `PrayerRepositoryImpl.kt:43-45` (3 lần) được gọi từ receiver chạy `Dispatchers.Main` → nguy cơ ANR. **Sửa:** biến thành `suspend`, đọc DataStore một lần.
10. **Múi giờ cứng `Asia/Ho_Chi_Minh`** (`PrayerRepositoryImpl.kt`): chấp nhận cho V1 Việt Nam, nhưng phải **ghi rõ phạm vi Việt Nam** và test khi thiết bị đặt múi giờ khác.
- **Xong khi:** test tự động cho `AdhanScheduler` (giờ ngày mai, qua nửa đêm, đổi múi giờ) + test thủ công: reboot, cập nhật app, đổi múi giờ, Doze/Battery saver, từ chối thông báo, chưa cấp exact alarm.

### B9 — Cứng hóa manifest và quyền riêng tư · P0
- **Sửa:**
  - `android:usesCleartextTraffic="true"` (dòng 32) → `false`. Mọi endpoint hiện đều là `https`; nếu enclosure podcast có `http://`, xử lý bằng nâng cấp sang https hoặc `network_security_config` theo từng domain (không mở toàn cục).
  - `android:allowBackup="true"` (dòng 25) đang dùng **file mẫu chưa chỉnh** (`backup_rules.xml`, `data_extraction_rules.xml` toàn comment). Viết quy tắc rõ: **bao gồm** DataStore + DB người dùng (Tracker, Zakat, yêu thích); **loại trừ** `filesDir/audio`, `filesDir/mushaf`, podcast/video đã tải, cache.
  - Bỏ `ACCESS_FINE_LOCATION` (chỉ giữ COARSE); dọn `SCHEDULE_EXACT_ALARM` trùng.
  - `DocumentReaderScreen.kt:30` gửi URL tài liệu tới `docs.google.com/viewer` → **rò rỉ hành vi người dùng cho Google**. Thay bằng `ACTION_VIEW` (mở bằng ứng dụng đọc PDF/Docs của thiết bị) hoặc tải về rồi mở qua `FileProvider`.
  - `Geocoder` (`LocationRepositoryImpl.getAddress`) phụ thuộc dịch vụ hệ thống, có thể gửi tọa độ ra ngoài và **trả rỗng trên máy không có GMS** → thay bằng danh sách thành phố offline (B7).

### B10 — Mất dữ liệu người dùng khi cập nhật (Room) · P0
- **Bằng chứng:** `fallbackToDestructiveMigration()` ở **5 database** (`DatabaseModule.kt:56 Tracker, :76 Quran, :115 Podcast, :154 Azkar, :174 DownloadedVideo`); **mọi** `@Database` đều `exportSchema = false`; chỉ có 1 migration (`MIGRATION_6_7`).
- **Hậu quả:** bất kỳ thay đổi schema ở V1.1 sẽ **xóa** dữ liệu người dùng nằm trong các DB có fallback phá dữ liệu: lịch sử **Tracker**, **bookmark Quran** (`AyahEntity.isBookmarked` trong `QuranDatabase`), **yêu thích Azkar** (`AzkarEntity.isFavorite`), **yêu thích/playlist/theo dõi Podcast** (`PodcastEpisodeEntity.isFavorite`…). Ngược lại, `ZakatDatabase` (lịch sử Zakat) và `HijriCalendarDatabase` **không có** fallback → thay đổi schema mà thiếu migration sẽ làm app **crash** khi mở DB.
- **Sửa (đây là thời điểm vàng vì chưa có người dùng thật):**
  1. Bật `exportSchema = true` + `ksp { arg("room.schemaLocation", "$projectDir/schemas") }`, commit thư mục `schemas/`.
  2. **Chốt version hiện tại làm baseline V1.**
  3. Bỏ `fallbackToDestructiveMigration()` khỏi mọi DB chứa dữ liệu người dùng (Tracker, Quran, Azkar, Podcast). Chỉ giữ ở DB thuần cache có thể tạo lại (ví dụ `DownloadedVideoDatabase` nếu tính năng còn tồn tại sau B6; cache Hijri) — và ghi chú lý do. Với các DB vừa chứa seed vừa chứa dữ liệu người dùng (Quran, Azkar), viết migration thật hoặc tách bảng người dùng khỏi bảng seed.
  4. Thêm test migration (`MigrationTestHelper`) ngay khi có thay đổi schema đầu tiên sau V1.
- Ngoài ra: `GlobalScope.launch` ở `QuranRepositoryImpl.kt:265,277` → dùng scope có vòng đời (ứng dụng/Worker).

### B11 — Nguồn gốc và giấy phép nội dung · P0
- **Sửa:**
  1. Tạo `docs/CONTENT_SOURCES.md` từ bảng §1.6: mỗi asset **bắt buộc** có *nguồn, tác giả/dịch giả, license, được phân phối lại?, yêu cầu ghi công*. Mục nào không xác định → gắn `KHÔNG RÕ`, coi là **blocker** đến khi owner xác nhận, rồi **thay thế/xóa**.
  2. **Xóa `assets/vi_Hisnul_Muslim.docx`** khỏi thư mục assets (không được dùng; −20 MB). Nếu owner muốn lưu làm tài liệu nguồn thì chuyển ra `docs/sources/` (ngoài APK) và ghi license.
  3. **Ảnh người thật** (Qari 8, Podcast 19, Học giả VN 2): nếu không có quyền → thay bằng avatar chữ cái/hình minh họa trung tính; ảnh podcast lấy từ **artwork RSS** của chính feed.
  4. **Adhan mp3:** chỉ giữ file có giấy phép rõ ràng (D7); tối thiểu vẫn cần 1 adhan + thông báo mặc định.
  5. Thêm dòng miễn trừ "MuslimVN không liên kết/được bảo trợ bởi các học giả, qari, Muslim Central, IslamHouse".

### B12 — Lỗi dữ liệu Quran · P0 (độ chính xác văn bản)
- **Bằng chứng (đã chạy script kiểm tra):**
  - `quran_vi.json`: 114 surah, 6.236 ayah (đủ). Có **1 ký tự U+FEFF (BOM) ẩn** ở đầu `textArabic` của Al-Fatihah ayah 1 → xóa.
  - **152 ayah** có ký hiệu chú thích `[1]`, `[2]`… trong `textVietnamese` (vd. "Allah[1]") nhưng **mô hình dữ liệu không có nội dung chú thích** (chỉ `number`, `textArabic`, `textVietnamese`), và không tìm thấy code xử lý regex `[n]` → người dùng thấy **số trong ngoặc vuông không có chú thích**.
- **Sửa:** (a) bổ sung chú thích từ **cùng nguồn** bản dịch (kèm license), hoặc (b) loại bỏ marker khỏi hiển thị. Đối chiếu văn bản Ả Rập với nguồn chuẩn (Tanzil/Quran.com) bằng checksum; thêm unit test đếm 114/6236 và kiểm tra ký tự lạ.

### B13 — Settings thiếu mục bắt buộc · P0
- **Bằng chứng:** `SettingsScreen.kt` chỉ có Giao diện, Nội dung đã tải, Cài đặt Quran, Thông báo, Đánh giá, Phiên bản.
- **Sửa:** thêm **Chính sách quyền riêng tư** (mở URL công khai), **Nguồn & Giấy phép mã nguồn mở** (thư viện + nguồn nội dung + ghi công), **Liên hệ/Báo lỗi** (GitHub Issues), disclaimer chung ("giờ cầu nguyện/Zakat chỉ mang tính tham khảo; hãy đối chiếu với cộng đồng/học giả địa phương"). Mục **"Đánh giá ứng dụng"** dùng `market://` (Play) → chỉ hiện khi cài từ Play (kiểm `packageManager.getInstallSourceInfo`) hoặc ẩn hẳn.

### B14 — Vệ sinh repo và tài liệu · P0
- Thêm: `LICENSE` (theo D2), `README.md`, `PRIVACY.md`, `CHANGELOG.md`, `fastlane/metadata/android/...` (xem §7).
- `.idea/` đang bị commit (kể cả `planningMode.xml`, `deviceManager.xml`, `deploymentTargetSelector.xml`, `misc.xml` cấu hình `jbr-25`) → `git rm -r --cached .idea`, thêm `/.idea/` vào `.gitignore`.
- `.artifacts/` chứa 7 thư mục kế hoạch/nhiệm vụ do Gemini sinh ra, có **đường dẫn tuyệt đối máy cá nhân** (`/home/hamid/...`) → chuyển sang `docs/history/` (hoặc xóa), loại đường dẫn cá nhân.
- `docs/gemini-implementation-round*.md`, `docs/uiux-audit-round2.md` → chuyển `docs/history/`.
- Xóa: `AppIcons/README.md` (nội dung quảng cáo bên thứ ba, hướng dẫn Expo — không liên quan); `test/java/TestNewPipe.kt`; `ExampleUnitTest.kt`, `ExampleInstrumentedTest.kt` (test mẫu do template tạo); màn/hàm chết (`SplashScreen.kt`, `YoutubePlayerScreen.kt` nếu xác nhận không dùng).
- Quét bí mật trong **toàn bộ lịch sử git** (`.jks`, `.keystore`, `.env`, `google-services.json`, token). Phát hiện → **báo owner**, không tự xử lý lịch sử.

### B15 — Toolchain có thể không build được trên F-Droid · P1 (kiểm chứng bằng `fdroid build`)
- **Bằng chứng:** `gradle-daemon-jvm.properties` (`toolchainVersion=25`), plugin `foojay-resolver-convention` trong `settings.gradle.kts` (tự tải JDK qua mạng), Gradle 9.5.0, AGP 9.3.2, `compileOptions` Java 11.
- **Rủi ro:** buildserver của F-Droid có thể không có JDK 25 và không cho tải toolchain khi build.
- **Sửa:** thử ghim **JDK 21** (nếu AGP cho phép), loại `foojay-resolver` khỏi đường build release, để `distributionSha256Sum` của wrapper được giữ nguyên. Ghi rõ yêu cầu JDK trong README.

### B16 — Độ phủ test thấp · P1
- Chỉ ~773 dòng test cho ~33.000 dòng code (Prayer, Zakat, Masjid, Hadith). **Bắt buộc thêm:** test **giá trị chuẩn (golden)** cho giờ cầu nguyện ở Hà Nội / TP.HCM / An Giang so với nguồn tham chiếu do owner chọn; test `AdhanScheduler`; test dữ liệu Quran (B12); test migration (B10).

### B17 — Chuỗi hard-code · P2
- ~262 chuỗi tiếng Việt hard-code trong UI so với 216 mục `strings.xml`. V1 chỉ tiếng Việt thì chấp nhận được, **trừ** chuỗi hiển thị ngoài app (thông báo — B8.8). Đưa dần sang `strings.xml` ở V1.1.

---

## 3. Quyết định của owner (điền trước Gate G0)

> Hướng dẫn: đánh dấu `[x]` vào một lựa chọn, hoặc ghi ý kiến riêng vào dòng **Khác**. **Gemini không được sửa code liên quan đến mục nào chưa được đánh dấu.**

**D1 — `applicationId` (không đổi được sau khi lên Play)**
- [x] `io.github.muslimvn.app` (gắn với tổ chức GitHub `MuslimVN`, không cần sở hữu domain)
- [ ] `vn.muslimvn.app` (chỉ khi owner sở hữu domain `muslimvn.vn`)

**D2 — License mã nguồn**
- [x] GPL-3.0-or-later (copyleft mạnh; phổ biến ở F-Droid; **bắt buộc** nếu còn NewPipeExtractor)
- [ ] Apache-2.0 hoặc MIT (**chỉ** khi đã bỏ NewPipe, D3)

**D3 — YouTube / NewPipe (B6)**
- [ ] **CUT** (khuyến nghị): bỏ NewPipe + tải video; giữ danh sách qua RSS, bấm thì mở YouTube
- [x] Giữ (Giữ lại NewPipeExtractor)

**D4 — Podcast Muslim Central**
- [x] KEEP: thay ảnh giảng viên bằng artwork RSS/avatar; xác nhận quyền dùng logo Muslim Central
- [ ] HIDE cho V1

**D5 — Quran audio & Mushaf**
- [ ] Audio: KEEP stream (EveryAyah/Quran.com) sau khi Gemini kiểm điều khoản; Mushaf ảnh: **HIDE**
- [x] Giữ cả Mushaf và audio stream (owner xác nhận giữ)
- [ ] Ẩn cả hai; V1 chỉ chữ + bản dịch

**D6 — Học giả Việt Nam**
- [ ] KEEP Mách Zên qua IslamHouse; **HIDE** Gosaly Ahmad (kênh *Unofficial*) đến khi có phép
- [x] Owner nắm giữ kênh Gosaly và Mách Zên, đã xin phép & IslamHouse cho phép công khai cho developer.
- [ ] HIDE cả hai

**D7 — Âm thanh Adhan** (điền license từng file)
| File | Có quyền phân phối lại? | Nguồn/License |
|---|---|---|
| Mishary-Alafasi.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| hamad_daghriry.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| Ahmed-El-Kourdi.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| Nasser-Alqatami.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| Mansoor-Az-Zahrani.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| Rabeh-Ibn-Darah-Al-Jazairi.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |
| Fajaz_Azan.mp3 | ☑ Có ☐ Không ☐ Không rõ | AlAdhan |

**D8 — Quyền báo thức và full-screen**
- [x] **An toàn (khuyến nghị):** bỏ `USE_EXACT_ALARM` và `USE_FULL_SCREEN_INTENT`; dùng `SCHEDULE_EXACT_ALARM` + luồng xin quyền + thông báo hành động "Dừng Adhan"
- [ ] Giữ `USE_EXACT_ALARM` và khai báo với Play (owner chấp nhận rủi ro bị từ chối)

**D9 — Loại tài khoản Play Console**
- [x] Cá nhân, tạo **sau** 13/11/2023 → bắt buộc **closed test ≥12 tester, opt-in liên tục ≥14 ngày**
- [ ] Cá nhân tạo trước 13/11/2023 hoặc tài khoản tổ chức (được miễn)

**D10 — Vị trí mặc định khi chưa chọn (B7)**
- [x] TP. Hồ Chí Minh · [ ] Hà Nội · [ ] An Giang · [ ] Bắt buộc chọn, không có mặc định

**D11 — Nguồn bản dịch Quran tiếng Việt** (B11/B12): dịch giả/tổ chức: ______________ · License: ______________
**D12 — Phương pháp tính giờ mặc định `MUSLIMVN_DEFAULT` (18°/18°)**: nguồn/căn cứ: ______________ · Tài liệu đối chiếu: ______________

---

## 4. Kế hoạch thực thi theo lô

| Batch | Nội dung | Blocker xử lý | Cỡ | Gate |
|---|---|---|---|---|
| **0** | Vệ sinh repo, tài liệu nền (không đổi hành vi) | B4, B14 (một phần) | S | — |
| — | **Chờ owner điền §3** | | | **G0** |
| **1** | Danh tính & build: `applicationId`, `targetSdk 36`, signing, `dependenciesInfo`, toolchain, dọn JitPack | B1, B2, B3, B15 | S–M | |
| **2** | Gỡ SDK độc quyền: LocationManager, bỏ ML Kit, bỏ NewPipe/tải video | B5, B6 | L | **G1** |
| **3** | Lõi giờ cầu nguyện: mô hình vị trí, chọn thành phố, viết lại scheduler/boot/permission, icon, bỏ `runBlocking`, test | B7, B8, B16 | L | |
| **4** | Manifest, backup, cleartext, Room baseline, `GlobalScope` | B9, B10 | M | **G2** |
| **5** | Nội dung & pháp lý: `CONTENT_SOURCES.md`, xóa docx, sửa dữ liệu Quran, thay ảnh/âm thanh, màn Nguồn/Giấy phép/Privacy, disclaimer | B11, B12, B13 | M–L | |
| **6** | Store assets + metadata: fastlane, icon, screenshot, PRIVACY, Data Safety, listing | §6, §7 | M | |
| **7** | Xác minh cuối, tag `v1.0.0`, AAB, draft recipe F-Droid, báo cáo | §8 | S | **G3** |

**Song song hóa (gợi ý cho owner):** ngay sau **G2**, upload AAB lên track **Closed testing** để bắt đầu 14 ngày; trong lúc chờ, làm Batch 5–6 và chuẩn bị merge request F-Droid.

**Mỗi task** ghi vào `CHANGELOG_V1_PREP.md`: `ID · mô tả · file đổi · tiêu chí xong · cách kiểm thử · kết quả`.

---

## 5. Hướng dẫn kỹ thuật cho các mục khó

### 5.1 Ký release không dùng debug key (B3)
```kotlin
signingConfigs {
    if (hasReleaseKeystore) {
        create("release") {
            storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
}
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        // Không có keystore → KHÔNG ký (F-Droid tự ký). Tuyệt đối không dùng debug key.
        if (hasReleaseKeystore) signingConfig = signingConfigs.getByName("release")
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
// F-Droid / reproducible builds: không nhúng khối metadata dependency mã hóa
dependenciesInfo {
    includeInApk = false
    includeInBundle = false
}
```

### 5.2 Nguyên tắc vị trí và alarm (B7, B8)
- Một nguồn sự thật duy nhất: `SavedLocation(lat, lng, name, source, updatedAt)` trong DataStore.
- **UI/Onboarding** được phép xin quyền và cập nhật `SavedLocation`. **Receiver/Worker/Boot** chỉ **đọc** `SavedLocation`, không xin vị trí.
- `GetPrayerTimesUseCase` không được có `?: 10.7005` — nếu chưa có `SavedLocation` thì trả trạng thái "chưa chọn vị trí" và UI dẫn tới màn chọn.
- Scheduler tính giờ theo **ngày cụ thể** (`date = today` rồi `today+1`), không cộng 24h.
- Sau mỗi lần alarm nổ: `finally { đặt alarm kế tiếp }`. Đăng ký thêm `MY_PACKAGE_REPLACED`, `TIMEZONE_CHANGED`, `TIME_SET`.
- Nếu `!canScheduleExactAlarms()` → hiện banner "Bật quyền Báo thức & nhắc nhở để nhận Adhan đúng giờ" dẫn tới `Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM`.

### 5.3 `proguard-rules.pro`
Sau khi bỏ NewPipe/ML Kit, rà lại file: xóa rule thừa, giữ rule cho Room/Hilt/Retrofit/Adhan. **Test kỹ bản release** (R8 có thể làm gãy Gson/Retrofit reflection). Rule `-keep class com.example.muslimvn.data.repository.NameAllahRepositoryImpl$*` phải đi theo package thực tế.

---

## 6. Google Play — checklist

**Kỹ thuật**
- [ ] `targetSdk 36`; xuất **AAB** (`bundleRelease`); bật **Play App Signing**; upload key riêng, **không commit**.
- [ ] Khai báo **Foreground Service** (`mediaPlayback` cho `AdhanService` và `PlaybackService`) trên Play Console, kèm mô tả/clip minh họa.
- [ ] Nếu giữ `USE_EXACT_ALARM` hoặc `USE_FULL_SCREEN_INTENT` (D8): hoàn thành biểu mẫu khai báo tương ứng; nếu bỏ thì không cần.
- [ ] Xin `POST_NOTIFICATIONS` khi có ngữ cảnh (đã có ở Onboarding) và xử lý từ chối.
- [ ] Không có quyền vị trí nền; vị trí chỉ COARSE.

**Hồ sơ**
- [ ] Privacy policy công khai (URL trang web) khớp thực tế: vị trí xử lý **trên thiết bị**; danh sách dịch vụ bên thứ ba được gọi (§1.3) — Aladhan, Quran.com, EveryAyah, IslamHouse, HadeethEnc, Muslim Central, YouTube RSS/thumbnail — chúng nhận **địa chỉ IP** của người dùng.
- [ ] **Data safety form** — soạn nháp `docs/release/play_data_safety.md`: không thu thập tài khoản/định danh; không quảng cáo; không analytics; nêu rõ vị trí không rời thiết bị (nếu bỏ `Geocoder`).
- [ ] Phân loại nội dung (IARC), **đối tượng người dùng không nhắm trẻ em**, danh mục app.
- [ ] Store listing tiếng Việt: tên ≤30 ký tự, mô tả ngắn ≤80, mô tả đầy đủ ≤4000, icon 512×512, feature graphic 1024×500, **≥4 ảnh chụp màn hình** (Home/giờ cầu nguyện, Quran, Qibla, Azkar).
- [ ] Ảnh chụp không chứa ảnh người thật chưa có phép; không dùng logo bên thứ ba.
- [ ] **Closed testing:** ≥12 tester thật, opt-in liên tục ≥14 ngày (nếu D9 áp dụng). Chuẩn bị 15–20 người để dự phòng; nên có kênh phản hồi (GitHub Issues).

---

## 7. F-Droid — checklist

**Điều kiện đầu vào**
- [ ] `LICENSE` FOSS ở thư mục gốc (D2); mọi dependency có license tương thích.
- [ ] **Không** có SDK độc quyền/tracking: `git grep -nE "com\.google\.android\.gms|com\.google\.mlkit|firebase|crashlytics|admob"` → rỗng; dependency tree sạch (§1.4).
- [ ] Không có file nhị phân dựng sẵn trong repo (`.apk`, `.aab`, `.jar`, `.aar`, `.so`) ngoại trừ `gradle/wrapper/gradle-wrapper.jar`.
- [ ] Repository Maven chỉ `google()` + `mavenCentral()` (bỏ JitPack sau B6).
- [ ] `./gradlew assembleRelease` chạy **không cần keystore** và không cần tải toolchain JDK qua mạng (B15).
- [ ] `dependenciesInfo { includeInApk = false; includeInBundle = false }` (mục 5.1).
- [ ] Asset không tự do → sẽ bị gắn anti-feature `NonFreeAssets`; tốt nhất loại hoặc thay thế (B11).

**Metadata trong repo (fastlane)**
```
fastlane/metadata/android/
└── vi/
    ├── title.txt                    (≤30 ký tự)
    ├── short_description.txt        (≤80 ký tự)
    ├── full_description.txt         (≤4000 ký tự)
    ├── changelogs/1.txt             (theo versionCode, ≤500 byte)
    └── images/
        ├── icon.png                 (512×512)
        ├── featureGraphic.png       (1024×500)
        └── phoneScreenshots/1.png … (≥4)
```
- [ ] Mô tả **trung thực về các dịch vụ mạng** (nêu rõ app tải dữ liệu từ những nguồn nào).
- [ ] Tag git `v1.0.0` khớp `versionName`, `versionCode` tăng dần.
- [ ] GitHub Issues bật; có mục liên hệ.
- [ ] Gemini soạn nháp `docs/release/fdroid_recipe_draft.yml` (Categories, License, SourceCode, IssueTracker, Builds, AutoUpdateMode). **Owner** là người gửi merge request lên `fdroiddata`.

**Ghi chú:**
- F-Droid ký APK bằng khóa riêng → APK F-Droid và Play **không cập nhật chéo** nhau (bình thường); ghi vào README.
- Google *Developer Verification* có hiệu lực từ 30/09/2026 ở Brazil, Indonesia, Singapore, Thailand (toàn cầu dự kiến 2027). Chưa chặn V1 ở Việt Nam; app phát hành qua Play thì được đăng ký tự động. Ghi nhận trong báo cáo cuối.

---

## 8. Xác minh cuối và Definition of Done

### 8.1 Lệnh bắt buộc
```
./gradlew clean lint testDebugUnitTest assembleRelease bundleRelease
./gradlew :app:dependencies --configuration releaseRuntimeClasspath > build/deps.txt
git grep -nEi "schabi|newpipe|com\.google\.android\.gms|com\.google\.mlkit|firebase|jitpack"
git grep -nEi "api[_-]?key|secret|password|token" -- ':!*.md'
apkanalyzer manifest print app/build/outputs/apk/release/*.apk
```
- Xác nhận: package mới, `targetSdkVersion=36`, chỉ còn các permission đã duyệt, APK nhỏ hơn đáng kể (bỏ docx 20 MB, có thể bỏ mp3 và ML Kit).

### 8.2 Ma trận test thủ công (thiết bị thật + emulator API 26 và API 36)
| Tình huống | Kỳ vọng |
|---|---|
| Từ chối vị trí → chọn "Hà Nội" thủ công | Giờ theo Hà Nội, nhãn vị trí đúng |
| Máy không có Google Play Services (emulator AOSP) | Vẫn lấy được vị trí, không crash |
| Từ chối thông báo | App giải thích, không crash |
| Chưa cấp exact alarm | Có banner; vẫn nhận thông báo (có thể trễ nhẹ) |
| Reboot / cập nhật app / đổi múi giờ / đổi giờ hệ thống | Lịch nhắc được đặt lại đúng |
| Doze / Battery saver | Adhan vẫn nổ hoặc app cảnh báo rõ |
| Đang ở nền, alarm nổ khi máy khóa | Có âm thanh hoặc thông báo thường (fallback) |
| Offline hoàn toàn | Giờ cầu nguyện, Qibla, Quran chữ, Azkar, Zakat dùng được |
| Font 200%, dark mode, RTL Ả Rập | Không vỡ layout; dấu harakat đúng |
| Nâng cấp từ bản tự build cũ | Tracker/bookmark còn nguyên |

### 8.3 Definition of Done
- [ ] Owner đã điền đủ §3; mọi mục P0 (B1–B14) đều `PASS`.
- [ ] `applicationId` mới, `targetSdk 36`, không ký bằng debug key.
- [ ] Dependency tree **không** có gms/mlkit/firebase/newpipe.
- [ ] Giờ cầu nguyện đúng theo vị trí đã chọn, kể cả khi alarm nổ ở nền; đã qua test golden.
- [ ] Không còn asset `KHÔNG RÕ` license; `CONTENT_SOURCES.md` đầy đủ; màn Nguồn & Giấy phép hoạt động.
- [ ] Room có schema export + baseline; DB người dùng không còn `fallbackToDestructiveMigration`.
- [ ] Repo có `LICENSE`, `README`, `PRIVACY`, `CHANGELOG`, fastlane; không còn `app/release/`, `.idea/`, `.artifacts/`.
- [ ] Đã tag `v1.0.0`; AAB sẵn sàng; bản nháp Data Safety và recipe F-Droid đã có.

---

## 9. Báo cáo cuối (`docs/release/RELEASE_READINESS_REPORT.md`)
1. **Go / No-Go** riêng cho Google Play và F-Droid, kèm lý do.
2. Bảng blocker còn lại (ID, file:dòng, mức, công sức).
3. Danh sách thay đổi theo commit.
4. Rủi ro còn lại (nội dung/bản quyền, điều khoản API bên thứ ba, chính sách Play).
5. Việc owner phải tự làm: tạo tài khoản Play, tuyển tester, host Privacy Policy, gửi MR `fdroiddata`, quyết định có dọn lịch sử git (`git filter-repo`) hay không.

---

## 10. TUYỆT ĐỐI KHÔNG
- Không thêm tính năng mới hoặc dependency mới ngoài phạm vi V1.
- Không dùng debug key để ký bản phát hành.
- Không commit keystore, mật khẩu, token, file `.env`.
- Không sao chép nội dung/asset từ nguồn không rõ license; không tự "hợp pháp hóa" bằng cách đổi tên file.
- Không mở/phân tích `app/release/app-release.apk` (chỉ xóa).
- Không viết lại lịch sử git, không `push --force`.
- Không tự chọn thay owner ở §3; không tuyên bố nội dung tôn giáo là "chính thống/được chứng nhận" khi chưa có nguồn xác thực.

---

*Bắt đầu từ **Batch 0**, rồi dừng ở **G0** và báo owner điền §3.*
