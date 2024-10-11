package com.ssw.kast.ui.screen.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.KastLoader
import com.ssw.kast.ui.component.ListCardVerticalContainer
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.viewmodel.SongViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    songManager: SongManager,
    songViewModel: SongViewModel
) {
    // ---------- Fetching data server ----------
    val scope = rememberCoroutineScope()

    val defaultCategoryCover = getCachedImageFromResources(resourceId = R.drawable.default_category_cover)

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        songViewModel.loadAllCategoriesCard(
            songManager,
            navController,
            defaultCategoryCover
        )
        isLoading = false
    }
    val categories by songViewModel.allCategoriesListCards

    // ------------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            BackNavBar(
                title = "Song categories (${categories.size})",
                onPressBackButton = {
                    NavigationManager.navigateTo(navController,"home")
                },
                extraIcon = Icons.Outlined.Refresh,
                onClickExtraIcon = {
                    scope.launch {
                        isLoading = true
                        songViewModel.refreshAllCategoriesCard(
                            songManager = songManager,
                            navController = navController,
                            categoriesCover = defaultCategoryCover
                        )
                        isLoading = false
                    }
                }
            )

            if (isLoading) {
                KastLoader(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    boxSize = 130.dp
                )
            }

            if (categories.isNotEmpty() && !isLoading) {
                ListCardVerticalContainer(
                    listListCard = categories,
                    contentHeight = 85.dp,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            songManager.currentSong?.let {
                CurrentSongBar(
                    navController = navController,
                    songManager = songManager,
                    onClickPlay = {
                        songManager.clickCurrentSong()
                    }
                )
            }
        }
    }
}