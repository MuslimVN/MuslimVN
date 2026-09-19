package com.example.muslimvn.core.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.muslimvn.R
import com.example.muslimvn.core.di.NotificationModule
import com.example.muslimvn.presentation.screens.StopAdhanActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdhanService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_STOP_ADHAN) {
            stopSelf()
            return START_NOT_STICKY
        }

        val prayerName = intent?.getStringExtra(EXTRA_PRAYER_NAME) ?: "Prayer"
        val adhanFile = intent?.getStringExtra(EXTRA_ADHAN_FILE)

        showForegroundNotification(prayerName)
        
        if (adhanFile != null) {
            playAdhan(adhanFile)
        }

        return START_NOT_STICKY
    }

    private fun showForegroundNotification(prayerName: String) {
        val stopIntent = Intent(this, AdhanService::class.java).apply {
            action = ACTION_STOP_ADHAN
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NotificationModule.ADHAN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_adhan)
            .setContentTitle(getString(R.string.adhan_notification_title, prayerName))
            .setContentText(getString(R.string.adhan_service_content))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(android.R.drawable.ic_media_pause, getString(R.string.adhan_stop), stopPendingIntent)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun playAdhan(fileName: String) {
        try {
            val afd = assets.openFd("audio/adhan/$fileName")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                prepare()
                start()
                setOnCompletionListener { stopSelf() }
            }
            afd.close()
        } catch (e: Exception) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        sendBroadcast(Intent(ACTION_ADHAN_STOPPED))
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val EXTRA_PRAYER_NAME = "EXTRA_PRAYER_NAME"
        const val EXTRA_ADHAN_FILE = "EXTRA_ADHAN_FILE"
        const val ACTION_STOP_ADHAN = "ACTION_STOP_ADHAN"
        const val ACTION_ADHAN_STOPPED = "io.github.muslimvn.app.ACTION_ADHAN_STOPPED"
        private const val NOTIFICATION_ID = 3001
    }
}
