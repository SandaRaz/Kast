package com.ssw.kast.ui.screen.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.component.FieldGroupTitle
import com.ssw.kast.ui.component.ItemPicker
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.OutlineSignButton
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.SignUpViewModel

@Composable
fun SignUpMusicGenreScreen(
    navController: NavHostController,
    authManager: AuthManager,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                )
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = "Backward",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            NavigationManager.navigateTo(navController,"sign_up")
                        }
                )

                LogoHeader(
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Text(
                text = "sign up",
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            // ------------ fetching data -------------
            LaunchedEffect(Unit) {
                viewModel.onLoadChooseMusicGenreScreen()
            }

            val newUser = authManager.newUser
            Log.d("ChooseMusicGenre", "$newUser")

            val musicGenres by viewModel.musicGenres
            val musicGenreList = mutableListOf<PickerElement>()
            musicGenres.forEach { musicGenre ->
                musicGenreList.add(PickerElement(musicGenre.type, musicGenre))
            }

            val pickedMusicGenres = remember { mutableStateListOf<PickerElement>() }

            LaunchedEffect(Unit) {
                if (authManager.newUserMusicGenres.isNotEmpty()) {
                    authManager.newUserMusicGenres.forEach { selectedMusicGenre ->
                        pickedMusicGenres.add(PickerElement(selectedMusicGenre.type, selectedMusicGenre))
                    }
                }
            }
            // ----------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FieldGroupTitle(title = "Pick your favourites music genres")

                ItemPicker(
                    label = "music genres",
                    pickedItems = pickedMusicGenres,
                    listItems = musicGenreList,
                    onAddItem = { item ->
                        if(pickedMusicGenres.none { it.label == item.label }) {
                            pickedMusicGenres.add(item)
                        }
                    },
                    onDeleteItem = { item ->
                        pickedMusicGenres.remove(item)
                    },
                    withTopPadding = true,
                    withBottomPadding = true
                )

                if (pickedMusicGenres.isEmpty()) {
                    OutlineSignButton(
                        label = "pass",
                        onClick = {
                            NavigationManager.navigateTo(navController,"sign_up_finalization")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    SignButton(
                        label = "next",
                        onClick = {
                            val newMusicGenres = mutableListOf<MusicGenre>()
                            pickedMusicGenres.forEach { picked ->
                                newMusicGenres.add(picked.value as MusicGenre)
                            }
                            authManager.newUserMusicGenres = newMusicGenres

                            NavigationManager.navigateTo(navController,"sign_up_finalization")
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpMusicGenreScreenPreview() {
    KastTheme {
        val navHostController = rememberNavController()
        val authManager = AuthManager()

        SignUpMusicGenreScreen(navHostController, authManager)
    }
}