package com.ssw.kast.screen.account

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.component.LogoHeader
import com.ssw.kast.component.SignButton
import com.ssw.kast.component.SignTextField
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    preferencesManager: PreferencesManager
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
                            navHostController.navigate("home")
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
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            // Se connecter ou activer le mode offline
            /*
                Sauvegarder dans un SharredPreferences si le mode offline est activé
                Si le mode offline est désactivé mais aucune compte n'est connecté on
                renvoie cet ecran de Login
             */

            // ------------ fetching data -------------


            var usernameInput by remember { mutableStateOf("") }
            var passwordInput by remember { mutableStateOf("") }
            var offlineMode by remember { mutableStateOf(false) }

            val prefsOfflineMode = preferencesManager.getBooleanData("offlineMode", false)
            offlineMode = prefsOfflineMode
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
                SignTextField(
                    value = usernameInput,
                    onValueChange = { newInput ->
                        usernameInput = newInput
                    },
                    label = "username",
                    icon = Icons.Outlined.Person,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignTextField(
                    value = passwordInput,
                    onValueChange = { newInput ->
                        passwordInput = newInput
                    },
                    label = "password",
                    icon = Icons.Outlined.Lock,
                    secured = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignButton(
                    label = "Connect",
                    onClick = {

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
                                navHostController.navigate("sign_up")
                            }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    KastTheme {
        val navHostController = rememberNavController()
        
        LoginScreen(navHostController, PreferencesManager(LocalContext.current))
    }
}
