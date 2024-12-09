package com.giraffe.quranpage.common.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Build
import com.giraffe.quranpage.R
import com.giraffe.quranpage.common.receiver.MediaPlayerReceiver
import com.giraffe.quranpage.common.utils.AudioPlayerManager
import com.giraffe.quranpage.common.utils.Constants.Actions.NEXT
import com.giraffe.quranpage.common.utils.Constants.Actions.PAUSE
import com.giraffe.quranpage.common.utils.Constants.Actions.PLAY
import com.giraffe.quranpage.common.utils.Constants.Actions.PREVIOUS
import com.giraffe.quranpage.common.utils.Constants.Actions.RELEASE
import com.giraffe.quranpage.common.utils.Constants.Actions.STOP
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.usecases.GetAllVersesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : Service() {

    @Inject
    lateinit var getAllVersesUseCase: GetAllVersesUseCase
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val binder: LocalBinder by lazy { LocalBinder() }
    private lateinit var mediaSession: MediaSession
    private lateinit var mediaStyle: Notification.MediaStyle
    private val _audioPlayer = MutableStateFlow(AudioPlayerManager)
    val audioPlayer = _audioPlayer.asStateFlow()
    var trackAudio: (VerseEntity?) -> Unit = {}


    override fun onCreate() {
        super.onCreate()
        _audioPlayer.value.setAllVerses(getAllVersesUseCase())
        initNotificationChannel()
        initMediaSession()
        CoroutineScope(Dispatchers.IO).launch {
            audioPlayer.value.isCompleted.collect {
                if (it) startSession()
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

    private fun initMediaSession() {
        mediaSession = MediaSession(this, "MediaPlayerSessionService")
        mediaStyle = Notification.MediaStyle().setMediaSession(mediaSession.sessionToken)
    }

    private fun startSession() {
        mediaSession.setPlaybackState(
            PlaybackState.Builder()
                .setState(
                    if (audioPlayer.value.isPlaying.value) PlaybackState.STATE_PLAYING else PlaybackState.STATE_PAUSED,
                    audioPlayer.value.getCurrentDuration(),
                    1f
                )
                .setActions(PlaybackState.ACTION_PLAY_PAUSE)
                .build()
        )
        mediaSession.setMetadata(
            MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, audioPlayer.value.surahName.value)
                .putString(MediaMetadata.METADATA_KEY_ALBUM, "Quran")
                .putString(MediaMetadata.METADATA_KEY_ARTIST, audioPlayer.value.reciter.value?.name)
                .putLong(MediaMetadata.METADATA_KEY_DURATION, audioPlayer.value.getDuration())
                .build()
        )
        startForeground(1, createNotificationMediaPlayer(this))
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            PLAY -> play()
            PAUSE -> pause()
            STOP -> {}
            NEXT -> next()
            PREVIOUS -> previous()
            RELEASE -> release()
        }
        startSession()

        return START_STICKY
    }

    fun release() {
        audioPlayer.value.release()
        notificationManager.cancel(1)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun play() {
        audioPlayer.value.play {
            this.trackAudio(it)
        }
        startSession()
    }

    fun pause() {
        audioPlayer.value.pause()
        startSession()
    }

    fun seekTo(verseIndex: Int) {
        audioPlayer.value.seekTo(verseIndex)
        if (!audioPlayer.value.isPlaying()) resume()
        startSession()
    }

    fun setTracker(trackAudio: (VerseEntity?) -> Unit) {
        this.trackAudio = trackAudio
    }

    private fun resume() {
        audioPlayer.value.resume {
            this.trackAudio(it)
        }
    }

    private fun next() {
        seekTo(audioPlayer.value.currentVerse.value?.verseIndex?.plus(1) ?: 1)
    }

    private fun previous() {
        seekTo(audioPlayer.value.currentVerse.value?.verseIndex?.minus(1) ?: 0)
    }

    @Suppress("deprecation")
    private fun createNotificationMediaPlayer(
        context: Context,
    ): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else Notification.Builder(context)

        val playPauseIntent = Intent(context, MediaPlayerReceiver::class.java)
            .apply { action = if (audioPlayer.value.isPlaying.value) PAUSE else PLAY }
        val playPausePI = PendingIntent.getBroadcast(
            context,
            1,
            playPauseIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val playPauseAction = Notification.Action.Builder(
            Icon.createWithResource(
                context,
                if (audioPlayer.value.isPlaying.value) R.drawable.ic_pause else R.drawable.ic_play
            ),
            "PlayPause",
            playPausePI
        ).build()

        val nextIntent = Intent(context, MediaPlayerReceiver::class.java)
            .setAction(NEXT)
        val nextPI = PendingIntent.getBroadcast(
            context,
            2,
            nextIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nextAction = Notification.Action.Builder(
            Icon.createWithResource(context, R.drawable.ic_next),
            "next",
            nextPI
        ).build()

        val previousIntent = Intent(context, MediaPlayerReceiver::class.java)
            .setAction(PREVIOUS)
        val previousPI = PendingIntent.getBroadcast(
            context,
            2,
            previousIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val previousAction = Notification.Action.Builder(
            Icon.createWithResource(context, R.drawable.ic_previous),
            "next",
            previousPI
        ).build()


        return builder
            .setStyle(mediaStyle)
            .setSmallIcon(R.drawable.ic_play)
            .setOnlyAlertOnce(true)
            .addAction(previousAction)
            .addAction(playPauseAction)
            .addAction(nextAction)
            .setOngoing(true)
            .build()
    }


    override fun onBind(intent: Intent?): Binder = binder
    inner class LocalBinder : Binder() {
        fun getService(): PlaybackService = this@PlaybackService
    }

    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID"
        const val CHANNEL_NAME: String = "CHANNEL_NAME"
    }
}
