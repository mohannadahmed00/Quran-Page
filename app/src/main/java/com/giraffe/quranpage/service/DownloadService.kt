package com.giraffe.quranpage.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.app.NotificationCompat
import com.giraffe.quranpage.R
import com.giraffe.quranpage.remote.api.AudioService
import com.giraffe.quranpage.remote.downloader.AudioDownloader
import com.giraffe.quranpage.utils.Constants.Actions.CANCEL_DOWNLOAD
import com.giraffe.quranpage.utils.Constants.Actions.START_DOWNLOAD
import com.giraffe.quranpage.utils.Constants.Keys.NOTIFICATION_ID
import com.giraffe.quranpage.utils.Constants.Keys.RECITER_ID
import com.giraffe.quranpage.utils.Constants.Keys.SURAH_ID
import com.giraffe.quranpage.utils.Constants.Keys.URL
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.Date

@AndroidEntryPoint
class DownloadService : Service() {

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val binder: LocalBinder by lazy { LocalBinder() }
    private val _queueState = MutableStateFlow(mutableStateMapOf<String, DownloadedAudio>())
    val queueState = _queueState.asStateFlow()
    val downloadedFiles = mutableStateMapOf<String, DownloadedAudio>()


    override fun onCreate() {
        super.onCreate()
        Log.d("DownloadService", "onCreate: ")
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand: ${intent?.action} ")
        val notificationId = intent?.getIntExtra(NOTIFICATION_ID, -1) ?: -1
        val surahId = intent?.getIntExtra(SURAH_ID, -1) ?: -1
        val reciterId = intent?.getIntExtra(RECITER_ID, -1) ?: -1
        val url = intent?.getStringExtra(URL) ?: ""
        val action = intent?.action
        if (action == START_DOWNLOAD && notificationId != -1) {
            startDownload(notificationId, url, reciterId, surahId)
        } else if (action == CANCEL_DOWNLOAD && url.isNotEmpty()) {
            cancelDownload(url)
        }
        return START_NOT_STICKY
    }

    private fun startDownload(notificationId: Int, url: String, reciterId: Int, surahId: Int) {
        if (!queueState.value.containsKey(url)) {
            Log.d("DownloadService", "startDownload($notificationId, $url, $reciterId, $surahId)")
            val downloadedFile = DownloadedAudio(
                id = notificationId,
                url = url,
                progress = MutableStateFlow(0),
                reciterId = reciterId,
                surahId = surahId,
                notificationBuilder = createNotificationBuilder(this)
            )
            lastNotificationId = downloadedFile.id
            startForeground(notificationId, downloadedFile.notificationBuilder?.build())
            getAudioDownloader().downloadFile(url) { progress, path, job, inputStream ->
                //Log.d("DownloadService", "startDownload: $progress = $path")
                updateQueue(downloadedFile, progress, path, job, inputStream)
                if (progress == 100) {
                    completeDownload(url)
                }
            }
        }
    }


    private fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ayah_end)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setProgress(100, 0, false)
    }

    private fun getAudioDownloader(): AudioDownloader {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return AudioDownloader(this, retrofit.create(AudioService::class.java))
    }


    private fun updateQueue(
        downloadedFile: DownloadedAudio,
        progress: Int,
        path: String,
        job: Job?,
        inputStream: InputStream?
    ) {
        _queueState.update {
            if (it.containsKey(downloadedFile.url)) {
                it[downloadedFile.url]?.progress?.update { progress }
                val newBuilder = downloadedFile.notificationBuilder?.apply {
                    setProgress(100, progress, false)
                    setContentTitle("$progress %")
                    setContentText("file id: ${downloadedFile.url}")
                    notificationManager.notify(downloadedFile.id, build())
                }
                it[downloadedFile.url]?.let { item ->
                    _queueState.value[downloadedFile.url] =
                        item.copy(notificationBuilder = newBuilder)
                }
            } else {
                it[downloadedFile.url] = downloadedFile.copy(
                    filePath = path,
                    downloadJob = job,
                    inputStream = inputStream
                )
            }
            notificationManager.notify(
                downloadedFile.id,
                it[downloadedFile.url]?.notificationBuilder?.build()
            )
            it
        }
    }


    private fun cancelDownload(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("DownloadService", "cancelDownload($url)")
            val canceledAudio = _queueState.value[url]
            stopDownload(canceledAudio?.inputStream, canceledAudio?.downloadJob)
            removeFromQueue(url)
            disableOngoing(canceledAudio)

            if (_queueState.value.isNotEmpty()) {
                if (canceledAudio?.id == lastNotificationId) {
                    val viceNotification =
                        _queueState.value.maxBy { item -> item.value.startDownloadTime }
                            .component2()
                    lastNotificationId = viceNotification.id
                    Log.d(
                        "DownloadService",
                        "cancelDownload : the vice = ${viceNotification.url}"
                    )
                    startForeground(
                        viceNotification.id,
                        viceNotification.notificationBuilder?.build()
                    )
                }
            } else {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            canceledAudio?.id?.let { notificationManager.cancel(it) }

        }
    }

    private fun disableOngoing(canceledAudio: DownloadedAudio?) {
        canceledAudio?.notificationBuilder?.setOngoing(false)
        notificationManager.notify(
            canceledAudio?.id ?: 0,
            canceledAudio?.notificationBuilder?.build()
        )
    }

    private fun stopDownload(inputStream: InputStream?, downloadJob: Job?) {
        Log.d("DownloadService", "stopDownload : inputStream , downloadJob")
        CoroutineScope(Dispatchers.IO).launch {
            inputStream?.close()
            downloadJob?.cancel()
        }
    }

    private fun removeFromQueue(url: String) {
        _queueState.update {
            it.remove(url)
            it
        }
    }

    private fun completeDownload(url: String) {
        _queueState.value[url]?.let {
            Log.d("DownloadService", "completeDownload($url)")
            downloadedFiles[url] = it
            if (it.id == lastNotificationId) {
                Log.d(
                    "DownloadService",
                    "completeDownload: lastNotificationId = $lastNotificationId"
                )
                stopForeground(STOP_FOREGROUND_REMOVE)
                if (_queueState.value.size > 1) {
                    Log.d(
                        "DownloadService",
                        "completeDownload: _queueState.value.size = ${_queueState.value.size}"
                    )
                    val mainTwo =
                        _queueState.value.entries.sortedByDescending { item -> item.value.startDownloadTime }[1].component2()
                    lastNotificationId = mainTwo.id
                    startForeground(mainTwo.id, mainTwo.notificationBuilder?.build())
                }
            }
            notificationManager.cancel(it.id)
        }
        removeFromQueue(url)
    }


    override fun onBind(intent: Intent?): Binder {
        Log.d("DownloadService", "onBind: ")
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): DownloadService = this@DownloadService
    }

    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID"
        const val CHANNEL_NAME: String = "CHANNEL_NAME"
        private var lastNotificationId = -1
    }

    data class DownloadedAudio(
        val id: Int = 0,
        val url: String = "",
        val progress: MutableStateFlow<Int> = MutableStateFlow(0),
        val reciterId: Int = 0,
        val surahId: Int = 0,
        val filePath: String = "",
        val notificationBuilder: NotificationCompat.Builder? = null,
        val downloadJob: Job? = null,
        val inputStream: InputStream? = null,
        val startDownloadTime: Long = Date().time,
    )


    override fun onDestroy() {
        super.onDestroy()
        Log.d("DownloadService", "onDestroy: ")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("DownloadService", "onUnbind: ")
        return super.onUnbind(intent)
    }

}
