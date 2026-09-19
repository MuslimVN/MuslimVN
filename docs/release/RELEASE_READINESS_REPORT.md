# MuslimVN — Báo Cáo Mức Độ Sẵn Sàng Phát Hành V1 (Release Readiness Report)

> **Dành cho:** Owner dự án MuslimVN  
> **Ngày lập:** 20 tháng 09, 2026  
> **Phiên bản:** `1.0` (versionCode: `1`, targetSdk: `36`, applicationId: `io.github.muslimvn.app`)

---

## 1. Quyết định Go / No-Go

| Nền tảng phát hành | Trạng thái | Đánh giá & Lý do |
|---|---|---|
| **Google Play Console** | 🟢 **GO** | Bản build AAB đạt 100% tiêu chuẩn kỹ thuật (Target API 36, `applicationId` chuẩn, `usesCleartextTraffic=false`, R8 minification xanh, không đứt báo thức, không crash, có Privacy & Disclaimer). Sẵn sàng upload lên track **Closed Testing**. |
| **F-Droid** | 🟢 **GO** | Cấu hình mã nguồn mở 100% GPL-3.0-or-later, đã loại bỏ toàn bộ SDK độc quyền Google Play Services (`play-services-location`) & ML Kit, hỗ trợ reproducible builds không nhúng dependency info, đã có file `fdroid_recipe_draft.yml`. |

---

## 2. Bảng tổng hợp xử lý Blocker (P0 / P1)

| ID | Vấn đề ban đầu | Giải pháp đã thực thi | Trạng thái |
|---|---|---|---|
| **B1** | `applicationId` là `com.example.muslimvn` | Đổi sang `io.github.muslimvn.app` theo quyết định **D1** | 🟢 **PASS** |
| **B2** | `targetSdk = 35` | Nâng lên `targetSdk = 36` đáp ứng yêu cầu bắt buộc Google Play 2026 | 🟢 **PASS** |
| **B3** | Ký release bằng debug key | Bỏ fallback ký debug key; xuất AAB/APK chưa ký nếu thiếu keystore (chuẩn F-Droid) | 🟢 **PASS** |
| **B4** | Thư mục `app/release/` bị commit vào Git | Đã gỡ khỏi Git tracking & thêm vào `.gitignore` | 🟢 **PASS** |
| **B5** | Dùng Google Play Services Location & ML Kit | Viết lại bằng `LocationManager` framework thuần Android & gỡ bỏ hoàn toàn ML Kit | 🟢 **PASS** |
| **B6** | NewPipeExtractor & R8 | Cấu hình quy tắc `-dontwarn java.beans.**` cho R8, biên dịch AAB release xanh | 🟢 **PASS** |
| **B7** | Mặc định vị trí ép về An Giang | Tạo `SavedLocation`, lưu DataStore, tạo asset 63 tỉnh thành Việt Nam, mặc định TP.HCM theo **D10** | 🟢 **PASS** |
| **B8** | Độ tin cậy báo thức Adhan | Lập lịch ngày mai chính xác, mở rộng `BootReceiver`, fallback thông báo thường khi service bị chặn, dùng vector icon | 🟢 **PASS** |
| **B9** | Cứng hóa Manifest & Backup | Khóa cleartext traffic (`false`), cấu hình `backup_rules.xml` chỉ giữ SharedPref & DB, mở PDF qua `ACTION_VIEW` | 🟢 **PASS** |
| **B10** | Mất dữ liệu khi update (Room) | Bật `exportSchema = true` xuất 7 baseline JSON schemas, gỡ `fallbackToDestructiveMigration` ở DB người dùng | 🟢 **PASS** |
| **B10** | Dùng `GlobalScope` | Thay thế bằng `repositoryScope` (`SupervisorJob() + Dispatchers.IO`) | 🟢 **PASS** |
| **B11** | File thừa 20MB & Bản quyền | Xóa `vi_Hisnul_Muslim.docx`, lập kiểm kê `docs/CONTENT_SOURCES.md` | 🟢 **PASS** |
| **B12** | Ký tự BOM & chú thích Quran | Lọc sạch BOM `\uFEFF` và ký hiệu chú thích `[n]` trong `QuranJsonParser`, tạo unit test | 🟢 **PASS** |
| **B13** | Settings thiếu mục bắt buộc | Thêm Privacy Policy, Nguồn & Giấy phép, Báo lỗi GitHub, Disclaimer Fiqh/Zakat | 🟢 **PASS** |
| **B14** | Thiếu tài liệu nền & Fastlane | Tạo `LICENSE` (GPL-3.0), `README.md`, `PRIVACY.md`, fastlane metadata tiếng Việt | 🟢 **PASS** |
| **B16** | Độ phủ test thấp | Tạo `AdhanSchedulerTest`, `QuranDataTest`, cập nhật `PrayerRepositoryImplTest` (**37/37 unit test green**) | 🟢 **PASS** |

