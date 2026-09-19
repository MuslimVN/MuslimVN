# MuslimVN (Muslim Vietnam)

**MuslimVN** là ứng dụng di động mã nguồn mở dành riêng cho cộng đồng Muslim tại Việt Nam, cung cấp giờ cầu nguyện offline chính xác, hướng Qibla, Kinh Quran chữ & âm thanh, lịch Hijri, tính toán Zakat, bộ Azkar và nội dung học giả phong phú.

---

## 🌟 Tính năng chính

- 🕌 **Giờ cầu nguyện & Adhan Offline:** Tính toán giờ cầu nguyện chuẩn xác bằng thuật toán thiên văn Adhan (`MUSLIMVN_DEFAULT`), hoạt động offline không cần mạng. Nhắc giờ Adhan linh hoạt theo tùy chọn thông báo.
- 🧭 **Hướng Qibla:** Xác định hướng Qibla chính xác kết hợp cảm biến thiết bị và vị trí địa lý.
- 📖 **Kinh Quran:** Đọc 114 Surah kèm bản dịch tiếng Việt sạch sẽ, hỗ trợ nghe phát âm thanh recitation và xem ảnh Mushaf.
- 📅 **Lịch Hijri:** Theo dõi lịch Hồi giáo kèm điều chỉnh lệch ngày thủ công và các sự kiện tôn giáo lớn trong năm.
- 🧮 **Máy tính Zakat:** Tính toán Zakat Tiền mặt, Vàng, Bạc, Chứng khoán, Bất động sản và Nông nghiệp theo Fiqh chuẩn kèm disclaimer.
- 🤲 **Bộ Azkar & 99 Danh xưng Allah:** Đọc và lưu danh sách Azkar yêu thích hàng ngày từ Hisnul Muslim.
- 🎙️ **Podcast & Bài giảng Học giả:** Nghe Podcast học giả thế giới (Muslim Central) và bài giảng của học giả Việt Nam (Mách Zên & Gosaly Ahmad).

---

## 🛠️ Công nghệ & Kiến trúc

- **Ngôn ngữ:** 100% Kotlin
- **UI:** Jetpack Compose + Material 3 Adaptive Navigation
- **Điều hướng:** Jetpack Navigation 3
- **Kiến trúc:** Clean Architecture + MVVM + Repository Pattern
- **Dependency Injection:** Hilt
- **Cơ sở dữ liệu:** Room Database (chốt Baseline Schema V1, bảo vệ dữ liệu người dùng khi cập nhật)
- **Lưu trữ Cài đặt:** DataStore Preferences
- **Quản lý Nền:** WorkManager + BroadcastReceiver
- **Phát Âm thanh:** Media3 ExoPlayer & Session
- **Vị trí:** Android Framework `LocationManager` (không phụ thuộc Google Play Services)

---

## 🚀 Hướng dẫn Biên dịch (Build Instructions)

### Yêu cầu môi trường
- Android Studio Ladybug (hoặc mới hơn)
- JDK 17 hoặc JDK 21 (ghim tương thích AGP)
- Android SDK 36 (compileSdk 37, minSdk 26, targetSdk 36)

### Lệnh build cơ bản
```bash
# Clone repository
git clone https://github.com/MuslimVN/MuslimVN.git
cd MuslimVN

# Biên dịch bản Debug
./gradlew assembleDebug

# Chạy Unit Tests
./gradlew testDebugUnitTest

# Biên dịch bản Release APK / AAB
./gradlew assembleRelease bundleRelease
```

### Cấu hình Ký bản phát hành (Keystore Setup)
1. Copy file mẫu: `cp keystore.properties.example keystore.properties`
2. Mở file `keystore.properties` và điền thông tin file `.jks`:
   ```properties
   storeFile=your_keystore.jks
   storePassword=your_password
   keyAlias=your_alias
   keyPassword=your_password
   ```
3. Chạy `./gradlew assembleRelease` để tạo APK phát hành đã ký. Nếu không có file `keystore.properties`, Gradle sẽ tự động tạo AAB/APK chưa ký (dành cho F-Droid tự ký).

---

## 📜 Giấy phép (License)

- **Mã nguồn:** [GNU General Public License v3.0](LICENSE) (GPL-3.0-or-later).
- **Chi tiết bản quyền tài sản & dữ liệu:** Xem tại [CONTENT_SOURCES.md](docs/CONTENT_SOURCES.md).
- **Chính sách quyền riêng tư:** Xem tại [PRIVACY.md](PRIVACY.md).
