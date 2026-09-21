# Content Sources & Licenses — MuslimVN

> **Application Content Audit & Attribution Document**

---

## 1. Source Code & Libraries
- **Application Source Code:** GNU General Public License v3.0 or later (GPL-3.0-or-later) with Non-Commercial Terms.
- **Third-Party Libraries:**
  - Android Jetpack, Compose, Hilt, Room, Media3, DataStore, WorkManager, Paging: Apache-2.0.
  - Adhan (`com.batoulapps.adhan`): MIT License.
  - Coil: Apache-2.0.
  - Retrofit, OkHttp, Gson: Apache-2.0.
  - Lottie Compose: Apache-2.0.
  - NewPipeExtractor (`com.github.TeamNewPipe:NewPipeExtractor`): GPL-3.0-or-later.

---

## 2. Typography & Fonts
- **Amiri Font** (`res/font/amiri_*.ttf`): SIL Open Font License 1.1 (OFL).
- **Inter Font** (`res/font/inter_variable.ttf`): SIL Open Font License 1.1 (OFL).

---

## 3. Text & Religious Data

### 3.1 Holy Quran & Translations
- **Arabic Text:** Tanzil.net / Quran.com API v4.
- **Vietnamese Translation (`assets/quran_vi.json`):** Meaning of the Holy Quran translated into Vietnamese.
- **Tafsir:** Quran.com API v4 (Tafsir Ibn Kathir / Saadi / Khiar).

### 3.2 Prayer Calculations & Hijri Calendar
- **Prayer Calculation Algorithm:** Adhan Library (Batoul Apps - MIT License).
- **Default Calculation Method:** `MUSLIMVN_DEFAULT` (Fajr Angle 18°, Isha Angle 18°).
- **Hijri Calendar:** Aladhan API (`api.aladhan.com`) integrated with offline local calculations.

### 3.3 Azkar & 99 Names of Allah
- **Azkar (`assets/azkar_vi.json`):** Collection of supplications from *Hisnul Muslim* (Fortress of the Muslim).
- **99 Names of Allah (`assets/NameAllah.json`):** Beautiful names and Vietnamese meanings of Allah.

### 3.4 Hadith & Scholars Content
- **Daily Hadith Quotes:** HadeethEnc API (`hadeethenc.com`).
- **World Scholars Podcasts:** RSS Feed Muslim Central (`rss.muslimcentral.com`).
- **Mach Zen Documents:** IslamHouse API v3 (`api3.islamhouse.com`).
- **Vietnamese Scholars YouTube Feeds (Mach Zen & Gosaly Ahmad):** Public content accessed with creator and IslamHouse public attribution.

---

## 4. Audio Assets
- **Adhan Audio Recitations (`assets/audio/adhan/*.mp3`):** Public domain / AlAdhan Project recitations:
  - `Mishary-Alafasi.mp3`
  - `hamad_daghriry.mp3`
  - `Ahmed-El-Kourdi.mp3`
  - `Nasser-Alqatami.mp3`
  - `Mansoor-Az-Zahrani.mp3`
  - `Rabeh-Ibn-Darah-Al-Jazairi.mp3`
  - `Fajaz_Azan.mp3`
- **Notification Tones (`res/raw/muslimvn_notification.mp3`):** Custom application alert tone.

---

## 5. Disclaimer
> MuslimVN is an independent open-source application developed by the MuslimVN community. The application is not officially affiliated with, sponsored, or endorsed by scholars, Qaris, Muslim Central, or IslamHouse organizations. Prayer times and Zakat tools are provided for informational and devotional purposes; users should verify calculations with local Islamic authorities and community centers.
