package com.ssw.kast.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.TitleBar
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun ContentDetails(navController: NavHostController, bottomNavigationBar: @Composable () -> Unit) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(ScrollState(0), enabled = true, null, false)
        ) {
            TitleBar(title = "Pop musics")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentDetailsPreview(){
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("content_details") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        ContentDetails(navController, bottomNavigationBar = {
            BottomNavigationBar(navController = navController, selectedItem = selectedItem)
        })
    }
}