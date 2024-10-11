package com.ssw.kast.ui.screen.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.IconButton
import com.ssw.kast.ui.component.ImagePopup
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen (
    accountManager: AccountManager,
    navController: NavHostController,
    selectedItemManager: SelectedItemManagement,
    userViewModel: UserViewModel
) {
    // ------------ fetching data -------------
    val user = remember { mutableStateOf(User()) }
    val isFollowed = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        user.value = accountManager.viewedUser!!
        if (accountManager.currentUser?.id!! != user.value.id) {
            isFollowed.value = userViewModel.isFollowing(accountManager.currentUser!!, user.value.id)
        } else {
            //viewModel.updateLoggedUser(database, accountManager)
        }
    }

    val scope = rememberCoroutineScope()

    // ----------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                )
                .padding(innerPadding)
        ) {
            BackNavBar(
                title = "Profile",
                onPressBackButton = {
                    NavigationManager.goToPreviousScreen(
                        navController = navController,
                        selectedItem = selectedItemManager,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController,"home")
                        }
                    )
                },
                underline = true
            )

            AccountHeader(
                accountManager = accountManager,
                navController = navController,
                user = user,
                isFollowed = isFollowed,
                scope = scope,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun AccountHeader(
    accountManager: AccountManager,
    navController: NavHostController,
    user: MutableState<User>,
    isFollowed: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    userViewModel: UserViewModel
) {
    // ------ fetching data ------
    val showProfilePicture = remember { mutableStateOf(false) }

    val defaultProfilePicture = getCachedImageFromResources(resourceId = R.drawable.default_profil)
    val profilePicture: ImageBitmap = user.value.profilePicture ?: defaultProfilePicture

    var followError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        followError = ""
    }
    // ---------------------------

    val profilePictureSize = 130.dp

    if (showProfilePicture.value) {
        ImagePopup(
            isShowed = showProfilePicture,
            image = profilePicture
        )
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // container of image/follower/following/username
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image (
                    bitmap = profilePicture,
                    contentScale = ContentScale.Crop,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(profilePictureSize)
                        .aspectRatio(1f)
                        .background(color = LightGrey)
                        .clickable {
                            if (user.value.profilePicture != null) {
                                showProfilePicture.value = true
                            }
                        }
                )
                Spacer(
                    modifier = Modifier
                        .width(profilePictureSize)
                        .height(16.dp)
                        .background(Color.Transparent)
                )
                Text (
                    text = user.value.username,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = user.value.followersCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text (
                    text = "follower",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = LightGrey
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = user.value.followingCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text (
                    text = "following",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = LightGrey
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // container of biography
        if (user.value.biography.isNotBlank()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text (
                    text = user.value.biography,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = LightGrey.copy(alpha = 0.7f)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        if (followError.isNotBlank()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text (
                    text = "Unexpected error: $followError",
                    fontSize = 22.sp,
                    color = Color.Red,
                )
            }
        }

        accountManager.currentUser?.let { currentUser ->
            if (currentUser.id != user.value.id) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    if (!isFollowed.value) {
                        IconButton(
                            icon = Icons.Outlined.Add,
                            label = "Follow",
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
                            onClick = {
                                scope.launch {
                                    userViewModel.follow(currentUser, user.value.id)
                                    followError = userViewModel.followError.value.error
                                    if (userViewModel.followError.value.code == 0) {
                                        user.value.followersCount += 1
                                        userViewModel.updateLoggedUser(accountManager)
                                    }

                                    isFollowed.value = true
                                }
                            },
                        )
                    } else {
                        IconButton(
                            icon = Icons.Outlined.Clear,
                            outline = true,
                            label = "Unfollow",
                            labelColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
                            onClick = {
                                scope.launch {
                                    userViewModel.unfollow(currentUser, user.value.id)
                                    followError = userViewModel.followError.value.error
                                    if (userViewModel.followError.value.code == 0) {
                                        user.value.followersCount -= 1
                                        userViewModel.updateLoggedUser(accountManager)
                                    }

                                    isFollowed.value = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("home") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController = navController, selectedItem = selectedItem)
        }

        //ProfileScreen(AccountManager(), navController,selectedItem, bottomNavigationBar)
    }
}

