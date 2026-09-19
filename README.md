# MuslimVN — The Islamic Companion for Vietnam

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Android SDK](https://img.shields.io/badge/SDK-26%2B-brightgreen.svg)](https://developer.android.com/about/dashboards)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-purple.svg)](https://kotlinlang.org/)

**MuslimVN** is a professional, privacy-focused, and open-source mobile application tailored for the Muslim community in Vietnam. It provides highly accurate offline prayer times, Qibla direction, Holy Quran with Vietnamese translation, Zakat tools, and rich Islamic educational content.

---

## ✨ Key Features

- 🕌 **Accurate Offline Prayer Times:** Precise calculations using the Adhan astronomical algorithm (`MUSLIMVN_DEFAULT`). Works 100% offline with flexible Adhan audio reminders.
- 🧭 **Precise Qibla Finder:** Real-time compass navigation using device sensors and location data to find the direction of the Kaaba (Makkah).
- 📖 **The Holy Quran:** Read all 114 Surahs with high-quality Arabic text and clean Vietnamese translations. Supports audio recitations and Mushaf page views.
- 📅 **Hijri Calendar:** Track the Islamic lunar calendar with manual date adjustment support and major religious event reminders.
- 🧮 **Zakat Calculator:** Comprehensive Fiqh-compliant tools for calculating Zakat on Cash, Gold, Silver, Stocks, Real Estate, and Agriculture.
- 🤲 **Daily Azkar & 99 Names of Allah:** Morning/Evening supplications from *Hisnul Muslim* and the beautiful names of Allah with Vietnamese meanings.
- 🎙️ **Scholars & Podcasts:** Stream world-class podcasts (Muslim Central) and localized Vietnamese lectures from trusted scholars.

---

## 🛠 Tech Stack & Architecture

Built with modern Android development practices to ensure performance, reliability, and maintainability.

- **UI:** Jetpack Compose with Material 3 Adaptive Design.
- **Navigation:** Jetpack Navigation 3 (Type-safe routing).
- **Architecture:** Clean Architecture + MVVM + Repository Pattern.
- **Dependency Injection:** Hilt (Dagger).
- **Local Storage:** Room Database (with V1 Baseline Schemas) and DataStore Preferences.
- **Background Tasks:** WorkManager & BroadcastReceivers for reliable Adhan scheduling.
- **Media Playback:** Media3 ExoPlayer & Session integration.
- **Privacy Engine:** Android Framework `LocationManager` (No Google Play Services dependency - perfect for F-Droid).

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer.
- JDK 17 or JDK 21.
- Android SDK 36 (targetSdk 36, minSdk 26).

### Build Instructions
```bash
# Clone the repository
git clone https://github.com/MuslimVN/MuslimVN.git
cd MuslimVN

# Build Debug APK
./gradlew assembleDebug

# Run Unit Tests (Golden Tests included)
./gradlew testDebugUnitTest

# Build Release Bundle (AAB)
./gradlew bundleRelease
```

### Keystore Configuration
To sign your release builds, create a `keystore.properties` file in the root directory based on the provided template:
```properties
storeFile=your_keystore.jks
storePassword=your_password
keyAlias=your_alias
keyPassword=your_password
```
If this file is missing, the project will generate an **unsigned** release build (ideal for F-Droid automated signing).

---

## 🔒 Privacy & Security

We take user privacy seriously:
- **Zero Tracking:** No analytics, no crashes reporting to 3rd party servers, and no advertisements.
- **Local Processing:** Your location coordinates never leave your device.
- **Open Source:** Audit the code yourself to verify our privacy claims.
- See our full [Privacy Policy](PRIVACY.md).

---

## 📜 License & Content Attribution

- **Mã nguồn (Source Code):** Licensed under [GNU General Public License v3.0](LICENSE) (GPL-3.0-or-later).
- **Content & Assets:** Detailed sources for Quran translations, fonts, and audio can be found in [CONTENT_SOURCES.md](docs/CONTENT_SOURCES.md).

---

## 🤝 Contributing & Feedback

Contributions are welcome! Please feel free to submit a Pull Request or open an issue for bugs and feature requests on our [GitHub Issues](https://github.com/MuslimVN/MuslimVN/issues).

---
*Developed with ❤️ by the MuslimVN Community.*
