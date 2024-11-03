package com.ssw.kast.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssw.kast.MainActivity
import com.ssw.kast.model.component.ThemeModel
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.ui.component.ThemeCard
import com.ssw.kast.ui.component.YesOrNoPopup
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.LightColorScheme

@Composable
fun SelectThemeScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    preferencesManager: PreferencesManager
) {
    // --------- fetching data ----------
    val context = LocalContext.current

    val currentThemeName = preferencesManager.getStringData("currentThemeName", "spotify")
    var selectedThemeName by remember { mutableStateOf(currentThemeName) }
    var selectedTheme by remember { mutableStateOf<ThemeModel?>(null) }
    val showRestartPopup = remember { mutableStateOf(false) }

    // ----------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .border(
                    1.dp,
                    Color.Transparent
                )
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            BackNavBar(
                title = "Select theme",
                onPressBackButton = {
                    NavigationManager.goToPreviousScreen(
                        navController = navController,
                        selectedItem = selectedItem,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController, "menu")
                        }
                    )
                    selectedThemeName = currentThemeName
                }
            )

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            ) {
                items(ThemeModel.themeList()) { themeModel ->
                    ThemeCard(
                        title = themeModel.name,
                        themeColors = themeModel,
                        checked = (selectedThemeName == themeModel.name)
                    ) { isChecked ->
                        if (isChecked) {
                            selectedThemeName = themeModel.name
                            selectedTheme = themeModel
                        }
                    }
                }
            }

            selectedTheme?.let { themeModel ->
                if (selectedThemeName != currentThemeName) {
                    Spacer (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .border(
                                1.dp,
                                Color.Transparent
                            )
                    )

                    SignButton(
                        label = "Save(Restart to apply)",
                        onClick = {
                            val newColorScheme = ThemeModel.switchThemeTo(themeModel, preferencesManager)
                            LightColorScheme = newColorScheme

                            showRestartPopup.value = true
                        }
                    )
                }
            }

            YesOrNoPopup(
                isShowed = showRestartPopup,
                title = "Restart app now to apply network changes ?",
                onClickYes = {
                    MainActivity.restartMainActivity(context)
                }
            )

            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            )
        }
    }
}