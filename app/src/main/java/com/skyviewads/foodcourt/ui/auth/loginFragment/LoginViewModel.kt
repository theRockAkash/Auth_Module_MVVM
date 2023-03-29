package com.skyviewads.foodcourt.ui.auth.loginFragment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.skyviewads.foodcourt.R
import com.skyviewads.foodcourt.network.CommonResponse
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.ui.auth.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    val logInResponse:LiveData<DataHelper<String>> get() = loginRepository.logInResponse

    fun userLogIn(username: String, password: String) {
        viewModelScope.launch {
            loginRepository.userLogin(username, password)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun loginDataChanged(username: String, password: String) {
        if ( username.isEmpty()) {
            _loginForm.value = LoginFormState( phoneError =  "" ,"",isDataValid = false)
        } else if (!isUserPhoneValid(username)) {
            _loginForm.value = LoginFormState( phoneError =  R.string.invalid_phone.toString() ,"",isDataValid = false)
        } else if (password.isEmpty()) {
            _loginForm.value = LoginFormState(passwordError = "", phoneError = "", isDataValid = false)
        }else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password.toString(), phoneError = "", isDataValid = false)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true, phoneError = "", passwordError = "")
        }
    }

      fun isUserPhoneValid(username: String): Boolean {
        return username.isNotBlank()&&username.length>8
    }
      fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}