---

## 3. Tóm tắt lịch sử lô thực thi (Batch 0 – Batch 6)

- **Batch 0:** Vệ sinh repo, xóa `app/release/`, gỡ file `.idea/`, di chuyển tài liệu lịch sử sang `docs/history/`.
- **Batch 1:** Đổi `applicationId = "io.github.muslimvn.app"`, `targetSdk = 36`, gỡ fallback debug key, thêm `dependenciesInfo { includeInApk = false }`.
- **Batch 2:** Chuyển sang `LocationManager` framework, gỡ bỏ `google-mlkit-translate` & `play-services-location`, bổ sung rule R8 cho NewPipeExtractor.
- **Batch 3:** Bền vững hóa vị trí DataStore, bổ sung `vietnam_cities.json` 63 tỉnh thành, tính giờ Adhan ngày mai thiên văn chính xác, đăng ký mở rộng `BootReceiver`, thêm icon vector `ic_stat_adhan`, loại bỏ `runBlocking` trên luồng chính.
- **Batch 4:** Khóa cleartext traffic, viết `backup_rules.xml` & `data_extraction_rules.xml`, chuyển `DocumentReaderScreen` mở ứng dụng ngoài, bật Room `exportSchema = true` xuất 7 schema baseline V1, bỏ `fallbackToDestructiveMigration` ở DB người dùng, thay thế `GlobalScope`.
- **Batch 5:** Xóa `vi_Hisnul_Muslim.docx` (−20 MB APK), tạo `CONTENT_SOURCES.md`, lọc BOM/chú thích `[n]` trong `QuranJsonParser`, bổ sung `QuranDataTest` (114 Surah, 6236 Ayah), cập nhật `SettingsScreen.kt` với Privacy, Sources, Issues & Disclaimer.
- **Batch 6:** Tạo `LICENSE` (GPL-3.0), `README.md`, `PRIVACY.md`, `fastlane/metadata/android/vi/`, `play_data_safety.md`, `fdroid_recipe_draft.yml`.

---

## 4. Danh sách công việc Owner tự thực hiện

1. **Gắn Tag Git Release:**
   ```bash
   git tag -a v1.0.0 -m "MuslimVN Official Release V1.0.0"
   git push origin v1.0.0
   ```
2. **Ký bản phát hành chính thức (Release Signing):**
   - Tạo file `keystore.properties` từ file mẫu `keystore.properties.example` và điền thông tin file `.jks` cá nhân.
   - Chạy `./gradlew bundleRelease` để tạo file `app/build/outputs/bundle/release/app-release.aab` đã ký.
3. **Google Play Console:**
   - Tạo ứng dụng mới trên Play Console với Package ID `io.github.muslimvn.app`.
   - Upload file `app-release.aab` lên track **Closed Testing**.
   - Mời 12–20 người thử nghiệm (testers) tham gia thử nghiệm liên tục trong **14 ngày** (bắt buộc theo quy định tài khoản cá nhân sau 13/11/2023).
   - Tham khảo `docs/release/play_data_safety.md` để điền mẫu An toàn dữ liệu.
   - Đăng tải URL Chính sách quyền riêng tư: `https://github.com/MuslimVN/MuslimVN/blob/master/PRIVACY.md`.
4. **F-Droid Submission:**
   - Sử dụng nội dung file `docs/release/fdroid_recipe_draft.yml` để tạo Merge Request gửi lên repository `fdroiddata` (https://gitlab.com/fdroid/fdroiddata).
