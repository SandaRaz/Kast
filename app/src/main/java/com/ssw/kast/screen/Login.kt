package com.ssw.kast.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

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
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoHeader()

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            // Se connecter ou activer le mode offline
            /*
                Sauvegarder dans un SharredPreferences si le mode offline est activé
                Si le mode offline est activé mais aucune compte n'est connecté on
                renvoie cet ecran de Login
             */

            // ------------ fetching data -------------


            // ----------------------------------------
            var usernameInput by remember { mutableStateOf("sanda_raz") }
            var passwordInput by remember { mutableStateOf("") }

            SignTextField(
                value = usernameInput,
                onValueChange = { newInput ->
                    usernameInput = newInput
                },
                placeholder = "username",
                icon = Icons.Outlined.Person
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )

            SignTextField(
                value = passwordInput,
                onValueChange = { newInput ->
                    passwordInput = newInput
                },
                placeholder = "password",
                icon = Icons.Outlined.Lock,
                secured = true
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    secured: Boolean = false
) {
    val cornerShape = 12.dp
    var visiblePassword by remember { mutableStateOf(false) }

    val visibilityIcon = if (visiblePassword) Icons.Filled.Visibility
        else Icons.Filled.VisibilityOff

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = LightGrey
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null)
        },
        trailingIcon = {
            if (secured) {
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable{
                            visiblePassword = !visiblePassword
                        }
                )
            }
        },
        visualTransformation = if (secured && !visiblePassword) PasswordVisualTransformation()
            else VisualTransformation.None,
        shape = RoundedCornerShape(cornerShape),
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerShape)
            ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    KastTheme {
        val navController = rememberNavController()
        
        LoginScreen(navHostController = navController)
    }
}
