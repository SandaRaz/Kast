package com.ssw.kast.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.model.persistence.PreferencesManager

@Composable
fun AppNavigation(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    preferencesManager: PreferencesManager
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("search") {
            SearchScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("menu") {
            MenuScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("content_details") {
            ContentDetails(navController, bottomNavigationBar)
        }
        composable("content_details2/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // Screen(id)
        }
        composable("login") {
            LoginScreen(navController, preferencesManager)
        }
        composable("sign_up") {
            SignUpScreen(navController)
        }
        composable("sign_up_music_genre") {
            SignUpMusicGenreScreen(navController)
        }
    }
}