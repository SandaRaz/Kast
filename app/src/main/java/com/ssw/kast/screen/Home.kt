package com.ssw.kast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.component.BottomNavigationBar
import com.ssw.kast.component.LogoHeader
import com.ssw.kast.component.MusicCardHorizontalContainer
import com.ssw.kast.component.MusicCardVerticalContainer
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.component.SmallCardContainer
import com.ssw.kast.component.TitleBar
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.component.SmallCardModel
import com.ssw.kast.model.getImageFromResources
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                bottomNavigationBar()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true, null, false)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            LogoHeader()

            TitleBar(
                title = "Musics",
                isNavigable = true,
                onClick = {
                    selectedItem.onSelectItem("content_details")
                    navController.navigate("content_details")
                }
            )

            val testImage: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_foreground)
            val musicCards: List<MusicCardModel> = listOf(
                MusicCardModel("Everybody knows","John Legend", testImage),
                MusicCardModel("Titre1","Singer1", getImageFromResources(resourceId = R.drawable.kast_old)),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre3","Singer3", testImage)
            )
            MusicCardVerticalContainer(listMusicCard = musicCards)

            TitleBar(title = "Users", isNavigable = true)

            val testImage2: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_background)
            val smallCards: List<SmallCardModel> = listOf(
                SmallCardModel(testImage2, "anonymous7_anonymous7", "5 followers"),
                SmallCardModel(testImage2, "xoxo10", "17 followers"),
                SmallCardModel(testImage2, "peanuts_", "8 followers"),
            )
            SmallCardContainer(listSmallCard = smallCards)

            TitleBar(title = "Musics")
            MusicCardHorizontalContainer(listMusicCard = musicCards)

            TitleBar(title = "Musics")
            MusicCardHorizontalContainer(listMusicCard = musicCards)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("home") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController = navController, selectedItem = selectedItem)
        }

        HomeScreen(navController, selectedItem, bottomNavigationBar)
    }
}