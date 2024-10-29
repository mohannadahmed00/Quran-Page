package com.giraffe.quranpage.common.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AudioPlayerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition = 0
    private val _allVerses = MutableStateFlow<List<VerseEntity>>(emptyList())
    private val _surahAudioData = MutableStateFlow<SurahAudioDataEntity?>(null)
    val surahAudioData = _surahAudioData.asStateFlow()
    private val _currentVerse = MutableStateFlow<VerseEntity?>(null)
    val currentVerse = _currentVerse.asStateFlow()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    private val _isPrepared = MutableStateFlow(false)
    val isPrepared = _isPrepared.asStateFlow()
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()
    private val _surahName = MutableStateFlow("")
    val surahName = _surahName.asStateFlow()
    private val _reciterName = MutableStateFlow("")
    val reciterName = _reciterName.asStateFlow()


    fun initializePlayer(
        context: Context,
        surahAudioData: SurahAudioDataEntity?,
        currentVerse: VerseEntity?,
        surahName: String,
        reciterName: String
    ) {
        _currentVerse.value = currentVerse
        _surahAudioData.value = surahAudioData
        _surahName.value = surahName
        _reciterName.value = reciterName
        release()
        mediaPlayer = MediaPlayer.create(context, surahAudioData?.audioPath?.toUri())
        mediaPlayer?.setOnPreparedListener {
            _isPrepared.value = true
            _isCompleted.update { false }
        }
        mediaPlayer?.setOnCompletionListener {
            stopTrackingTime()
            this._currentVerse.value = null
            _isPlaying.update { false }
            _isCompleted.update { true }
        }
        mediaPlayer?.setOnErrorListener { _, _, _ ->
            stopTrackingTime()
            stop()
            _isPlaying.update { mediaPlayer?.isPlaying ?: false }
            true
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    fun play(trackAudio: (VerseEntity?) -> Unit) {
        if (_isPrepared.value) {
            seekTo(_currentVerse.value?.verseIndex ?: 0)
            mediaPlayer?.start()
            trackTime { trackAudio(it) }
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            stopTrackingTime()
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun resume(trackAudio: (VerseEntity?) -> Unit) {
        mediaPlayer?.start()
        trackTime { trackAudio(it) }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun getCurrentDuration() = (mediaPlayer?.currentPosition ?: 0).toLong()
    fun getDuration() = (mediaPlayer?.duration ?: 0).toLong()

    private fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            stopTrackingTime()
            _isPrepared.value = false
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun clearAudioData() {
        _surahAudioData.update { null }
    }

    fun seekTo(verseIndex: Int) {
        if (_isPrepared.value) {
            currentPosition = getVerseTime(verseIndex)
            _currentVerse.value = getVerseFromTiming(currentPosition)
            mediaPlayer?.seekTo(currentPosition)
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPrepared.value = false
        stopTrackingTime()
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun setSurahAudioData(surahAudioData: SurahAudioDataEntity?) {
        _surahAudioData.value = surahAudioData
    }


    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }


    fun setAllVerses(verses: List<VerseEntity>) {
        _allVerses.value = verses
    }


    private fun getVerseTime(verseToReadIndex: Int): Int {
        if ((_surahAudioData.value?.surahIndex ?: 0) == 1) {
            if ((_surahAudioData.value?.verseTiming?.size ?: 0) == 7) {
                return _surahAudioData.value?.verseTiming?.getOrNull(verseToReadIndex - 1)?.startTime
                    ?: 0
            }
        }
        return _surahAudioData.value?.verseTiming?.getOrNull(verseToReadIndex)?.startTime ?: 0
    }

    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun trackTime(action: (VerseEntity?) -> Unit) {
        stopTrackingTime()
        handler.post(object : Runnable {
            override fun run() {
                currentPosition = mediaPlayer?.currentPosition ?: 0
                _currentVerse.value = getVerseFromTiming(currentPosition)
                action(_currentVerse.value)
                handler.postDelayed(this, 500)
            }
        })
    }

    private fun getVerseFromTiming(trackPosition: Int): VerseEntity? {
        val ayahTiming =
            _surahAudioData.value?.verseTiming?.firstOrNull { ayah -> trackPosition >= ayah.startTime && trackPosition <= ayah.endTime }
        return _allVerses.value.firstOrNull {
            it.verseIndex == (ayahTiming?.verseIndex
                ?: 0) && it.surahIndex == _surahAudioData.value?.surahIndex
        }
    }
}