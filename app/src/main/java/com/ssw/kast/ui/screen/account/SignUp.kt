package com.ssw.kast.ui.screen.account

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.component.DatePickerField
import com.ssw.kast.ui.component.DropDownList
import com.ssw.kast.ui.component.FieldGroupTitle
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.SignButton
import com.ssw.kast.ui.component.SignTextField
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.model.global.dateToString
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.ui.component.InputError
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavHostController,
    authManager: AuthManager,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    // ------------ fetching data -------------
    LaunchedEffect(Unit) {
        viewModel.onLoadSignUpScreen()
    }
    val scope = rememberCoroutineScope()

    val genders by viewModel.genders
    val genderList = mutableListOf<PickerElement>()
    genders.forEach { gender ->
        genderList.add(PickerElement(gender.type, gender))
    }

    val countries by viewModel.countries
    val countryList = mutableListOf<PickerElement>()
    countries.forEach { country ->
        countryList.add(PickerElement(country.name, country))
    }

    val usernameInput = remember { mutableStateOf("") }
    val usernameError = remember { mutableStateOf("") }

    val usermailInput = remember { mutableStateOf("") }
    val usermailError = remember { mutableStateOf("") }

    val dateOfBirthInput = remember { mutableStateOf("") }
    val dateOfBirthError = remember { mutableStateOf("") }

    val passwordInput = remember { mutableStateOf("") }
    val passwordLengthError = remember { mutableStateOf("") }

    val confirmPasswordInput = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }

    val selectedGender = remember { mutableStateOf(PickerElement("", null)) }
    val selectedGenderError = remember { mutableStateOf("") }

    val selectedCountry = remember { mutableStateOf(PickerElement("", null)) }
    val selectedCountryError = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val authUser = authManager.newUser
        if (authUser.username.isNotBlank()) {
            usernameInput.value = authUser.username
        }
        if (authUser.usermail.isNotBlank()) {
            usermailInput.value = authUser.usermail
        }
        val dateOfBirthString = dateToString(authUser.dateOfBirth)
        if (dateOfBirthString.isNotBlank()) {
            dateOfBirthInput.value = dateOfBirthString
        }
        if (authUser.country.name.isNotBlank()) {
            selectedCountry.value = PickerElement(authUser.country.name, authUser.country)
        }
        if (authUser.gender.type.isNotBlank()) {
            selectedGender.value = PickerElement(authUser.gender.type, authUser.gender)
        }
    }
    // ----------------------------------------

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    contentDescription = "Back to sign in",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            authManager.reinitialize()
                            NavigationManager.navigateTo(navController,"sign_in")
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
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

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
                    value = usernameInput.value,
                    onValueChange = {
                        usernameInput.value = it
                        usernameError.value = ""
                    },
                    label = "username *",
                    icon = Icons.Outlined.Person,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )
                if (usernameError.value.isNotBlank()) {
                    InputError(usernameError.value)
                }

                SignTextField(
                    value = usermailInput.value,
                    onValueChange = {
                        usermailInput.value = it
                        usermailError.value = ""
                    },
                    label = "email",
                    icon = Icons.Outlined.AlternateEmail,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true
                )
                if (usermailError.value.isNotBlank()) {
                    InputError(usermailError.value)
                }

                DatePickerField(
                    onDateSelected = {
                        dateOfBirthInput.value = it
                        dateOfBirthError.value = ""
                    },
                    label = "date of birth",
                    withBottomPadding = true
                )
                if (dateOfBirthError.value.isNotBlank()) {
                    InputError(dateOfBirthError.value)
                }

                DropDownList(
                    label = "gender",
                    selected = selectedGender.value,
                    onSelectItem = { selected ->
                        selectedGender.value = selected
                        selectedGenderError.value = ""
                    },
                    items = genderList,
                    withBottomPadding = true
                )
                if (selectedGenderError.value.isNotBlank()) {
                    InputError(selectedGenderError.value)
                }

                DropDownList(
                    label = "country",
                    selected = selectedCountry.value,
                    onSelectItem = { selected ->
                        selectedCountry.value = selected
                        selectedCountryError.value = ""
                    },
                    items = countryList,
                    withBottomPadding = true
                )
                if (selectedCountryError.value.isNotBlank()) {
                    InputError(selectedCountryError.value)
                }

                FieldGroupTitle(title = "Password")

                SignTextField(
                    value = passwordInput.value,
                    onValueChange = {
                        passwordInput.value = it

                        passwordLengthError.value = if (it.isNotBlank() && it.length < 6) {
                            "Password must contains at least 6 characters"
                        } else {
                            ""
                        }
                    },
                    label = "password *",
                    icon = Icons.Outlined.Lock,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withTopPadding = true,
                    withBottomPadding = true,
                    secured = true
                )
                if (passwordLengthError.value.isNotBlank()) {
                    InputError(passwordLengthError.value)
                }


                SignTextField(
                    value = confirmPasswordInput.value,
                    onValueChange = {
                        confirmPasswordInput.value = it

                        confirmPasswordError.value = if (it != passwordInput.value && passwordInput.value.isNotBlank()) {
                            "Password confirmation error"
                        } else {
                            ""
                        }
                    },
                    label = "confirm password *",
                    icon = Icons.Outlined.Lock,
                    modifier = Modifier
                        .fillMaxWidth(),
                    withBottomPadding = true,
                    secured = true
                )
                if (confirmPasswordError.value.isNotBlank()) {
                    InputError(confirmPasswordError.value)
                }

                SignButton(
                    label = "next",
                    onClick = {
                        scope.launch {
                            viewModel.onPressNext(
                                navController = navController,
                                usernameInput = usernameInput.value.trim(),
                                usernameError = usernameError,
                                usermailInput = usermailInput.value.trim(),
                                usermailError = usermailError,
                                dateOfBirthInput = dateOfBirthInput.value,
                                dateOfBirthError = dateOfBirthError,
                                selectedGender = selectedGender,
                                selectedGenderError = selectedGenderError,
                                selectedCountry = selectedCountry,
                                selectedCountryError = selectedCountryError,
                                passwordInput = passwordInput.value,
                                passwordLengthError = passwordLengthError,
                                confirmPasswordInput = confirmPasswordInput.value,
                                confirmPasswordError = confirmPasswordError,
                                authManager = authManager
                            )
                        }
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
        val authManager = AuthManager()

        SignUpScreen(navHostController, authManager)
    }
}