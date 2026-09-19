package com.example.muslimvn.core.utils

import com.example.muslimvn.domain.models.PrayerName
import com.example.muslimvn.domain.models.PrayerReminder
import com.example.muslimvn.domain.models.PrayerTimes
import com.example.muslimvn.domain.models.ReminderMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class AdhanSchedulerTest {

    private val vnTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")

    private fun createDate(hour: Int, minute: Int, isTomorrow: Boolean = false): Date {
        val cal = Calendar.getInstance(vnTimeZone)
        if (isTomorrow) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    private fun createTestPrayerTimes(isTomorrow: Boolean = false): PrayerTimes {
        val fajr = createDate(4, 30, isTomorrow)
        val sunrise = createDate(5, 45, isTomorrow)
        val dhuhr = createDate(12, 0, isTomorrow)
        val asr = createDate(15, 15, isTomorrow)
        val maghrib = createDate(18, 0, isTomorrow)
        val isha = createDate(19, 15, isTomorrow)

        return PrayerTimes(
            fajr = fajr,
            sunrise = sunrise,
            dhuhr = dhuhr,
            asr = asr,
            maghrib = maghrib,
            isha = isha,
            nextPrayerName = PrayerName.FAJR,
            nextPrayerTime = fajr,
            nextPrayerCountdown = "00:00"
        )
    }

    @Test
    fun testPrayerRemindersMap() {
        val reminders = mapOf(
            PrayerName.FAJR to PrayerReminder(PrayerName.FAJR, ReminderMode.NOTIFICATION),
            PrayerName.DHUHR to PrayerReminder(PrayerName.DHUHR, ReminderMode.ADHAN),
            PrayerName.ASR to PrayerReminder(PrayerName.ASR, ReminderMode.SILENT)
        )

        assertEquals(ReminderMode.NOTIFICATION, reminders[PrayerName.FAJR]?.mode)
        assertEquals(ReminderMode.ADHAN, reminders[PrayerName.DHUHR]?.mode)
        assertEquals(ReminderMode.SILENT, reminders[PrayerName.ASR]?.mode)
    }

    @Test
    fun testTodayAndTomorrowPrayerTimesDates() {
        val todayTimes = createTestPrayerTimes(isTomorrow = false)
        val tomorrowTimes = createTestPrayerTimes(isTomorrow = true)

        assertNotNull(todayTimes.fajr)
        assertNotNull(tomorrowTimes.fajr)
        assertTrue(tomorrowTimes.fajr.after(todayTimes.fajr))
    }
}
