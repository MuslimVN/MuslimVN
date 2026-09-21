# Privacy Policy — MuslimVN

> **Last Updated:** September 20, 2026

The **MuslimVN** development project ("we") is committed to strictly respecting and protecting user privacy. This privacy policy explains how the **MuslimVN** application processes data on your device.

---

## 1. Location Data Collection & Processing

- **Sole Purpose:** Your geographic location is used **exclusively** for two features:
  1. Calculating the 5 daily prayer times based on astronomical coordinates.
  2. Determining the Qibla direction towards the Kaaba (Makkah).
- **On-Device Processing:** Geographic coordinates are queried through the Android system default `LocationManager` service and calculated 100% locally on your device.
- **No Data Sharing:** Your location coordinates are **NEVER** sent to any third-party servers, never stored remotely, and never used for tracking or advertising.

---

## 2. Device Permissions

1. **Location Permission (`ACCESS_COARSE_LOCATION`):** Required for prayer times and Qibla compass calculations. Users can decline location permission and manually select a city from all 63 provinces of Vietnam.
2. **Notification Permission (`POST_NOTIFICATIONS`):** Required for playing Adhan audio alerts and prayer reminders.
3. **Exact Alarm Permission (`SCHEDULE_EXACT_ALARM`):** Required for scheduling Adhan alarms precisely at astronomical minute intervals.
4. **Boot Completed Permission (`RECEIVE_BOOT_COMPLETED`):** Required to automatically reschedule Adhan alarms after device reboot or timezone changes.

---

## 3. Zero Tracking & No Advertisements

- **No Analytics or Tracking:** We do not embed any user behavior analytics SDKs (such as Google Analytics, Firebase Analytics, Flurry, etc.).
- **100% Ad-Free:** The application contains zero advertisements or marketing tracking code.
- **No Account Required:** The application requires no registration, login, or collection of personally identifiable information (name, email, phone number).

---

## 4. Third-Party Network Services

To provide online content (such as scholar podcasts or video listings), the application connects directly to the following public endpoint APIs:

| Service | Endpoint | Purpose |
|---|---|---|
| **Aladhan API** | `api.aladhan.com` | Fetching Hijri calendar data |
| **Quran.com API** | `api.quran.com` | Fetching Ayah recitation audio durations & Tafsir |
| **Quran Audio CDN** | `audio.qurancdn.com`, `everyayah.com` | Streaming/downloading Quran recitations |
| **IslamHouse API** | `api3.islamhouse.com` | Fetching Mach Zen lecture documents |
| **HadeethEnc API** | `hadeethenc.com` | Fetching daily Hadith quotes |
| **Muslim Central** | `rss.muslimcentral.com` | Fetching world scholar podcast RSS feeds |
| **YouTube RSS** | `youtube.com/feeds/videos.xml` | Fetching scholar video lecture feeds |

*Note: When connecting to these public HTTPS endpoints, third-party servers may log standard client IP addresses per standard HTTPS networking protocols.*

---

## 5. Local Data Storage & Backup

- All application settings, favorite Azkar lists, prayer progress history (Tracker), and Zakat calculations are stored 100% locally on your device using DataStore and Room Database.
- When Android Cloud Backup is enabled, user settings are encrypted and protected under your Google Account backup policies. Temporary media cache files are never included in backups.

---

## 6. Contact & Inquiries

If you have any questions regarding this Privacy Policy or wish to report an issue, please contact:
- **Lead Developer:** Abdol Hamid ([abdolhamid.dev@gmail.com](mailto:abdolhamid.dev@gmail.com))
- **GitHub Issues:** [https://github.com/MuslimVN/MuslimVN/issues](https://github.com/MuslimVN/MuslimVN/issues)
