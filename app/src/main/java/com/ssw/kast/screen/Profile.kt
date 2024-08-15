package com.ssw.kast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.component.BottomNavigationBar
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.model.Country
import com.ssw.kast.model.Gender
import com.ssw.kast.model.User
import com.ssw.kast.model.getImageFromResources
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import org.threeten.bp.LocalDate

@Composable
fun ProfileScreen (
    idUser: Any,
    navController: NavHostController,
    selectedItemManager: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true, null, false)
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                )
                .padding(innerPadding)
        ) {
            // ------------ fetching data -------------
            val image1 = getImageFromResources(resourceId = R.drawable.ic_launcher_foreground)
            val gender1 = Gender("1", "Male")
            val country1 = Country("1", "Madagascar")

            val user1 = User(
                id = "1",
                username = "sanda_raz",
                usermail = "sr@gmail.com",
                passwordHash = "",
                dateOfBirth = LocalDate.of(2000,1,1),
                profilePicture = image1,
                biography = "carpe diem",
                followersCount = 10,
                followingCount = 3,
                createdAt = LocalDate.now(),
                gender = gender1,
                country = country1
            )
            // ----------------------------------------

            AccountHeader(navController = navController, user = user1)
        }
    }
}

@Composable
fun AccountHeader(
    navController: NavHostController,
    user: User,
    modifier: Modifier = Modifier
) {
    val profilePictureSize = 150.dp

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
                    bitmap = user.profilePicture,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(profilePictureSize)
                        .aspectRatio(1f)
                        .background(color = LightGrey)
                )
                Spacer(
                    modifier = Modifier
                        .width(profilePictureSize)
                        .height(16.dp)
                        .background(Color.Transparent)
                )
                Text (
                    text = user.username.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = user.followersCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text (
                    text = "follower",
                    style = MaterialTheme.typography.titleMedium,
                    color = LightGrey
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = user.followingCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text (
                    text = "following",
                    style = MaterialTheme.typography.titleMedium,
                    color = LightGrey
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        // container of biography
        if (user.biography.isNotBlank()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text (
                    text = user.biography,
                    style = MaterialTheme.typography.titleMedium,
                    color = LightGrey
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
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

        ProfileScreen("001", navController,selectedItem, bottomNavigationBar)
    }
}

