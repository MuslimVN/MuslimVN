package com.example.muslimvn.core.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.muslimvn.domain.models.PrayerName
import com.example.muslimvn.domain.models.PrayerReminder
import com.example.muslimvn.domain.models.PrayerTimes
import com.example.muslimvn.domain.models.ReminderMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdhanScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private data class PrayerAlarm(
        val name: String,
        val timeInMillis: Long
    )

    /**
     * Tìm và lập MỘT báo thức cho mốc cầu nguyện sắp tới gần nhất.
     * Sử dụng giờ chính xác của hôm nay và ngày mai.
     */
    fun scheduleNextWithSettings(
        todayTimes: PrayerTimes,
        tomorrowTimes: PrayerTimes? = null,
        reminders: Map<String, PrayerReminder>
    ) {
        val now = System.currentTimeMillis()

        val todayMap = mapOf(
            PrayerName.FAJR to todayTimes.fajr.time,
            PrayerName.DHUHR to todayTimes.dhuhr.time,
            PrayerName.ASR to todayTimes.asr.time,
            PrayerName.MAGHRIB to todayTimes.maghrib.time,
            PrayerName.ISHA to todayTimes.isha.time
        )

        val tomorrowMap = if (tomorrowTimes != null) {
            mapOf(
                PrayerName.FAJR to tomorrowTimes.fajr.time,
                PrayerName.DHUHR to tomorrowTimes.dhuhr.time,
                PrayerName.ASR to tomorrowTimes.asr.time,
                PrayerName.MAGHRIB to tomorrowTimes.maghrib.time,
                PrayerName.ISHA to tomorrowTimes.isha.time
            )
        } else null

        val candidates = todayMap.mapNotNull { (name, todayTime) ->
            val reminder = reminders[name]
            if (reminder?.mode == ReminderMode.SILENT) return@mapNotNull null

            if (todayTime > now) {
                PrayerAlarm(name, todayTime)
            } else {
                val tomorrowTime = tomorrowMap?.get(name) ?: (todayTime + ONE_DAY_MILLIS)
                if (tomorrowTime > now) {
                    PrayerAlarm(name, tomorrowTime)
                } else {
                    null
                }
            }
        }

        val next = candidates.minByOrNull { it.timeInMillis }
        if (next != null) {
            scheduleAdhan(next.name, Date(next.timeInMillis))
        } else {
            cancelAdhan()
        }
    }

    /**
     * Lập báo thức cho một mốc thời gian cụ thể.
     */
    fun scheduleAdhan(prayerName: String, prayerTime: Date) {
        val now = System.currentTimeMillis()
        val triggerAtMillis = prayerTime.time

        if (triggerAtMillis <= now) return

        val intent = Intent(context, AdhanReceiver::class.java).apply {
            action = ACTION_ADHAN_ALARM
            putExtra(EXTRA_PRAYER_NAME, prayerName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ADHAN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun cancelAdhan() {
        val intent = Intent(context, AdhanReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ADHAN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val ACTION_ADHAN_ALARM = "ACTION_ADHAN_ALARM"
        const val EXTRA_PRAYER_NAME = "PRAYER_NAME"

        private const val REQUEST_CODE_ADHAN = 2001
        private const val ONE_DAY_MILLIS = 24L * 60 * 60 * 1000
    }
}
