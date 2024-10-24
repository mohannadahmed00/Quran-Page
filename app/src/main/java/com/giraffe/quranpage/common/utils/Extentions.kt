package com.giraffe.quranpage.common.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.giraffe.quranpage.R
import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.data.datasource.local.models.SurahAudioDataModel
import com.giraffe.quranpage.domain.entities.ReciterEntity
import java.io.IOException

fun NetworkError.toString(context: Context): String {
    val stringResId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.REQUEST_TIMEOUT
        NetworkError.TOO_MANY_REQUESTS -> R.string.TOO_MANY_REQUESTS
        NetworkError.NO_INTERNET -> R.string.NO_INTERNET
        NetworkError.SERVER_ERROR -> R.string.UNKNOWN
        NetworkError.SERIALIZATION -> R.string.SERIALIZATION
        NetworkError.UNKNOWN -> R.string.UNKNOWN
    }
    return context.getString(stringResId)
}

fun NetworkError.showToast(context: Context) {
    Toast.makeText(context, this.toString(context), Toast.LENGTH_SHORT).show()
}


@Composable
fun <viewModel : LifecycleObserver> viewModel.ObserveLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserveLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@ObserveLifecycleEvents)
        }
    }
}


fun MutableList<SurahAudioDataModel>.addOrUpdate(item: SurahAudioDataModel): MutableList<SurahAudioDataModel> {
    val index = indexOfFirst { it.surahIndex == item.surahIndex }
    if (index >= 0) set(index, item) else add(item)
    return this
}

fun MutableList<ReciterEntity>.addOrUpdate(reciter: ReciterEntity?): List<ReciterEntity> {
    reciter?.let {
        val index = indexOfFirst { it.id == reciter.id }
        if (index >= 0) set(index, reciter) else add(reciter)
    }
    return this
}

fun isNetworkAvailable(): Boolean {
    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}

fun Int.toThreeDigits(): String {
    var intStr = this.toString()
    while (intStr.length < 3) {
        intStr = "0".plus(intStr)
    }
    return intStr
}
