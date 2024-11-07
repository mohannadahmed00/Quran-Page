package com.giraffe.quranpage.common.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.app.NotificationCompat
import com.giraffe.quranpage.R
import com.giraffe.quranpage.common.utils.Constants.Actions.CANCEL_DOWNLOAD
import com.giraffe.quranpage.common.utils.Constants.Actions.START_DOWNLOAD
import com.giraffe.quranpage.common.utils.Constants.Keys.NOTIFICATION_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.RECITER_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.RECITER_NAME
import com.giraffe.quranpage.common.utils.Constants.Keys.SURAH_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.SURAH_NAME
import com.giraffe.quranpage.common.utils.Constants.Keys.URL
import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.domain.onError
import com.giraffe.quranpage.common.utils.domain.onSuccess
import com.giraffe.quranpage.data.datasource.remote.api.AudioApiService
import com.giraffe.quranpage.data.datasource.remote.downloader.AudioDownloader
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.entities.VerseTimingEntity
import com.giraffe.quranpage.domain.usecases.AddSurahAudioDataToReciterUseCase
import com.giraffe.quranpage.domain.usecases.GetVersesTimingUseCase
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val binder: LocalBinder by lazy { LocalBinder() }
    private val _queueState = MutableStateFlow(mutableStateMapOf<String, DownloadedProcessData>())
    val queueState = _queueState.asStateFlow()
    val downloadedFiles = mutableStateMapOf<String, DownloadedProcessData>()
    private val _networkError = MutableSharedFlow<Pair<String, NetworkError>?>()
    val networkError = _networkError.asSharedFlow()

    @Inject
    lateinit var getVersesTimingUseCase: GetVersesTimingUseCase

    @Inject
    lateinit var addSurahAudioDataToReciterUseCase: AddSurahAudioDataToReciterUseCase


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationId = intent?.getIntExtra(NOTIFICATION_ID, -1) ?: -1
        val reciterId = intent?.getIntExtra(RECITER_ID, -1) ?: -1
        val reciterName = intent?.getStringExtra(RECITER_NAME) ?: ""
        val surahIndex = intent?.getIntExtra(SURAH_ID, -1) ?: -1
        val surahName = intent?.getStringExtra(SURAH_NAME) ?: ""
        val url = intent?.getStringExtra(URL) ?: ""
        val action = intent?.action
        if (action == START_DOWNLOAD && notificationId != -1) {
            val downloadedProcessData = DownloadedProcessData(
                id = notificationId,
                url = url,
                progress = MutableStateFlow(0),
                reciterId = reciterId,
                surahIndex = surahIndex,
                notificationBuilder = createNotificationBuilder(this, reciterName, surahName),
            )
            startDownload(downloadedProcessData)
        } else if (action == CANCEL_DOWNLOAD && url.isNotEmpty()) {
            cancelDownload(url)
        }
        return START_NOT_STICKY
    }

    private fun startDownload(downloadedProcessData: DownloadedProcessData) {
        with(downloadedProcessData) {
            lastNotificationId = id
            startForeground(lastNotificationId, notificationBuilder?.build())
            val job = CoroutineScope(Dispatchers.IO).launch {
                getVersesTimingUseCase(
                    surahIndex = surahIndex,
                    reciterId = reciterId,
                ).onSuccess { data ->
                    try {
                        getAudioDownloader().downloadFile(url) { progress, filePath, inputStream ->
                            updateDownloadProcess(
                                this@with.copy(
                                    versesTiming = data,
                                    filePath = filePath,
                                    inputStream = inputStream,
                                    notificationBuilder = notificationBuilder?.apply {
                                        setProgress(100, progress, false)
                                        setContentText("$progress %")
                                        notificationManager.notify(id, build())
                                    }
                                ).also {
                                    it.progress.value = progress
                                }
                            )
                            if (progress == 100) completeDownload(url)
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is UnresolvedAddressException, is SocketException -> {
                                _networkError.emit(Pair(url, NetworkError.NO_INTERNET))
                                cancelDownload(url)
                            }

                            else -> {}
                        }
                    } finally {
                        withContext(Dispatchers.IO) {
                            _queueState.value[url]?.inputStream?.close()
                        }
                    }

                }.onError { error ->
                    _networkError.emit(Pair(url, error))
                    cancelDownload(url)
                }

            }
            updateDownloadProcess(this.copy(downloadJob = job))
        }
    }

    private fun updateDownloadProcess(downloadedProcessData: DownloadedProcessData?) {
        downloadedProcessData?.let {
            _queueState.update { queue ->
                queue.apply {
                    set(key = downloadedProcessData.url, value = downloadedProcessData)
                }
            }
        }
    }

    private fun completeDownload(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _queueState.value[url]?.let { downloadedProcessData ->
                val surahAudioDataEntity = SurahAudioDataEntity(
                    surahIndex = downloadedProcessData.surahIndex,
                    audioPath = downloadedProcessData.filePath,
                    verseTiming = downloadedProcessData.versesTiming,
                )
                val reciter = addSurahAudioDataToReciterUseCase(
                    reciterId = downloadedProcessData.reciterId,
                    surahAudioDataEntity = surahAudioDataEntity,
                )
                downloadedFiles[url] = downloadedProcessData.copy(
                    reciter = reciter,
                    surahAudioDataModel = surahAudioDataEntity
                )
                promoteViceForeground(downloadedProcessData)
            }
        }
    }

    private fun cancelDownload(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _queueState.value[url]?.let { downloadedProcessData ->
                stopDownload(downloadedProcessData.inputStream, downloadedProcessData.downloadJob)
                disableOngoing(downloadedProcessData)
                promoteViceForeground(downloadedProcessData)
            }
        }
    }

    private fun promoteViceForeground(downloadedProcessData: DownloadedProcessData) {
        removeFromQueue(downloadedProcessData.url)
        if (_queueState.value.isEmpty()) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        } else {
            if (downloadedProcessData.id == lastNotificationId) {
                _queueState.value.maxBy { item -> item.value.startDownloadTime }
                    .component2().let { viceForeground ->
                        lastNotificationId = viceForeground.id
                        startForeground(
                            viceForeground.id,
                            viceForeground.notificationBuilder?.build()
                        )
                    }

            }
        }
        notificationManager.cancel(downloadedProcessData.id)
    }

    private fun disableOngoing(downloadedProcessData: DownloadedProcessData?) {
        downloadedProcessData?.notificationBuilder?.setOngoing(false)
        notificationManager.notify(
            downloadedProcessData?.id ?: 0,
            downloadedProcessData?.notificationBuilder?.build()
        )
    }

    private suspend fun stopDownload(inputStream: InputStream?, downloadJob: Job?) {
        withContext(Dispatchers.IO) {
            inputStream?.close()
            downloadJob?.cancel()
        }
    }

    private fun removeFromQueue(url: String) {
        _queueState.update { it.apply { remove(url) } }
    }


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

    private fun createNotificationBuilder(
        context: Context,
        reciterName: String,
        surahName: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ayah_end)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setContentTitle("$surahName : $reciterName")
            .setProgress(100, 0, false)
    }

    private fun getAudioDownloader(): AudioDownloader {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return AudioDownloader(this, retrofit.create(AudioApiService::class.java))
    }

    override fun onBind(intent: Intent?) = binder
    inner class LocalBinder : Binder() {
        fun getService(): DownloadService = this@DownloadService
    }

    data class DownloadedProcessData(
        val id: Int = 0,
        val url: String = "",
        val progress: MutableStateFlow<Int> = MutableStateFlow(0),
        val reciterId: Int = 0,
        val surahIndex: Int = 0,
        val filePath: String = "",
        val notificationBuilder: NotificationCompat.Builder? = null,
        val downloadJob: Job? = null,
        val inputStream: InputStream? = null,
        val reciter: ReciterEntity? = null,
        val surahAudioDataModel: SurahAudioDataEntity? = null,
        val versesTiming: List<VerseTimingEntity> = emptyList(),
        val startDownloadTime: Long = Date().time,
    )

    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID"
        const val CHANNEL_NAME: String = "CHANNEL_NAME"
        private var lastNotificationId = -1
    }
}