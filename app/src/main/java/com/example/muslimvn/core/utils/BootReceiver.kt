package com.example.muslimvn.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.muslimvn.domain.repository.SettingsRepository
import com.example.muslimvn.domain.usecases.GetPrayerTimesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getPrayerTimesUseCase: GetPrayerTimesUseCase

    @Inject
    lateinit var adhanScheduler: AdhanScheduler

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED
        ) {
            val pendingResult = goAsync()
            scope.launch {
                try {
                    val todayTimes = getPrayerTimesUseCase(date = Date())
                    val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
                    val tomorrowTimes = getPrayerTimesUseCase(date = tomorrowCal.time)
                    val reminders = settingsRepository.getPrayerReminders().first()
                    adhanScheduler.scheduleNextWithSettings(todayTimes, tomorrowTimes, reminders)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
