package com.giraffe.quranpage

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.QuranScreen
import com.giraffe.quranpage.ui.screens.search.SearchScreen
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(navController = navController, startDestination = HomeScreenRoute) {
            composable<HomeScreenRoute> {
                QuranScreen(navController = navController)
            }
            composable<SearchScreenRoute>{
                SearchScreen(navController = navController)
            }
            //HomeScreen()
        }
    }
}

@Serializable
object HomeScreenRoute

@Serializable
object SearchScreenRoute
//data class SearchScreenRoute(val id: String)

fun NavController.navigateToSearch(){
    navigate(SearchScreenRoute)
}
