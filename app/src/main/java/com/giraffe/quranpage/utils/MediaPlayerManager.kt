package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import java.io.IOException
import kotlin.math.abs
import kotlin.math.log

class MediaPlayerManager() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var isStopped = false
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0
    private var surahAudioData: SurahAudioModel? = null
    private var pages: List<PageUI> = emptyList()


    fun setSurahAudioData(surahAudioData: SurahAudioModel?, pages: List<PageUI>) {
        this.surahAudioData = surahAudioData
        this.pages = pages
    }


    fun playAudio() {
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

    }

    fun seekTo(verseIndex: Int) {
        //it's the actual verse index (start from 1)
        Log.d("TAG", "getVerseTime: org: $verseIndex")
        /*if (verseIndex != surahAudioData?.ayahsTiming?.size) {

        }*/
        currentPosition = getVerseTime(verseIndex)
        mediaPlayer?.seekTo(currentPosition)
        isPaused = false
    }

    private fun getVerseTime(verseToReadIndex: Int): Int {
        Log.d("TAG", "getVerseTime: size: ${surahAudioData?.ayahsTiming?.size}")
        Log.d("TAG", "getVerseTime: verseIndex: $verseToReadIndex")
        if ((surahAudioData?.surahId ?: 0) == 1) {
            if ((surahAudioData?.ayahsTiming?.size ?: 0) == 7) {
                Log.d("TAG", "getVerseTime: result: ${surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex - 1) ?: 0}")
                return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex - 1)?.startTime ?: 0
            }
        }
        Log.d("TAG", "getVerseTime: result: ${surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex) ?: 0}")

        return surahAudioData?.ayahsTiming?.getOrNull(verseToReadIndex)?.startTime ?: 0
    }


    private fun resumeAudio() {
        mediaPlayer?.seekTo(currentPosition)
        mediaPlayer?.start()
        isPaused = false
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
        isPaused = true
        currentPosition = mediaPlayer?.currentPosition ?: 0
        stopTrackingTime()
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        isPaused = false
        isStopped = true
        stopTrackingTime()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopTrackingTime()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun addOnCompleteListener(onComplete: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            onComplete()
        }
    }

    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }


    fun trackTime(action: (VerseModel?) -> Unit) {
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
        val ayahPageIndex =
            if (ayahTiming?.pageUrl != null) ayahTiming.getPageIndexFromUrl() else surahAudioData?.ayahsTiming
                ?.get(
                    1
                )
                ?.getPageIndexFromUrl()
        val pageUi = pages[ayahPageIndex?.minus(1) ?: 0]
        val content = pageUi.contents.firstOrNull { content ->
            content.verses.firstOrNull { v -> v.verseNumber == ayahTiming?.ayahIndex && v.surahNumber == surahAudioData?.surahId } != null
        }
        var verseIndex =
            content?.verses?.indexOfFirst { v -> v.verseNumber == ayahTiming?.ayahIndex }
                ?: 0
        /*if (verseIndex == -1) {
            verseIndex = 0
        } else {
            if ((surahAudioData?.surahId ?: 0) == 1) {
                if ((surahAudioData?.ayahsTiming?.size
                        ?: 0) == 7
                ) {
                    verseIndex++
                }
            }
        }*/
        return content?.verses
            ?.get(verseIndex)
    }
}

//ibrahim
//23uz + (1) => 0 : 12570
//(2) => 13213 : 19085

//3agamy
//23uz => 0 : 2459
//(1) => 2775 : 5390
//(2) => 5657 : 9885