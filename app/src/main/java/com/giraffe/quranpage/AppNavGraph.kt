package com.giraffe.quranpage

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.giraffe.quranpage.presentation.ui.screens.quran.QuranScreen
import com.giraffe.quranpage.presentation.ui.screens.search.SearchScreen
import kotlinx.serialization.Serializable


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(navController = navController, startDestination = QuranScreenRoute) {
            composable<QuranScreenRoute> {
                QuranScreen(navController = navController)
            }
            composable<SearchScreenRoute> {
                SearchScreen(navController = navController)
            }
        }
    }
}

@Serializable
object QuranScreenRoute

@Serializable
object SearchScreenRoute

fun NavController.navigateToSearch() {
    navigate(SearchScreenRoute)
}