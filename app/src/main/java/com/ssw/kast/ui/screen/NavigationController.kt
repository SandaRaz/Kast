package com.ssw.kast.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.screen.account.FinalSignUpScreen
import com.ssw.kast.ui.screen.account.ProfileScreen
import com.ssw.kast.ui.screen.account.SignInScreen
import com.ssw.kast.ui.screen.account.SignUpMusicGenreScreen
import com.ssw.kast.ui.screen.account.SignUpScreen
import com.ssw.kast.ui.screen.feed.HomeScreen
import com.ssw.kast.ui.screen.menu.AboutScreen
import com.ssw.kast.ui.screen.menu.MenuScreen
import com.ssw.kast.ui.screen.menu.NetworkScreen
import com.ssw.kast.ui.screen.menu.SelectThemeScreen
import com.ssw.kast.ui.screen.music.CategoriesScreen
import com.ssw.kast.ui.screen.music.CategoryMusicScreen
import com.ssw.kast.ui.screen.music.MusicScreen
import com.ssw.kast.ui.screen.music.PlayerScreen
import com.ssw.kast.ui.screen.music.PlaylistMusicScreen
import com.ssw.kast.ui.screen.music.PlaylistScreen
import com.ssw.kast.ui.screen.music.RecentScreen
import com.ssw.kast.ui.screen.music.StreamedScreen
import com.ssw.kast.ui.screen.music.SuggestionsScreen
import com.ssw.kast.ui.screen.search.SearchScreen
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SearchViewModel
import com.ssw.kast.viewmodel.SongViewModel
import com.ssw.kast.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    database: AppDatabase,
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    preferencesManager: PreferencesManager,
    songManager: SongManager,
    authManager: AuthManager,
    accountManager: AccountManager
) {
    // ------ Setup some viewModels ------
    val playlistViewModel: PlaylistViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val songViewModel: SongViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    // -----------------------------------

    NavHost(navController = navController, startDestination = "home") {
        composable("about") {
            selectedItem.onSelectItem("menu")
            AboutScreen(navController, selectedItem)
        }
        composable("categories") {
            selectedItem.onSelectItem("music")
            CategoriesScreen(navController, selectedItem, songManager, songViewModel)
        }
        composable("category_music") {
            selectedItem.onSelectItem("music")
            CategoryMusicScreen(navController, selectedItem, accountManager, songManager, songViewModel, playlistViewModel)
        }
        composable("content_details") {
            ContentDetails(navController, bottomNavigationBar)
        }
        composable("content_details2/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // Screen(id)
        }
        composable("home") {
            selectedItem.onSelectItem("home")
            HomeScreen(accountManager, songManager,navController, selectedItem, bottomNavigationBar, userViewModel, songViewModel, playlistViewModel)
        }
        composable("menu") {
            selectedItem.onSelectItem("menu")
            MenuScreen(navController, selectedItem, bottomNavigationBar, accountManager, songManager, userViewModel)
        }
        composable("music") {
            selectedItem.onSelectItem("music")
            MusicScreen(navController, selectedItem, bottomNavigationBar, accountManager, songManager, songViewModel, playlistViewModel)
        }
        composable("network") {
            selectedItem.onSelectItem("menu")
            NetworkScreen(navController, selectedItem, preferencesManager)
        }
        composable("player") {
            selectedItem.onSelectItem("music")
            PlayerScreen(navController, selectedItem, songManager, accountManager, songViewModel, userViewModel)
        }
        composable("playlist") {
            selectedItem.onSelectItem("music")
            PlaylistScreen(navController, selectedItem, accountManager, songManager, playlistViewModel)
        }
        composable("playlist_music") {
            selectedItem.onSelectItem("music")
            PlaylistMusicScreen(navController, selectedItem, accountManager, songManager, playlistViewModel)
        }
        composable("profile") {
            selectedItem.onSelectItem("menu")
            ProfileScreen(accountManager , navController, selectedItem, userViewModel)
        }
        composable("recent") {
            selectedItem.onSelectItem("music")
            RecentScreen(navController, selectedItem, accountManager, songManager, playlistViewModel)
        }
        composable("search") {
            selectedItem.onSelectItem("search")
            SearchScreen(navController, selectedItem, bottomNavigationBar, accountManager, songManager, searchViewModel, playlistViewModel)
        }
        composable("sign_in") {
            SignInScreen(database, navController, preferencesManager, accountManager, songManager, userViewModel = userViewModel, playlistViewModel = playlistViewModel)
        }
        composable("sign_up") {
            SignUpScreen(navController, authManager)
        }
        composable("sign_up_finalization") {
            FinalSignUpScreen(database, navController, authManager, accountManager, songManager, userViewModel = userViewModel, playlistViewModel = playlistViewModel)
        }
        composable("sign_up_music_genre") {
            SignUpMusicGenreScreen(navController, authManager)
        }
        composable("streamed") {
            StreamedScreen(navController, selectedItem, accountManager, songManager, songViewModel, playlistViewModel)
        }
        composable("suggestion") {
            selectedItem.onSelectItem("music")
            SuggestionsScreen(navController, selectedItem, accountManager, songManager, songViewModel, playlistViewModel)
        }
        composable("theme") {
            SelectThemeScreen(navController, selectedItem, preferencesManager)
        }
    }
}

class NavigationManager {
    companion object {
        fun goToPreviousScreen(
            navController: NavHostController,
            selectedItem: SelectedItemManagement,
            onPreviousRouteNull: () -> Unit = {
                navigateTo(navController,"home")
            }
        ) {
            val previousBackStackEntry = navController.previousBackStackEntry
            val previousRoute = previousBackStackEntry?.destination?.route
            if(previousRoute != null){
                selectedItem.onSelectItem(previousRoute)
                navController.popBackStack()
            } else {
                onPreviousRouteNull()
            }
        }

        fun navigateTo(navController: NavHostController, route: String) {
            navController.navigate(route) {
                popUpTo(route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }

        @SuppressLint("RestrictedApi")
        fun displayCurrentStack(navController: NavHostController) {
            val currentBackStack = navController.currentBackStack
            val listBS = currentBackStack.value
            val stackRouteNames = mutableListOf<String>()
            listBS.forEach { stackEntry ->
                stackRouteNames.add("${stackEntry.destination.route}")
            }
            Log.d("CurrentStack", "Stack: $stackRouteNames")
        }
    }
}