package com.skyviewads.foodcourt.ui.auth.signupFragment

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyviewads.foodcourt.R
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.ui.auth.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm
    val singUpResponse: LiveData<DataHelper<String>> get() = authRepository.signUpResponse
    fun userRegister(username: String, email: String, phoneNumber: String, password: String) {
        viewModelScope.launch {
            authRepository.userRegister(username, email, phoneNumber, password)
        }
    }

    fun signUpDataChanged(
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ) {
        if (username.isEmpty()) {
            _signUpForm.value = SignUpFormState()
        } else if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(
                userNameError = R.string.invalid_name.toString(),
                isDataValid = false
            )
        } else if (email.isEmpty()) {
            _signUpForm.value = SignUpFormState()
        } else if (!isEmailValid(email)) {
            _signUpForm.value =
                SignUpFormState(emailError = R.string.invalid_email.toString(), isDataValid = false)
        } else if (phoneNumber.isEmpty()) {
            _signUpForm.value = SignUpFormState()
        } else if (!isPhoneValid(phoneNumber)) {
            _signUpForm.value =
                SignUpFormState(phoneError = R.string.invalid_phone.toString(), isDataValid = false)
        } else if (password.isEmpty()) {
            _signUpForm.value = SignUpFormState()
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(
                passwordError = R.string.invalid_password.toString(),
                isDataValid = false
            )
        } else if (confirmPassword.isEmpty()) {
            _signUpForm.value = SignUpFormState()
        } else if (!isConfirmPasswordValid(password, confirmPassword)) {
            _signUpForm.value = SignUpFormState(
                confirmPasswordError = R.string.password_mismatch.toString(),
                isDataValid = false
            )
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password.length > 5 && password.equals(confirmPassword)
    }

    fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank() && username.length > 2 && username.contains(" ")
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.isNotBlank() && phone.length > 8
    }
}