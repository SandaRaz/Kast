package com.ssw.kast.ui.screen.account

import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.model.global.dateToString
import com.ssw.kast.model.global.getBase64FromUri
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.ui.component.ImagePickerSample
import com.ssw.kast.ui.component.InputError
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.OutlineSignButton
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.ui.component.TitleAndValue
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SignUpViewModel
import com.ssw.kast.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

@Composable
fun FinalSignUpScreen(
    database: AppDatabase,
    navController: NavHostController,
    authManager: AuthManager,
    accountManager: AccountManager,
    songManager: SongManager,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    userViewModel: UserViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // -------- fetching data --------
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var profilPictureUri by remember { mutableStateOf<Uri?>(null) }

    var signUpError by remember { mutableStateOf("") }

    val userDefaultPicture = getCachedImageFromResources(R.drawable.default_profil)
    val playlistDefaultCover = getCachedImageFromResources(R.drawable.default_playlist_cover)
    // -------------------------------

    Scaffold (
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
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
                            NavigationManager.navigateTo(navController, "sign_up_music_genre")
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

            Column (
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    )
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    text = "Review your information:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                TitleAndValue(
                    title = "Username",
                    value = authManager.newUser.username,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
                TitleAndValue(
                    title = "Email",
                    value = authManager.newUser.usermail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
                TitleAndValue(
                    title = "Date of birth",
                    value = dateToString(authManager.newUser.dateOfBirth),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                TitleAndValue(
                    title = "Gender",
                    value = authManager.newUser.gender.type,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )

                TitleAndValue(
                    title = "Country",
                    value = authManager.newUser.country.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
            }

            ImagePickerSample(
                imageUri = profilPictureUri,
                title = "Profile picture (optional)",
                titleColor = MaterialTheme.colorScheme.tertiary,
                onChooseImage = {
                    profilPictureUri = it
                },
                onRemoveImage = {
                    profilPictureUri = null
                }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            )

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.Transparent
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (signUpError.isNotBlank()) {
                    InputError(signUpError)
                }
                
                OutlineSignButton(
                    label = "back",
                    onClick = {
                        NavigationManager.navigateTo(navController,"sign_up")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                SignButton(
                    label = "Finalize",
                    onClick = {
                        signUpError = ""

                        val user = authManager.newUser
                        user.createdAt = LocalDateTime.now()
                        user.musicGenres = authManager.newUserMusicGenres
                        profilPictureUri?.let {
                            user.profilePictureCode = getBase64FromUri(context, it)
                        }

                        scope.launch {
                            Log.d("FinalSignUp", "USER: \n $user")

                            try {
                                val err = signUpViewModel.signUp(database, accountManager, user)
                                if (err.code == 0) {
                                    signUpError = ""

                                    userViewModel.refreshNewestUsers(
                                        accountManager = accountManager,
                                        navController = navController,
                                        loggedUserId = user.id,
                                        amount = 5,
                                        defaultProfilePicture = userDefaultPicture
                                    )

                                    songManager.refreshRecentSong(user.id)
                                    playlistViewModel.refreshUserPlaylists(user.id)
                                    playlistViewModel.refreshPlaylistCards(
                                        navController = navController,
                                        amount = 5,
                                        defaultCover = playlistDefaultCover
                                    )
                                    playlistViewModel.refreshPlaylistPickers(user.id)

                                    NavigationManager.navigateTo(navController,"home")
                                } else {
                                    signUpError = err.error
                                }
                            } catch (e: Exception) {
                                Log.e("SignUp Finalization", "Exception: ${e.message}")
                                e.printStackTrace()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalSignUpScreenPreview() {
    KastTheme {
        val navController = rememberNavController()
        val authManager = AuthManager()

        //FinalSignUpScreen(navController, authManager)
    }
}