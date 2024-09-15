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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val binder: LocalBinder by lazy { LocalBinder() }
    private val _queueState = MutableStateFlow(mapOf<String, DownloadedAudio>())
    val queueState = _queueState.asStateFlow()
    val downloadedFiles = mutableStateMapOf<String, DownloadedAudio>()


    override fun onCreate() {
        super.onCreate()
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


    private fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ayah_end)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setOngoing(true)
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

    private fun startDownload(notificationId: Int, url: String, reciterId: Int, surahId: Int,update:Boolean = false) {
        if (!queueState.value.containsKey(url) || update) {
            Log.d("messi", "startDownload: $url")
            val downloadedFile = if (update) _queueState.value[url]!! else DownloadedAudio(
                id = notificationId,
                url = url,
                progress = MutableStateFlow(0),
                reciterId = reciterId,
                surahId = surahId,
                notificationBuilder = createNotificationBuilder(this)
            )
            lastNotificationId = downloadedFile.id
            startForeground(notificationId,downloadedFile.notificationBuilder?.build())
            getAudioDownloader().downloadFile(url) { progress, path, job, inputStream ->
                updateQueue(downloadedFile,progress,path,job,inputStream)
                if (progress == 100) {
                    completeDownload(url)
                    Log.d(
                        "messi",
                        "complete Download (progress == 100): ${downloadedFiles[url]?.url}"
                    )
                }
            }
        }
    }

    private fun updateQueue(
        downloadedFile: DownloadedAudio,
        progress: Int,
        path: String,
        job: Job?,
        inputStream: InputStream?
    ){
        _queueState.update {
            val tempMap = mutableMapOf<String, DownloadedAudio>()
            tempMap.putAll(_queueState.value)
            if (tempMap.containsKey(downloadedFile.url)) {
                tempMap[downloadedFile.url]?.progress?.update { progress }
                val newBuilder = downloadedFile.notificationBuilder?.apply {
                    setProgress(100, progress, false)
                    setContentTitle("$progress %")
                    setContentText("file id: ${downloadedFile.url}")
                    notificationManager.notify(downloadedFile.id, build())
                }
                tempMap[downloadedFile.url]?.let {
                    tempMap[downloadedFile.url] = it.copy(notificationBuilder = newBuilder)
                }
            } else {
                tempMap[downloadedFile.url] = downloadedFile.copy(
                    filePath = path,
                    downloadJob = job,
                    inputStream = inputStream
                )
            }
            notificationManager.notify(downloadedFile.id, tempMap[downloadedFile.url]?.notificationBuilder?.build())
            /*downloadedFile.notificationBuilder?.apply {
                setProgress(100, progress, false)
                setContentTitle("$progress %")
                setContentText("file id: ${downloadedFile.url}")
                notificationManager.notify(downloadedFile.id, build())
            }*/
            tempMap
        }
    }

    private fun removeFromQueue(url: String) {
        _queueState.update {
            val tempMap = mutableMapOf<String, DownloadedAudio>()
            tempMap.putAll(it)
            tempMap.remove(url)
            tempMap.toMap()
        }
        Log.d("messi", "removeFromQueue($url) : size = ${_queueState.value.size}")
    }

    private fun stopDownload(inputStream: InputStream?,downloadJob: Job?) {
        Log.d("messi", "stopDownload : inputStream = $inputStream")
        Log.d("messi", "stopDownload : downloadJob = $downloadJob")
        CoroutineScope(Dispatchers.IO).launch {
            inputStream?.close()
            downloadJob?.cancel()
        }
    }

    private fun cancelDownload(url: String) {
        _queueState.value[url]?.let {
            stopDownload(it.inputStream,it.downloadJob)
            if (it.id == lastNotificationId) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                if (_queueState.value.size > 1) {
                    val mainOne = _queueState.value.maxBy {item-> item.value.startDownloadTime }.component2()
                    val mainTwo = _queueState.value.entries.sortedByDescending {item-> item.value.startDownloadTime }[1].component2()
                    lastNotificationId = mainTwo.id
                    startForeground(mainTwo.id,mainTwo.notificationBuilder?.build())
                }
            }

            notificationManager.cancel(it.id)
        }
        removeFromQueue(url)


        Log.d("messi", "cancelDownload($url) : size = ${_queueState.value.size}")
    }

    private fun completeDownload(url: String) {
        _queueState.value[url]?.let {
            downloadedFiles[url] = it
            //stopDownload(it.inputStream,it.downloadJob)
            if (it.id == lastNotificationId) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                if (_queueState.value.size > 1) {
                    val mainTwo = _queueState.value.entries.sortedByDescending {item-> item.value.startDownloadTime }[1].component2()
                    lastNotificationId = mainTwo.id
                    startForeground(mainTwo.id,mainTwo.notificationBuilder?.build())
                }
            }
            notificationManager.cancel(it.id)
        }
        removeFromQueue(url)
        Log.d("messi", "completeDownload($url) : size = ${_queueState.value.size}")
    }


    override fun onBind(intent: Intent?) = binder
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

}
