package com.giraffe.quranpage.remote.downloader

import android.content.Context
import android.util.Log
import com.giraffe.quranpage.utils.toThreeDigits
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class FileDownloader(
    private val context: Context,
    private val httpClient: OkHttpClient.Builder
) {
    fun download(folderUrl: String, surahIndex: Int,onComplete: (String?) -> Unit) {
        //https://server7.mp3quran.net/basit/Almusshaf-Al-Mojawwad/020.mp3
        val url = folderUrl + surahIndex.toThreeDigits() + ".mp3"
        Log.d("TAG", "QuranContent 0: $url")

        val request = Request.Builder()
            .url(url)
            .build()
        httpClient.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onComplete(null)
            }
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onComplete(null)
                    return
                }
                val inputStream = response.body?.byteStream()
                val file = File(context.cacheDir, url.split(".net/")[1].replace("/", "-"))
                saveFile(inputStream, file)
                onComplete(file.absolutePath)
            }
        })


    }

    private fun saveFile(inputStream: InputStream?, file: File) {
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }
}