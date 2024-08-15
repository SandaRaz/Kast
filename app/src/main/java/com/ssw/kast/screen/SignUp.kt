package com.ssw.kast.screen

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
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.component.DatePickerField
import com.ssw.kast.component.DropDownList
import com.ssw.kast.component.FieldGroupTitle
import com.ssw.kast.component.LogoHeader
import com.ssw.kast.component.SignButton
import com.ssw.kast.component.SignTextField
import com.ssw.kast.model.abstractListOfCountry
import com.ssw.kast.model.abstractListOfGender
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun SignUpScreen(
    navHostController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
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
                            navHostController.navigate("login")
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

            // ------------ fetching data -------------
            val countryList = mutableListOf<PickerElement>()
            abstractListOfCountry().forEach { country ->
                countryList.add(PickerElement(country.name, country))
            }

            val genderList = mutableListOf<PickerElement>()
            abstractListOfGender().forEach { gender ->
                genderList.add(PickerElement(gender.type, gender))
            }

            var usernameInput by remember { mutableStateOf("") }
            var mailInput by remember { mutableStateOf("") }
            var dateOfBirth by remember { mutableStateOf("") }
            var passwordInput by remember { mutableStateOf("") }
            var confirmPasswordInput by remember { mutableStateOf("") }
            var selectedCountry by remember { mutableStateOf(PickerElement("", null)) }
            var selectedGender by remember { mutableStateOf(PickerElement("", null)) }
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
                    onValueChange = {
                        usernameInput = it
                    },
                    label = "username *",
                    icon = Icons.Outlined.Person,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                SignTextField(
                    value = mailInput,
                    onValueChange = {
                        mailInput = it
                    },
                    label = "email",
                    icon = Icons.Outlined.AlternateEmail,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )

                DatePickerField(
                    onDateSelected = {
                         dateOfBirth = it
                    },
                    label = "date of birth",
                    withBottomPadding = true
                )

                DropDownList(
                    label = "your country",
                    selected = selectedCountry,
                    onSelectItem = { selected ->
                        selectedCountry = selected
                    },
                    items = countryList,
                    withBottomPadding = true
                )

                DropDownList(
                    label = "your gender",
                    selected = selectedGender,
                    onSelectItem = { selected ->
                        selectedGender = selected
                    },
                    items = genderList,
                    withBottomPadding = true
                )

                FieldGroupTitle(title = "Password")

                SignTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                    },
                    label = "password *",
                    icon = Icons.Outlined.Lock,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withTopPadding = true,
                    withBottomPadding = true,
                    secured = true
                )

                SignTextField(
                    value = confirmPasswordInput,
                    onValueChange = {
                        confirmPasswordInput = it
                    },
                    label = "confirm password *",
                    icon = Icons.Outlined.Lock,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true,
                    secured = true
                )

                SignButton(
                    label = "next",
                    onClick = {

                        navHostController.navigate("sign_up_music_genre")
                    }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    KastTheme {
        val navHostController = rememberNavController()

        SignUpScreen(navHostController)
    }
}