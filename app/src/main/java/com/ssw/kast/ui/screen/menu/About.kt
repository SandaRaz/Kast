package com.ssw.kast.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.TitleBar
import com.ssw.kast.ui.screen.NavigationManager

@Composable
fun AboutScreen(
    navController: NavHostController
) {
    // ---------- Preparing data ------------

    val welcome = "Welcome to Kast! I am excited to bring you a seamless audio streaming experience. " +
            "My app allows you to listen to audio content anytime when you are connected to our server " +
            "KastCore."

    val streamingQuality = "Enjoy your audio content in stunning quality, i've used advanced Hls streaming " +
            "technology to ensure you have the best experience."

    val userInterface = "Navigate easily with Kast intuitive interface and discover new content effortlessly"

    val contact = "I value your feedback! If you have any questions, suggestions, or issues, please don't " +
            "hesitate to reach out to me: "
    val email = "25sandanirina@gmail.com"
    val facebook = "Sanda Razanajatovo"

    val copyrightInfo = " 2024 Kast. Personal Project. " +
            "\n Kast is developed by Sanda Razanajatovo. " +
            "\n All audio content and logos are the property of their respective owners. If you believe " +
            "any content is infringing on your copyright, please contact me at the email above."

    // --------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            BackNavBar(
                title = "About",
                onPressBackButton = {
                    NavigationManager.navigateTo(navController, "menu")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(thickness = 2.dp,color = MaterialTheme.colorScheme.primary)

            Column (
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Welcome to ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Kast")
                        }
                        append("! I am excited to bring you a seamless audio streaming experience. " +
                                "My app allows you to listen to audio content anytime when you are connected to our server ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("KastCore")
                        }
                        append(".")
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            TitleBar(
                title = "Features",
                titleColor = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )

            Column (
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Streaming: ")
                        }
                        append(streamingQuality)
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("User-Friendly Interface: ")
                        }
                        append(userInterface)
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            TitleBar(
                title = "Contact",
                titleColor = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )

            Column (
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(contact)
                        append("\n")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Email: ")
                        }
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(email)
                        }
                        append("\n")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Facebook: ")
                        }
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(facebook)
                        }
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            TitleBar(
                title = "Copyright Information",
                titleColor = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )

            Box (
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Copyright,
                    contentDescription = "Copyright",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(22.dp)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .padding(top = 2.dp)
                )
                Text (
                    text = buildAnnotatedString {
                        append("      2024 ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Kast")
                        }
                        append(". Personal Project.")
                        append("\n")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Kast ")
                        }
                        append("is developed by ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append("Sanda Razanajatovo")
                        }
                        append(". \n")
                        append("All audio content and logos are the property of their respective owners. If you believe " +
                                "any content is infringing on your copyright, please contact me at the email above.")
                    }
                )
            }
        }
    }
}