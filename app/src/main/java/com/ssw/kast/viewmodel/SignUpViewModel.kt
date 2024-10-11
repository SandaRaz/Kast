package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.model.entity.Country
import com.ssw.kast.model.entity.Gender
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.dateFromString
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.network.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){

    private val _usernameError = mutableStateOf(ErrorCatcher())
    val usernameError: State<ErrorCatcher> get() = _usernameError

    private val _usermailError = mutableStateOf(ErrorCatcher())
    val usermailError: State<ErrorCatcher> get() = _usermailError

    private val _ageError = mutableStateOf(ErrorCatcher())
    val ageError: State<ErrorCatcher> get() = _ageError

    private val _countries = mutableStateOf<List<Country>>(emptyList())
    val countries: State<List<Country>> = _countries

    private val _genders = mutableStateOf<List<Gender>>(emptyList())
    val genders: State<List<Gender>> = _genders

    private val _musicGenres = mutableStateOf<List<MusicGenre>>(emptyList())
    val musicGenres: State<List<MusicGenre>> = _musicGenres

    private suspend fun checkUserInput(
        username: String,
        usermail: String,
        dateOfBirth: String
    ) {
        try {
            _usernameError.value = userRepository.isValidUsername(username)

            if (usermail.isNotBlank()) {
                _usermailError.value = userRepository.isValidUsermail(usermail)
            }

            _ageError.value = userRepository.isValidAge(dateOfBirth)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("SignUpViewModel", "Exception: ${e.message}")
        }
    }

    fun onLoadSignUpScreen() {
        viewModelScope.launch {
            try {
                _countries.value = userRepository.getAllCountry()
                _genders.value = userRepository.getAllGender()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("SignUpViewModel", "Exception: ${e.message}")
            }
        }
    }

    suspend fun onPressNext(
        navController: NavHostController,
        usernameInput: String,
        usernameError: MutableState<String>,
        usermailInput: String,
        usermailError: MutableState<String>,
        dateOfBirthInput: String,
        dateOfBirthError: MutableState<String>,
        selectedGender: MutableState<PickerElement>,
        selectedGenderError: MutableState<String>,
        selectedCountry: MutableState<PickerElement>,
        selectedCountryError: MutableState<String>,
        passwordInput: String,
        passwordLengthError: MutableState<String>,
        confirmPasswordInput: String,
        confirmPasswordError: MutableState<String>,
        authManager: AuthManager
    ) {
        this.checkUserInput(usernameInput, usermailInput, dateOfBirthInput)

        var inputError = 0

        if (usernameInput.isBlank()) {
            usernameError.value = "Username doesn't have any value"
            inputError++
        } else {
            if (this.usernameError.value.code != 0) {
                usernameError.value = this.usernameError.value.error
                inputError++
            }
        }

        if (this.usermailError.value.code != 0) {
            usermailError.value = this.usermailError.value.error
            inputError++
        }

        if (dateOfBirthInput.isBlank()) {
            dateOfBirthError.value = "Select your date of birth"
            inputError++
        } else {
            if (this.ageError.value.code != 0) {
                dateOfBirthError.value = this.ageError.value.error
                inputError++
            }
        }

        if (selectedGender.value.value == null) {
            selectedGenderError.value = "Select your gender"
            inputError++
        }

        if (selectedCountry.value.value == null) {
            selectedCountryError.value = "Select your country"
            inputError++
        }

        if (passwordInput.isBlank()) {
            passwordLengthError.value = "Password must contains at least 6 characters"
        } else {
            if (passwordInput != confirmPasswordInput) {
                confirmPasswordError.value = "Password confirmation error"
                inputError++
            }
        }

        if (inputError == 0) {
            val newUser = User()
            newUser.id = UUID.randomUUID()
            newUser.username = usernameInput
            newUser.usermail = usermailInput
            newUser.passwordHash = passwordInput
            newUser.dateOfBirth = dateFromString(dateOfBirthInput)

            val userGender: Gender = selectedGender.value.value as Gender
            newUser.genderId = userGender.id
            newUser.gender = userGender

            val userCountry: Country = selectedCountry.value.value as Country
            newUser.countryId = userCountry.id
            newUser.country = userCountry

            authManager.newUser = newUser
            navController.navigate("sign_up_music_genre")
        }
    }

    fun onLoadChooseMusicGenreScreen() {
        viewModelScope.launch {
            try {
                _musicGenres.value = userRepository.getAllMusicGenre()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("SignUpViewModel", "Exception: ${e.message}")
            }
        }
    }

    suspend fun signUp(database: AppDatabase, accountManager: AccountManager, user: User): ErrorCatcher {
        return user.signUp(database, userRepository, accountManager)
    }
}