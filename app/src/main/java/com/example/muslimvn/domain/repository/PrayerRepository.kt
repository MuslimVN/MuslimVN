package com.example.muslimvn.domain.repository

import com.example.muslimvn.domain.models.AsrMethod
import com.example.muslimvn.domain.models.PrayerAdjustments
import com.example.muslimvn.domain.models.PrayerTimes
import java.util.Date

interface PrayerRepository {
    fun getPrayerTimes(
        latitude: Double,
        longitude: Double,
        date: Date,
        calculationMethod: String? = null,
        asrMethod: AsrMethod? = null,
        adjustments: PrayerAdjustments? = null
    ): PrayerTimes
}
