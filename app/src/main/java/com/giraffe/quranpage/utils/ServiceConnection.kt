package com.giraffe.quranpage.utils

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServiceConnection<T>(private val binderToService: (IBinder) -> T?) : ServiceConnection {
    private var _service = MutableStateFlow<T?>(null)
    var service = _service.asStateFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        _service.value = service?.let { binderToService(it) }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        _service.value = null
    }
}