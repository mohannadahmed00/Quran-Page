package com.giraffe.quranpage.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.giraffe.quranpage.R
import com.giraffe.quranpage.remote.downloader.AudioDownloader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {
    @Inject
    lateinit var audioDownloader: AudioDownloader
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val binder: LocalBinder by lazy {
        LocalBinder()
    }
    private val builders = mutableMapOf<String, NotificationCompat.Builder>()
    private val _queueState = MutableStateFlow(mapOf<String, DownloadedAudio>())
    val queueState = _queueState.asStateFlow()
    fun removeFromQueue(key: String) {
        _queueState.update {
            val tempMap = mutableMapOf<String, DownloadedAudio>()
            tempMap.putAll(it)
            tempMap.remove(key)
            tempMap.toMap()
        }
        builders.remove(key)
    }

    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()
    }

    private fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ayah_end)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(100, 0, false)
    }

    fun startDownload(url: String, reciterId: Int, surahIndex: Int) {
        if (!queueState.value.containsKey(url)) {
            builders[url] = createNotificationBuilder(this)
            startForeground(url.hashCode(), builders[url]?.build())
            audioDownloader.downloadFile(url) { progress, path ->
                _queueState.update {
                    val tempMap = mutableMapOf<String, DownloadedAudio>()
                    tempMap.putAll(it)
                    tempMap[url] = DownloadedAudio(progress, reciterId, surahIndex, path)
                    builders[url]?.apply {
                        setProgress(100, progress, false)
                        setContentTitle("$progress %")
                        setContentText("file id: $url")
                        notificationManager.notify(url.hashCode(), build())
                    }
                    tempMap.toMap()
                }
                if (progress == 100) {
                    notificationManager.cancel(url.hashCode())
                }
            }
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?) = binder
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
    inner class LocalBinder : Binder() {
        fun getService(): DownloadService = this@DownloadService
    }

    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID"
        const val CHANNEL_NAME: String = "CHANNEL_NAME"
    }

    data class DownloadedAudio(
        val progress: Int = 0,
        val reciterId: Int = 0,
        val surahIndex: Int = 0,
        val filePath: String = ""
    )
}
