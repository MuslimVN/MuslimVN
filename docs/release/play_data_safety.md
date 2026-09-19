# Hướng dẫn điền Mẫu An toàn Dữ liệu (Data Safety Form) — Google Play Console

> **Mục đích:** Hướng dẫn Owner điền chính xác Mẫu An toàn Dữ liệu (Data Safety) trên Google Play Console cho bản phát hành V1.

---

## 1. Thu thập & Chia sẻ Dữ liệu (Data Collection & Sharing)

- **Ứng dụng có thu thập hoặc chia sẻ bất kỳ loại dữ liệu người dùng nào không?**
  👉 Chọn: **KHÔNG (No)**
  *(Giải thích: Tọa độ vị trí được xử lý hoàn toàn trên thiết bị thông qua LocationManager framework và không bao giờ rời khỏi thiết bị; ứng dụng không gửi vị trí hay định danh lên bất kỳ máy chủ nào).*

- **Tất cả dữ liệu người dùng được ứng dụng thu thập có được mã hóa khi truyền không?**
  👉 Chọn: **CÓ (Yes)** *(Mọi yêu cầu mạng tải RSS/Audio đều dùng HTTPS)*.

- **Ứng dụng có cung cấp phương thức để người dùng yêu cầu xóa dữ liệu của họ không?**
  👉 Chọn: **KHÔNG ÁP DỤNG / KHÔNG CẦN TÀI KHOẢN** *(Vì ứng dụng không lưu trữ dữ liệu người dùng trên máy chủ xa)*.

---

## 2. Chi tiết các Quyền Hạn (Permissions)

| Quyền hạn | Lý do khai báo trên Google Play |
|---|---|
| `ACCESS_COARSE_LOCATION` | **Tính toán giờ cầu nguyện & hướng Qibla** *(Chỉ xử lý trên thiết bị)* |
| `POST_NOTIFICATIONS` | **Phát thông báo giờ Adhan & lời nhắc** |
| `SCHEDULE_EXACT_ALARM` | **Đặt lịch báo thức mốc giờ Adhan chính xác** |
| `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | **Phát âm thanh đọc Quran & Adhan nền** |

---

## 3. Tuyên bố về Đối tượng người dùng (Target Audience)

- **Ứng dụng có hướng tới trẻ em dưới 13 tuổi không?**
  👉 Chọn: **KHÔNG (No)** *(Ứng dụng phục vụ mọi đối tượng người dùng đại chúng - General Audience)*.
