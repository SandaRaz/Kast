package com.ssw.kast.ui.screen.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NetworkPing
import androidx.compose.material.icons.outlined.SensorDoor
import androidx.compose.material.icons.outlined.WifiTethering
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.ui.component.SignTextField
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun NetworkScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    preferencesManager: PreferencesManager
) {
    Scaffold (
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                )
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ------ retrieving data from SharedPreferences ------
            val savedProtocol = preferencesManager.getStringData("protocol", "http")
            val savedIpAddress = preferencesManager.getStringData("ipAddress", "0.0.0.0")
            val savedPort = preferencesManager.getStringData("port", "5039")

            var protocol by remember { mutableStateOf(savedProtocol) }
            var ipAddress by remember { mutableStateOf(savedIpAddress) }
            var port by remember { mutableStateOf(savedPort) }
            // ----------------------------------------------------

            BackNavBar(
                title = "Network Setting",
                onPressBackButton = {
                    NavigationManager.navigateTo(navController,"menu")
                }
            )

            Spacer(
                modifier = Modifier
                    .height(48.dp)
                    .width(8.dp)
                    .background(Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.Transparent
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignTextField (
                    value = protocol,
                    onValueChange = { newInput ->
                        protocol = newInput.lowercase()
                    },
                    label = "Protocol",
                    icon = Icons.Outlined.NetworkPing,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignTextField (
                    value = ipAddress,
                    onValueChange = { newInput ->
                        ipAddress = newInput
                    },
                    label = "Server IpAddress",
                    icon = Icons.Outlined.WifiTethering,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignTextField (
                    value = port,
                    onValueChange = { newInput ->
                        port = newInput
                    },
                    label = "Server Port",
                    icon = Icons.Outlined.SensorDoor,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                Spacer (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                SignButton(
                    label = "Save(Restart to apply)",
                    onClick = {
                        preferencesManager.saveStringData("protocol", protocol)
                        preferencesManager.saveStringData("ipAddress", ipAddress)
                        preferencesManager.saveStringData("port", port)
                        Log.d("NetworkScreen", "Saved protocol $protocol ipAddress $ipAddress and port $port")
                    }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkScreenPreview() {
    KastTheme {
        val navHostController = rememberNavController()

        val selectedItemName = ""
        val selectedItem = SelectedItemManagement(selectedItemName)

        NetworkScreen(navHostController, selectedItem, PreferencesManager(LocalContext.current))
    }
}