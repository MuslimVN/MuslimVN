# MuslimVN — Nguồn gốc & Giấy phép Nội dung (Content Sources & Licenses)

> **Tài liệu kiểm kê nội dung ứng dụng MuslimVN V1**

---

## 1. Mã nguồn & Thư viện
- **Mã nguồn ứng dụng:** GNU General Public License v3.0 or later (GPL-3.0-or-later).
- **Thư viện bên thứ ba:**
  - Android Jetpack, Compose, Hilt, Room, Media3, DataStore, WorkManager, Paging: Apache-2.0.
  - Adhan (`com.batoulapps.adhan`): MIT License.
  - Coil: Apache-2.0.
  - Retrofit, OkHttp, Gson: Apache-2.0.
  - Lottie Compose: Apache-2.0.
  - NewPipeExtractor (`com.github.TeamNewPipe:NewPipeExtractor`): GPL-3.0-or-later.

---

## 2. Phông chữ (Fonts)
- **Amiri Font** (`res/font/amiri_*.ttf`): SIL Open Font License 1.1 (OFL).
- **Inter Font** (`res/font/inter_variable.ttf`): SIL Open Font License 1.1 (OFL).

---

## 3. Văn bản & Dữ liệu Tôn giáo

### 3.1 Kinh Quran & Bản dịch
- **Văn bản Ả Rập (Arabic Text):** Nguồn Tanzil.net / Quran.com API.
- **Bản dịch tiếng Việt (`assets/quran_vi.json`):** Bản dịch ý nghĩa Kinh Quran tiếng Việt.
- **Tafsir:** Quran.com API v4 (Tafsir Ibn Kathir / Saadi / Khiar).

### 3.2 Giờ cầu nguyện & Lịch Hijri
- **Thuật toán tính giờ cầu nguyện:** Thư viện Adhan (bởi Batoul Apps - MIT License).
- **Phương pháp mặc định:** `MUSLIMVN_DEFAULT` (Góc Fajr 18°, Góc Isha 18°).
- **Lịch Hijri:** Aladhan API (`api.aladhan.com`) kết hợp tính toán địa phương offline.

### 3.3 Azkar & 99 Danh xưng Allah
- **Azkar (`assets/azkar_vi.json`):** Tuyển tập Azkar từ Hisnul Muslim (Pháo đài người Muslim).
- **99 Danh xưng Allah (`assets/NameAllah.json`):** Tên và ý nghĩa 99 danh xưng của Allah bằng tiếng Việt.

### 3.4 Hadith & Học giả
- **Hadith hằng ngày:** HadeethEnc API (`hadeethenc.com`).
- **Podcast Học giả thế giới:** RSS Feed Muslim Central (`rss.muslimcentral.com`).
- **Tài liệu Mách Zên:** IslamHouse API v3 (`api3.islamhouse.com`).
- **Kênh Youtube học giả Việt Nam (Mách Zên & Gosaly Ahmad):** Đã có sự cho phép công khai từ chủ sở hữu kênh và IslamHouse.

---

## 4. Âm thanh (Audio Assets)
- **Âm thanh Adhan (`assets/audio/adhan/*.mp3`):** Nguồn công khai từ AlAdhan Project.
  - `Mishary-Alafasi.mp3`
  - `hamad_daghriry.mp3`
  - `Ahmed-El-Kourdi.mp3`
  - `Nasser-Alqatami.mp3`
  - `Mansoor-Az-Zahrani.mp3`
  - `Rabeh-Ibn-Darah-Al-Jazairi.mp3`
  - `Fajaz_Azan.mp3`
- **Âm thông báo (`res/raw/muslimvn_notification.mp3`):** Âm báo tùy chỉnh của ứng dụng.

---

## 5. Tuyên bố Miễn trừ Trách nhiệm (Disclaimer)
> MuslimVN là ứng dụng độc lập được phát triển bởi cộng đồng MuslimVN. Ứng dụng không liên kết chính thức, không được tài trợ hoặc bảo trợ bởi các học giả, Qari, tổ chức Muslim Central hay IslamHouse. Mọi thông tin giờ cầu nguyện và Zakat mang tính chất tham khảo, người dùng nên đối chiếu với cộng đồng và giáo sĩ địa phương.
