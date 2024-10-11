package com.ssw.kast.ui.screen.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.ui.component.SignTextField
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.component.InputError
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SignInViewModel
import com.ssw.kast.viewmodel.SongViewModel
import com.ssw.kast.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    database: AppDatabase,
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    accountManager: AccountManager,
    songManager: SongManager,
    signInViewModel: SignInViewModel = hiltViewModel(),
    userViewModel: UserViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // Se connecter ou activer le mode offline
    /*
        Sauvegarder dans un SharredPreferences si le mode offline est activé
        Si le mode offline est désactivé mais aucune compte n'est connecté on
        renvoie cet ecran de Login
     */

    // ------------ fetching data -------------

    var userLoginInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    val wrongCredential = remember { mutableStateOf("") }

    val prefsOfflineMode = preferencesManager.getBooleanData("offlineMode", false)
    var offlineMode by remember { mutableStateOf(prefsOfflineMode) }

    val scope = rememberCoroutineScope()

    val defaultUserPicture = getCachedImageFromResources(resourceId = R.drawable.default_profil)
    val defaultPlaylistCover = getCachedImageFromResources(resourceId = R.drawable.default_playlist_cover)
    // ----------------------------------------


    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                )
                .padding(bottom = innerPadding.calculateBottomPadding()),
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
                            NavigationManager.navigateTo(navController, "home")
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
                text = "sign in",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignTextField(
                    value = userLoginInput,
                    onValueChange = { newInput ->
                        userLoginInput = newInput
                        wrongCredential.value = ""
                    },
                    label = "username/usermail",
                    icon = Icons.Outlined.Person,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignTextField(
                    value = passwordInput,
                    onValueChange = { newInput ->
                        passwordInput = newInput
                        wrongCredential.value = ""
                    },
                    label = "password",
                    icon = Icons.Outlined.Lock,
                    secured = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                if (wrongCredential.value.isNotBlank()) {
                    InputError(error = wrongCredential.value)
                }

                SignButton(
                    label = "Connect",
                    onClick = {
                        scope.launch {
                            signInViewModel.onPressConnect(
                                database = database,
                                navController = navController,
                                userLoginInput = userLoginInput.trim(),
                                passwordInput = passwordInput.trim(),
                                credentialError = wrongCredential,
                                accountManager = accountManager,
                                songManager = songManager,
                                userDefaultPicture = defaultUserPicture,
                                userViewModel = userViewModel,
                                playlistDefaultCover = defaultPlaylistCover,
                                playlistViewModel = playlistViewModel
                            )
                        }
                    }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New? ",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Sign up",
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable{
                                NavigationManager.navigateTo(navController,"sign_up")
                            }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
/*
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = offlineMode,
                        onCheckedChange = {
                            offlineMode = it
                            preferencesManager.saveBooleanData("offlineMode", offlineMode)
                            Log.d("Login", "OfflineMode: ${preferencesManager.getBooleanData("offlineMode", false)}")
                        },
                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = MaterialTheme.colorScheme.primary,
//                            checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
//                            checkedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(
                        modifier = Modifier
                            .height(48.dp)
                            .width(8.dp)
                            .background(Color.Transparent)
                    )

                    Text(
                        text = "Offline mode",
                        color = LightGrey
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                )
*/
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Network setting",
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable{
                                NavigationManager.navigateTo(navController,"network")
                            }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "About",
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable{
                                NavigationManager.navigateTo(navController,"about")
                            }
                    )
                }

            }
        }
    }
}
