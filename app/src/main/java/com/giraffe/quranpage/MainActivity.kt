package com.giraffe.quranpage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.giraffe.quranpage.ui.screens.home.HomeScreen
import com.giraffe.quranpage.ui.screens.quran.QuranScreen
import com.giraffe.quranpage.ui.theme.QuranPageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            QuranPageTheme(
                darkTheme = false
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    QuranScreen()
                    //HomeScreen()
                }
            }
        }
    }
}
