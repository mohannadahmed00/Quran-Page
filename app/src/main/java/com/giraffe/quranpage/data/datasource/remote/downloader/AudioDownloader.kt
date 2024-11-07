package com.giraffe.quranpage.data.datasource.remote.downloader

import android.content.Context
import com.giraffe.quranpage.data.datasource.remote.api.AudioApiService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class AudioDownloader(private val context: Context, private val service: AudioApiService) {

    private var inputStream: InputStream? = null

    @Throws(IOException::class)
    suspend fun downloadFile(url: String, trackProgress: (Int, String, InputStream?) -> Unit) {
        val response =
            service.downloadAudio(url)
        response.let { responseBody ->
            val fileSize = responseBody.contentLength()
            inputStream = responseBody.byteStream()
            val file = File(context.cacheDir, url.split(".net/")[1].replace("/", "-"))
            saveFile(inputStream, file, fileSize, trackProgress)
            inputStream?.close()
        }
    }

    private fun saveFile(
        inputStream: InputStream?,
        file: File,
        fileSize: Long,
        trackProgress: (Int, String, InputStream?) -> Unit
    ) {
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var byteCopied: Long = 0
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    byteCopied += read
                    trackProgress(
                        (byteCopied.toFloat() / fileSize * 100).toInt(),
                        file.absolutePath,
                        inputStream
                    )
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }
}