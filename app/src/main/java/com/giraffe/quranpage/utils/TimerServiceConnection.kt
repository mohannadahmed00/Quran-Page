package com.giraffe.quranpage.utils

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.giraffe.quranpage.service.DownloadService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerServiceConnection : ServiceConnection {
    private var _service = MutableStateFlow<DownloadService?>(null)
    var service = _service.asStateFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        this._service.value = (service as DownloadService.LocalBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        _service.value = null
    }
}