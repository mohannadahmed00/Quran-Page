package com.giraffe.quranpage.data.datasource.remote.downloader

import android.content.Context
import android.util.Log
import com.giraffe.quranpage.data.datasource.remote.api.AudioApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AudioDownloader(private val context: Context, private val service: AudioApiService) {

    private var inputStream: InputStream? = null
    private var downloadJob: Job? = null

    fun downloadFile(url: String, trackProgress: (Int, String, Job?, InputStream?) -> Unit) {
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    service.downloadAudio(url)
                response.let { responseBody ->
                    val fileSize = responseBody.contentLength()
                    inputStream = responseBody.byteStream()
                    val file = File(context.cacheDir, url.split(".net/")[1].replace("/", "-"))
                    saveFile(inputStream, file, fileSize, trackProgress)
                    inputStream?.close()
                }
            } catch (e: Exception) {
                Log.e(TAG, "downloadFileException: $e")
            } finally {
                inputStream?.close()
            }


        }
    }

    private fun saveFile(
        inputStream: InputStream?,
        file: File,
        fileSize: Long,
        trackProgress: (Int, String, Job?, InputStream?) -> Unit
    ) {
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var byteCopied: Long = 0
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    byteCopied += read
                    trackProgress(
                        (byteCopied.toFloat() / fileSize * 100).toInt(),
                        file.absolutePath,
                        downloadJob,
                        inputStream
                    )
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }

    companion object {
        private const val TAG = "AudioDownloader"
    }
}