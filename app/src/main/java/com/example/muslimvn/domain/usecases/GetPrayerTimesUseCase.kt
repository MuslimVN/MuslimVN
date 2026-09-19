package com.example.muslimvn.domain.usecases

import com.example.muslimvn.domain.models.LocationSource
import com.example.muslimvn.domain.models.PrayerTimes
import com.example.muslimvn.domain.models.SavedLocation
import com.example.muslimvn.domain.repository.LocationRepository
import com.example.muslimvn.domain.repository.PrayerRepository
import com.example.muslimvn.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class GetPrayerTimesUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val prayerRepository: PrayerRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(
        date: Date = Date(),
        lat: Double? = null,
        lng: Double? = null
    ): PrayerTimes {
        val savedLocation = settingsRepository.getSavedLocation().first()
        val actualLat: Double
        val actualLng: Double

        if (lat != null && lng != null) {
            actualLat = lat
            actualLng = lng
        } else if (savedLocation.source == LocationSource.GPS) {
            val location = locationRepository.getCurrentLocation()
            if (location != null) {
                actualLat = location.latitude
                actualLng = location.longitude
                settingsRepository.saveLocation(
                    SavedLocation(
                        latitude = actualLat,
                        longitude = actualLng,
                        cityName = savedLocation.cityName,
                        source = LocationSource.GPS,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } else {
                actualLat = savedLocation.latitude
                actualLng = savedLocation.longitude
            }
        } else {
            actualLat = savedLocation.latitude
            actualLng = savedLocation.longitude
        }

        val methodStr = settingsRepository.getCalculationMethod().first()
        val asrMethod = settingsRepository.getAsrMethod().first()
        val adjustments = settingsRepository.getPrayerAdjustments().first()

        return prayerRepository.getPrayerTimes(
            latitude = actualLat,
            longitude = actualLng,
            date = date,
            calculationMethod = methodStr,
            asrMethod = asrMethod,
            adjustments = adjustments
        )
    }
}
