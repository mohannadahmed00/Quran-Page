package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException
import kotlin.math.abs
import kotlin.math.log

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var isStopped = false
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0
    private var surahAudioData: SurahAudioModel? = null
    private var ayahs: List<VerseModel> = emptyList()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    fun setSurahAudioData(surahAudioData: SurahAudioModel?) {
        this.surahAudioData = surahAudioData
    }

    fun setAyahs(ayahs: List<VerseModel>) {
        this.ayahs = ayahs
    }


    fun playAudio(removeSelectedVerse: () -> Unit,trackAudio:(VerseModel?) -> Unit) {
        if (mediaPlayer?.isPlaying == false || mediaPlayer == null) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            } else {
                if (isPaused) {
                    resumeAudio()
                    return
                } else {
                    mediaPlayer?.reset()
                }
            }

            surahAudioData?.let {
                if (!isPaused) {
                    try {
                        mediaPlayer?.apply {
                            setDataSource(it.audioPath)
                            prepare()
                            start()
                            isPaused = false
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
        if (_isPlaying.value){
            trackTime(trackAudio)
            mediaPlayer?.setOnCompletionListener {
                release()
                _isPlaying.update { false }
                removeSelectedVerse()
            }
        }
    }

    fun seekTo(verseIndex: Int) {
        currentPosition = getVerseTime(verseIndex)
        mediaPlayer?.seekTo(currentPosition)
        isPaused = false
    }

    private fun getVerseTime(verseToReadIndex: Int): Int {
        if ((surahAudioData?.surahId ?: 0) == 1) {
            if ((surahAudioData?.ayahsTiming?.size ?: 0) == 7) {
                return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex - 1)?.startTime ?: 0
            }
        }
        return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex)?.startTime ?: 0
    }


    private fun resumeAudio() {
        mediaPlayer?.seekTo(currentPosition)
        mediaPlayer?.start()
        isPaused = false
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
        isPaused = true
        currentPosition = mediaPlayer?.currentPosition ?: 0
        stopTrackingTime()
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        isPaused = false
        isStopped = true
        stopTrackingTime()
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun release() {

        mediaPlayer?.release()
        mediaPlayer = null
        stopTrackingTime()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }


    fun trackTime(action: (VerseModel?) -> Unit) {
        stopTrackingTime()
        handler.post(object : Runnable {
            override fun run() {
                val currentTime = mediaPlayer?.currentPosition ?: 0
                action(getVerseFromTiming(currentTime))
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