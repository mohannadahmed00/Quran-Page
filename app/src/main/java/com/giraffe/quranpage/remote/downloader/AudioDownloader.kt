package com.giraffe.quranpage.remote.downloader

import android.content.Context
import android.os.Environment
import android.util.Log
import com.giraffe.quranpage.remote.api.AudioService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AudioDownloader(
    private val context: Context,
    private val service:AudioService){
    fun downloadFile(url: String,trackProgress:(Int,String)->Unit) {
        CoroutineScope(Dispatchers.IO) .launch{
            try {
                val response =
                    service.downloadAudio(url)
                response.let { responseBody ->
                    val fileSize = responseBody.contentLength()
                    val inputStream: InputStream = responseBody.byteStream()
                    val file = File(context.cacheDir, url.split(".net/")[1].replace("/", "-"))
                    saveFile(inputStream,file,fileSize,trackProgress)
                    /*val outputStream = FileOutputStream(file)
                    val buffer = ByteArray(1024)
                    var byteCopied: Long = 0
                    var bytes: Int
                    while (inputStream.read(buffer).also { bytes = it } != -1) {
                        byteCopied += bytes
                        val progress = (byteCopied.toFloat() / fileSize * 100).toInt()
                        outputStream.write(buffer, 0, bytes)

                        progressTrack(progress,file.absolutePath)
                    }
                    outputStream.close()*/
                    inputStream.close()
                }
            } catch (e: Exception) {
                Log.e("TAG", "downloadFileR:$e ")
            }


        }
    }

    private fun saveFile(inputStream: InputStream?, file: File,fileSize:Long,trackProgress:(Int,String)->Unit) {
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var byteCopied: Long = 0
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    byteCopied += read
                    trackProgress((byteCopied.toFloat() / fileSize * 100).toInt(),file.absolutePath)
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }

}