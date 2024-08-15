package com.ssw.kast.screen

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
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.NetworkWifi
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.component.BottomNavigationBar
import com.ssw.kast.component.MenuItem
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.model.User
import com.ssw.kast.model.getImageFromResources
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

@Composable
fun MenuScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
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
                .verticalScroll(rememberScrollState(), true, null, false)
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            val image1 = getImageFromResources(resourceId = R.drawable.ic_launcher_foreground)
            val user1 = User("sanda_raz", "sr@gmail.com", image1)

            val cornerShape = 14.dp

            val onClickAccount: () -> Unit = {
                //TO-DO
            }

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
                        shape = RoundedCornerShape(
                            topStart = cornerShape,
                            topEnd = cornerShape
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = cornerShape,
                            topEnd = cornerShape
                        )
                    )
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = user1.profilePicture,
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
                        },
                    contentScale = ContentScale.FillBounds
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
                        text = user1.username,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable {
                                onClickAccount()
                            }
                    )
                    Text(
                        text = user1.usermail,
                        color = LightGrey,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.background
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(
                            bottomStart = cornerShape,
                            bottomEnd = cornerShape
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(
                            bottomStart = cornerShape,
                            bottomEnd = cornerShape
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            bottomStart = cornerShape,
                            bottomEnd = cornerShape
                        )
                    )
            ) {
                MenuItem(
                    icon = Icons.Outlined.PersonOutline,
                    label = "Login",
                    onClick = {
                        navController.navigate("login")
                    }
                )
                MenuItem(
                    icon = Icons.Outlined.PersonOutline,
                    label = "Edit profile"
                )
                MenuItem(
                    icon = Icons.Outlined.NetworkWifi,
                    label = "Network"
                )
                MenuItem(
                    icon = Icons.Outlined.Settings,
                    label = "Setting"
                )
                MenuItem(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    label = "Log out"
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

        val selectedItemName by remember { mutableStateOf("search") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController, selectedItem)
        }

        MenuScreen(navController, selectedItem, bottomNavigationBar)
    }
}