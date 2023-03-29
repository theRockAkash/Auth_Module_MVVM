package com.skyviewads.foodcourt.ui.auth.forgotPassFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.ui.auth.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val forgetPassResponse: LiveData<DataHelper<String>> get() = authRepository.forgetPassResponse
    var isOtpSent:Boolean=false
    fun userForgotPassword(phone_number:String){
        viewModelScope.launch {
            authRepository.userForgetPassword(phone_number)
        }
    }

    fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password.length > 5 && password.equals(confirmPassword)
    }
}