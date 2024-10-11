package com.ssw.kast.ui.screen.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NetworkWifi
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.MainActivity
import com.ssw.kast.R
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.MenuItem
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.ui.component.SignOutPopup
import com.ssw.kast.ui.component.YesOrNoPopup
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.viewmodel.UserViewModel

@Composable
fun MenuScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    accountManager: AccountManager,
    songManager: SongManager,
    userViewModel: UserViewModel
) {
    // ------ fetching data ------
    var loggedUser by remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }

        userViewModel.saveListenedSongs(loggedUser, songManager)
    }

    val cornerShape = 14.dp

    val onClickAccount: () -> Unit = {
        accountManager.viewedUser = loggedUser
        NavigationManager.navigateTo(navController,"profile")
    }

    val profilePicture: ImageBitmap = if (loggedUser.profilePicture != null) {
        loggedUser.profilePicture!!
    } else {
        getCachedImageFromResources(resourceId = R.drawable.default_profil)
    }

    val showSignOutPopup = remember { mutableStateOf(false) }
    val showExitPopup = remember { mutableStateOf(false) }

    // ---------------------------

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                bottomNavigationBar()
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
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
            // current logged account container
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(
                            topStart = cornerShape,
                            topEnd = cornerShape
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(cornerShape)
                    )
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(cornerShape)
                    )
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = profilePicture,
                    contentScale = ContentScale.Crop,
                    contentDescription = "account picture",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                        .background(
                            color = LightGrey
                        )
                        .clickable {
                            onClickAccount()
                        }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(16.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .border(
                            width = 1.dp,
                            color = Color.Transparent
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = loggedUser.username,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable {
                                onClickAccount()
                            }
                    )

                    if (loggedUser.usermail.isNotBlank()) {
                        Text(
                            text = loggedUser.usermail,
                            color = LightGrey,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clickable {
                                    onClickAccount()
                                }
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.background
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(
                            bottomStart = cornerShape,
                            bottomEnd = cornerShape
                        )
                    )
                    .padding(
                        bottom = 16.dp
                    )
            ) {
                /*
                MenuItem(
                    icon = Icons.Outlined.PersonOutline,
                    label = "Edit profile"
                )
                */
                MenuItem(
                    icon = Icons.Outlined.NetworkWifi,
                    label = "Network",
                    onClick = {
                        NavigationManager.navigateTo(navController,"network")
                    }
                )
                /*
                MenuItem(
                    icon = Icons.Outlined.Settings,
                    label = "Setting"
                )
                 */
                MenuItem(
                    icon = Icons.Outlined.Info,
                    label = "About",
                    onClick = {
                        NavigationManager.navigateTo(navController, "about")
                    }
                )

                MenuItem(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    label = "Log out",
                    onClick = {
                        showSignOutPopup.value = true
                    }
                )

                MenuItem(
                    icon = Icons.AutoMirrored.Outlined.ExitToApp,
                    label = "Exit",
                    containerColor = Color.Red.copy(alpha = 0.1f),
                    onClick = {
                        showExitPopup.value = true
                    }
                )

                SignOutPopup(
                    isShowed = showSignOutPopup,
                    title = "Are you sure you want to log out ?",
                    navController = navController,
                    accountManager = accountManager,
                    songManager = songManager
                )

                YesOrNoPopup(
                    isShowed = showExitPopup,
                    title = "Are you sure you want to exit ?",
                    onClickYes = {
                        MainActivity.exit()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("menu") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController, selectedItem)
        }

        val accountManager = AccountManager()

        //MenuScreen(navController, selectedItem, bottomNavigationBar, accountManager)
    }
}