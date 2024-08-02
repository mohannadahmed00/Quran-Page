package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.io.IOException

class MediaPlayerManager() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var isStopped = false
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0

    fun playAudio(filePath: String) {
        if (mediaPlayer?.isPlaying==false || mediaPlayer==null) {
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

            if (!isPaused) {
                try {
                    mediaPlayer?.apply {
                        setDataSource(filePath)
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

    fun seekTo(positionTime: Int) {
        currentPosition = positionTime
        mediaPlayer?.seekTo(currentPosition)
        isPaused = false
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

    fun addOnCompleteListener(onComplete:()->Unit){
        mediaPlayer?.setOnCompletionListener {
            onComplete()
        }
    }

    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }

    fun trackTime(action:(Int)->Unit) {
        handler.post(object : Runnable {
            override fun run() {
                val currentTime = mediaPlayer?.currentPosition ?: 0
                action(currentTime)
                handler.postDelayed(this, 500)
            }
        })
    }
}
