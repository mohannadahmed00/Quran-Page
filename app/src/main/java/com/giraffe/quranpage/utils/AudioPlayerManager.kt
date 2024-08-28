package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.net.toUri
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException
import kotlin.math.abs
import kotlin.math.log

object AudioPlayerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var surahAudioData: SurahAudioModel? = null
    private var ayahs: List<VerseModel> = emptyList()
    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared: Boolean = false
    private var currentVerse:VerseModel? = null
    private var currentPosition = 0
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    // Initialize the media player with an audio source
    fun initializePlayer(context: Context,surahAudioData: SurahAudioModel?,currentVerse:VerseModel?) {
        this.currentVerse = currentVerse
        this.surahAudioData = surahAudioData


        release() // Release any existing player instance
        mediaPlayer = MediaPlayer.create(context, surahAudioData?.audioPath?.toUri())
        mediaPlayer?.setOnPreparedListener {
            isPrepared = true
            Log.d("AudioPlayer", "Media Player is prepared. from ${currentVerse?.surahNumber} : ${currentVerse?.verseNumber} ")
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d("AudioPlayer", "Playback complete.")
            stopTrackingTime()
            _isPlaying.update { mediaPlayer?.isPlaying ?: false }
            //stop()
        }
        mediaPlayer?.setOnErrorListener { _, what, extra ->
            Log.e("AudioPlayer", "Error occurred: What = $what, Extra = $extra")
            stopTrackingTime()
            stop()
            _isPlaying.update { mediaPlayer?.isPlaying ?: false }
            true
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    // Play the audio if prepared
    fun play(trackAudio:(VerseModel?) -> Unit) {
        if (isPrepared) {
            seekTo(currentVerse?.verseNumber ?: 0)
            mediaPlayer?.start()
            trackTime { trackAudio(it) }
            Log.d("AudioPlayer", "Playing audio.")
        } else {
            Log.d("AudioPlayer", "Player not prepared yet.")
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    // Pause the audio
    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            stopTrackingTime()
            Log.d("AudioPlayer", "Audio paused.")
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun resume(trackAudio:(VerseModel?) -> Unit) {
        mediaPlayer?.start()
        trackTime { trackAudio(it) }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    // Stop the audio
    private fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            stopTrackingTime()
            Log.d("AudioPlayer", "Audio stopped.")
            isPrepared = false
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun seekTo(verseIndex: Int) {
        if (isPrepared) {
            currentPosition = getVerseTime(verseIndex)
            currentVerse = getVerseFromTiming(currentPosition)
            mediaPlayer?.seekTo(currentPosition)
            Log.d("AudioPlayer", "Seeking to verse Index $verseIndex at $currentPosition ms.")
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    // Release resources when done
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
        stopTrackingTime()
        Log.d("AudioPlayer", "Media player resources released.")
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    // Check if the audio is playing
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }










    fun setAyahs(ayahs: List<VerseModel>) {
        this.ayahs = ayahs
    }









    private fun getVerseTime(verseToReadIndex: Int): Int {
        if ((surahAudioData?.surahId ?: 0) == 1) {
            if ((surahAudioData?.ayahsTiming?.size ?: 0) == 7) {
                return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex - 1)?.startTime ?: 0
            }
        }
        return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex)?.startTime ?: 0
    }
    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }
    private fun trackTime(action: (VerseModel?) -> Unit) {
        stopTrackingTime()
        handler.post(object : Runnable {
            override fun run() {
                currentPosition = mediaPlayer?.currentPosition ?: 0
                currentVerse = getVerseFromTiming(currentPosition)
                action(currentVerse)
                handler.postDelayed(this, 500)
            }
        })
    }
    private fun getVerseFromTiming(trackPosition: Int): VerseModel? {
        val ayahTiming =
            surahAudioData?.ayahsTiming?.firstOrNull { ayah -> trackPosition >= ayah.startTime && trackPosition <= ayah.endTime }
        return ayahs.firstOrNull { it.verseNumber == (ayahTiming?.ayahIndex ?: 0) && it.surahNumber == surahAudioData?.surahId  }
    }
}