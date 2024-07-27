package com.ssw.kast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun LoginScreen(
    navHostController: NavHostController
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
                    color = Color.Blue
                )
                .padding(innerPadding)
        ) {
            LogoHeader()

            // Se connecter ou activer le mode offline
            /*
                Sauvegarder dans un SharredPreferences si le mode offline est activé
                Si le mode offline est activé mais aucune compte n'est connecté on
                renvoie cet ecran de Login
             */

            // ------------ fetching data -------------


            // ----------------------------------------
        }
    }
}

@Composable
fun SignTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row (
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black
            )
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    KastTheme {
        val navController = rememberNavController()
        
        LoginScreen(navHostController = navController)
    }
}
