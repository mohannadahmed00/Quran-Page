package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

class MediaPlayerManager() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var isStopped = false

    fun playAudio(filePath: String) {
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

    private fun resumeAudio() {
        mediaPlayer?.start()
        isPaused = false
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
        isPaused = true
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()

        isPaused = false
        isStopped = true
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun addOnCompleteListener(onComplete:()->Unit){
        mediaPlayer?.setOnCompletionListener {
            onComplete()
        }
    }
}
