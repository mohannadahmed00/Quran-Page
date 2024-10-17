package com.giraffe.quranpage

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.metrics.performance.JankStats
import com.giraffe.quranpage.presentation.ui.theme.QuranPageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var jankStats: JankStats
    private val jankFrameListener = JankStats.OnFrameListener { frameData ->
        if (frameData.isJank) {
            Log.v("JankStatsSample", frameData.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            QuranPageTheme(darkTheme = false) { AppNavGraph() }
        }
        jankStats = JankStats.createAndTrack(window, jankFrameListener)
    }

    override fun onResume() {
        super.onResume()
        jankStats.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats.isTrackingEnabled = false
    }
}
