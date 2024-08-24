package com.ssw.kast.screen.account

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.component.FieldGroupTitle
import com.ssw.kast.component.ItemPicker
import com.ssw.kast.component.LogoHeader
import com.ssw.kast.component.OutlineSignButton
import com.ssw.kast.component.SignButton
import com.ssw.kast.model.entity.abstractListOfMusicGenre
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun SignUpMusicGenreScreen(
    navHostController: NavHostController
) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true, null, false)
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
                    contentDescription = "Back to home",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navHostController.navigate("sign_up")
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
            val musicGenreList = mutableListOf<PickerElement>()
            abstractListOfMusicGenre().forEach { musicGenre ->
                musicGenreList.add(PickerElement(musicGenre.type, musicGenre))
            }

            val pickedMusicGenres = remember { mutableStateListOf<PickerElement>() }
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

                val nextStep: () -> Unit = {

                }

                if (pickedMusicGenres.isEmpty()) {
                    OutlineSignButton(
                        label = "pass",
                        onClick = {
                            nextStep()
                        }
                    )
                } else {
                    SignButton(
                        label = "next",
                        onClick = {
                            nextStep()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpMusicGenreScreenPreview() {
    KastTheme {
        val navHostController = rememberNavController()

        SignUpMusicGenreScreen(navHostController)
    }
}