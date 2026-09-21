# Chính sách Quyền riêng tư (Privacy Policy) — MuslimVN

> **Ngày cập nhật:** 20 tháng 09, 2026

Cộng đồng phát triển **MuslimVN** ("chúng tôi") cam kết tôn trọng và bảo vệ tuyệt đối quyền riêng tư của người dùng. Chính sách quyền riêng tư này giải thích cách ứng dụng **MuslimVN** xử lý thông tin trên thiết bị của bạn.

---

## 1. Thu thập và Xử lý Vị trí (Location Data)

- **Mục đích duy nhất:** Vị trí địa lý của bạn được sử dụng **duy nhất** cho hai tính năng:
  1. Tính toán thời gian 5 lễ cầu nguyện hằng ngày theo tọa độ thiên văn.
  2. Xác định góc hướng Qibla về Kaaba (Makkah).
- **Xử lý trên thiết bị (On-Device Processing):** Tọa độ địa lý được truy vấn thông qua dịch vụ `LocationManager` mặc định của hệ điều hành Android và được tính toán hoàn toàn trực tiếp trên thiết bị của bạn.
- **Không chia sẻ:** Tọa độ vị trí của bạn **KHÔNG BAO GIỜ** được gửi về bất kỳ máy chủ bên thứ ba nào, không lưu trữ từ xa, và không được dùng cho mục đích quảng cáo hay theo dõi.

---

## 2. Quyền Truy cập Thiết bị

1. **Quyền Vị trí (`ACCESS_COARSE_LOCATION`):** Để tính giờ cầu nguyện và hướng Qibla. Người dùng có quyền từ chối và chọn thành phố thủ công từ danh sách 63 tỉnh thành Việt Nam có sẵn trong app.
2. **Quyền Thông báo (`POST_NOTIFICATIONS`):** Để phát âm thanh Adhan và hiển thị thông báo nhắc giờ cầu nguyện.
3. **Quyền Báo thức chính xác (`SCHEDULE_EXACT_ALARM`):** Để đặt lịch báo thức Adhan đúng mốc phút thiên văn.
4. **Quyền Khởi động cùng hệ thống (`RECEIVE_BOOT_COMPLETED`):** Để tự động lập lại lịch báo thức Adhan sau khi thiết bị khởi động lại hoặc đổi múi giờ.

---

## 3. Không Theo dõi & Không Quảng cáo

- **Không Analytics / Tracking:** Chúng tôi không nhúng bất kỳ công cụ phân tích hành vi người dùng nào (như Google Analytics, Firebase Analytics, Flurry, v.v.).
- **Không Quảng cáo (Ad-Free):** Ứng dụng hoàn toàn không chứa bất kỳ mã quảng cáo hay mạng lưới tiếp thị nào.
- **Không Yêu cầu Tài khoản:** Ứng dụng không yêu cầu đăng ký, đăng nhập hay thu thập thông tin định danh cá nhân (tên, email, số điện thoại).

---

## 4. Các Dịch vụ Mạng Bên thứ Ba (Third-Party Network Services)

Để cung cấp một số nội dung trực tuyến (như podcast hay danh sách video), ứng dụng kết nối trực tiếp tới các máy chủ endpoint công khai sau:

| Dịch vụ | Endpoint | Mục đích sử dụng |
|---|---|---|
| **Aladhan API** | `api.aladhan.com` | Tải dữ liệu lịch Hijri |
| **Quran.com API** | `api.quran.com` | Tải thời lượng đọc Ayah & Tafsir |
| **Quran Audio CDN** | `audio.qurancdn.com`, `everyayah.com` | Stream/Tải âm thanh đọc Kinh Quran |
| **IslamHouse API** | `api3.islamhouse.com` | Tải tài liệu bài giảng Mách Zên |
| **HadeethEnc API** | `hadeethenc.com` | Tải trích dẫn Hadith hằng ngày |
| **Muslim Central** | `rss.muslimcentral.com` | Tải RSS feed podcast học giả thế giới |
| **YouTube RSS** | `youtube.com/feeds/videos.xml` | Tải danh sách video bài giảng |

*Lưu ý: Khi kết nối tới các endpoint trên, máy chủ bên thứ ba có thể ghi nhận địa chỉ IP tiêu chuẩn của thiết bị theo giao thức mạng HTTPS.*

---

## 5. Lưu trữ Dữ liệu Cụ bộ (Local Storage & Backup)

- Tất cả dữ liệu cài đặt, danh sách Azkar yêu thích, lịch sử theo dõi cầu nguyện (Tracker) và tính toán Zakat được lưu trữ hoàn toàn cục bộ trên thiết bị qua DataStore và Room Database.
- Khi người dùng sử dụng tính năng Sao lưu Android (Android Cloud Backup), dữ liệu cài đặt cá nhân được mã hóa và bảo vệ theo chính sách tài khoản Google của bạn. Dữ liệu file tạm/media không bao giờ đưa vào backup.

---

## 6. Liên hệ & Đóng góp

Nếu bạn có bất kỳ câu hỏi nào về Chính sách quyền riêng tư này hoặc muốn báo lỗi ứng dụng, vui lòng liên hệ:
- **Tác giả & Lead Developer:** Abdol Hamid ([abdolhamid.dev@gmail.com](mailto:abdolhamid.dev@gmail.com))
- **GitHub Issues:** [https://github.com/MuslimVN/MuslimVN/issues](https://github.com/MuslimVN/MuslimVN/issues)
