package com.example.muslimvn.domain.models

enum class LocationSource {
    GPS,
    MANUAL,
    DEFAULT
}

data class SavedLocation(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val source: LocationSource,
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        val DEFAULT = SavedLocation(
            latitude = 10.7769,
            longitude = 106.7009,
            cityName = "TP. Hồ Chí Minh",
            source = LocationSource.DEFAULT
        )
    }
}
