package com.giraffe.quranpage.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.net.toUri
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AudioPlayerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition = 0


    private val _ayahs  = MutableStateFlow<List<VerseModel>>(emptyList())
    val ayahs  = _ayahs.asStateFlow()
    private val _surahAudioData = MutableStateFlow<SurahAudioModel?>(null)
    val surahAudioData = _surahAudioData.asStateFlow()
    private val _reciter = MutableStateFlow<ReciterModel?>(null)
    val reciter = _reciter.asStateFlow()
    private val _currentVerse = MutableStateFlow<VerseModel?>(null)
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


    // Initialize the media player with an audio source
    fun initializePlayer(
        context: Context,
        surahAudioData: SurahAudioModel?,
        currentVerse: VerseModel?,
        surahName: String,
        reciterName: String
    ) {
        _currentVerse.value = currentVerse
        _surahAudioData.value = surahAudioData
        _surahName.value = surahName
        _reciterName.value = reciterName


        release() // Release any existing player instance
        mediaPlayer = MediaPlayer.create(context, surahAudioData?.audioPath?.toUri())
        mediaPlayer?.setOnPreparedListener {
            _isPrepared.value = true
            _isCompleted.update { false }
            Log.d(
                "AudioPlayer",
                "Media Player is prepared. from ${_currentVerse.value?.surahNumber} : ${_currentVerse.value?.verseNumber} "
            )
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d("AudioPlayer", "Playback complete.")
            //stop()
            stopTrackingTime()
            this._currentVerse.value = null
            _isPlaying.update { false }
            _isCompleted.update { true }
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
    fun play(trackAudio: (VerseModel?) -> Unit) {
        if (_isPrepared.value) {
            seekTo(_currentVerse.value?.verseNumber ?: 0)
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

    fun resume(trackAudio: (VerseModel?) -> Unit) {
        mediaPlayer?.start()
        trackTime { trackAudio(it) }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun getCurrentDuration() = (mediaPlayer?.currentPosition ?: 0).toLong()
    fun getDuration() = (mediaPlayer?.duration ?: 0).toLong()

    // Stop the audio
    private fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            stopTrackingTime()
            Log.d("AudioPlayer", "Audio stopped.")
            _isPrepared.value = false
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun clearAudioData(){
        _surahAudioData.update { null }
    }

    fun seekTo(verseIndex: Int) {
        if (_isPrepared.value) {
            currentPosition = getVerseTime(verseIndex)
            _currentVerse.value = getVerseFromTiming(currentPosition)
            mediaPlayer?.seekTo(currentPosition)
            Log.d("AudioPlayer", "Seeking to verse Index $verseIndex at $currentPosition ms.")
        }
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }


    // Release resources when done
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        //_surahAudioData.value = null
        _isPrepared.value = false
        stopTrackingTime()
        Log.d("AudioPlayer", "Media player resources released.")
        _isPlaying.update { mediaPlayer?.isPlaying ?: false }
    }

    fun setSurahAudioData(surahAudioData:SurahAudioModel?){
        Log.d("AudioPlayer", "setSurahAudioData($surahAudioData)")
        _surahAudioData.value= surahAudioData
    }

    fun setReciter(reciter:ReciterModel?){
        Log.d("AudioPlayer", "setReciter($reciter)")
        _reciter.value= reciter
    }

    // Check if the audio is playing
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }


    fun setAyahs(ayahs: List<VerseModel>) {
        _ayahs.value = ayahs
    }


    private fun getVerseTime(verseToReadIndex: Int): Int {
        if ((_surahAudioData.value?.surahId ?: 0) == 1) {
            if ((_surahAudioData.value?.ayahsTiming?.size ?: 0) == 7) {
                return _surahAudioData.value?.ayahsTiming?.getOrNull(verseToReadIndex - 1)?.startTime
                    ?: 0
            }
        }
        return _surahAudioData.value?.ayahsTiming?.getOrNull(verseToReadIndex)?.startTime ?: 0
    }

    private fun stopTrackingTime() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun trackTime(action: (VerseModel?) -> Unit) {
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

    private fun getVerseFromTiming(trackPosition: Int): VerseModel? {
        Log.d("AudioPlayerManager", "getVerseFromTiming($trackPosition)")

        val ayahTiming =
            _surahAudioData.value?.ayahsTiming?.firstOrNull { ayah -> trackPosition >= ayah.startTime && trackPosition <= ayah.endTime }
        return _ayahs.value.firstOrNull {
            it.verseNumber == (ayahTiming?.ayahIndex
                ?: 0) && it.surahNumber == _surahAudioData.value?.surahId
        }
    }
}