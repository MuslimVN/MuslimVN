package com.example.muslimvn.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import com.example.muslimvn.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationManager: LocationManager
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.IO) {
        try {
            // 1. Thử lấy vị trí gần nhất đã lưu từ các provider
            val providers = listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
            )
            var bestLocation: Location? = null
            for (provider in providers) {
                try {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null && (bestLocation == null || loc.time > bestLocation.time)) {
                        bestLocation = loc
                    }
                } catch (_: Exception) { }
            }

            // Nếu vị trí lưu gần đây đủ mới (dưới 15 phút), trả về ngay
            if (bestLocation != null && (System.currentTimeMillis() - bestLocation.time) < 15 * 60 * 1000) {
                return@withContext bestLocation
            }

            // 2. Yêu cầu vị trí trực tiếp từ hệ thống
            val freshLocation = getFreshLocation()
            freshLocation ?: bestLocation
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): Location? = suspendCancellableCoroutine { continuation ->
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val provider = when {
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                    else -> null
                }
                if (provider != null) {
                    val cancellationSignal = CancellationSignal()
                    continuation.invokeOnCancellation { cancellationSignal.cancel() }
                    locationManager.getCurrentLocation(
                        provider,
                        cancellationSignal,
                        context.mainExecutor
                    ) { location ->
                        if (continuation.isActive) continuation.resume(location)
                    }
                    return@suspendCancellableCoroutine
                }
            }

            // Fallback cho API < 30 hoặc khi getCurrentLocation không khả dụng
            val provider = when {
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> null
            }

            if (provider == null) {
                if (continuation.isActive) continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    if (continuation.isActive) continuation.resume(location)
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            continuation.invokeOnCancellation {
                locationManager.removeUpdates(listener)
            }

            @Suppress("DEPRECATION")
            locationManager.requestSingleUpdate(provider, listener, context.mainLooper)
        } catch (e: Exception) {
            if (continuation.isActive) continuation.resume(null)
        }
    }

    override suspend fun getAddress(lat: Double, lng: Double): String? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.adminArea ?: address.locality
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val DEFAULT_LATITUDE = 10.7769 // TP. Hồ Chí Minh (theo D10)
        const val DEFAULT_LONGITUDE = 106.7009
    }
}
