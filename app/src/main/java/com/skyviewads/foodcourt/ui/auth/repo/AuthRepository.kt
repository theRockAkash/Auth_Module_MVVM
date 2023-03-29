package com.skyviewads.foodcourt.ui.auth.repo

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyviewads.foodcourt.network.Api
import com.skyviewads.foodcourt.network.CommonResponse
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.utils.NetworkManager
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthRepository @Inject constructor(private val api: Api,private val networkManager: NetworkManager) {
    private val _logInResponse:MutableLiveData<DataHelper<String>> = MutableLiveData<DataHelper<String>>()
    val logInResponse:LiveData<DataHelper<String>> get() = _logInResponse

    private val _signUpResponse:MutableLiveData<DataHelper<String>> = MutableLiveData<DataHelper<String>>()
    val signUpResponse:LiveData<DataHelper<String>> get() = _signUpResponse

    private val _forgetPassResponse:MutableLiveData<DataHelper<String>> = MutableLiveData<DataHelper<String>>()
    val forgetPassResponse:LiveData<DataHelper<String>> get() = _forgetPassResponse


   suspend fun userLogin (phone_number: String, password: String)  {
       if (networkManager.isNetworkAvailable())
       {
           _logInResponse.postValue(DataHelper.Loading())
           handleLogInResponse(api.userLogIn(phone_number, password))
       }
       else _logInResponse.postValue(DataHelper.Error("Please Connect Internet"))
    }

    private fun handleLogInResponse(response: Response<CommonResponse>) {
        if (response.isSuccessful)
        {
            if (response.body()!!.status)
                _logInResponse.postValue(DataHelper.Success(response.body()!!.message))
            else _logInResponse.postValue(DataHelper.Error(response.body()!!.message))

        }else handleCommonResponse(response,_logInResponse)
    }

    private fun handleCommonResponse(response: Response<CommonResponse>, _logInResponse: MutableLiveData<DataHelper<String>>) {
        if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                 _logInResponse.postValue(DataHelper.Error(errorObj.getString("message")))
            } catch (e: Exception) {
                e.printStackTrace()
                 _logInResponse.postValue(DataHelper.Error("Error Occurred"))
            }
        } else {
             _logInResponse.postValue(DataHelper.Error("Error"))
        }
    }

    suspend fun userForgetPassword (phone_number: String)  {
        if (networkManager.isNetworkAvailable())
        {
            _forgetPassResponse.postValue(DataHelper.Loading())
            handleForgetPassResponse(api.userForgotPassword(phone_number))
        }
        else _forgetPassResponse.postValue(DataHelper.Error("Please Connect Internet"))
    }

    private fun handleForgetPassResponse(response: Response<CommonResponse>) {
        if (response.isSuccessful)
        {
            if (response.body()!!.status)
                _forgetPassResponse.postValue(DataHelper.Success(response.body()!!.message))
            else _forgetPassResponse.postValue(DataHelper.Error(response.body()!!.message))
        }else handleCommonResponse(response,_forgetPassResponse)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun userRegister (username: String, email: String, phoneNumber: String, password: String)  {
        if (networkManager.isNetworkAvailable())
        {
            _signUpResponse.postValue(DataHelper.Loading())
            handleSignUpResponse(api.userRegister(username,email,phoneNumber, password))
        }
        else _signUpResponse.postValue(DataHelper.Error( "Please Connect Internet"))
    }

    private fun handleSignUpResponse(response: Response<CommonResponse>) {
        if (response.isSuccessful)
        {
            if (response.body()!!.status)
                _signUpResponse.postValue(DataHelper.Success(response.body()!!.message))
            else _signUpResponse.postValue(DataHelper.Error(response.body()!!.message))
        }else handleCommonResponse(response,_signUpResponse)
    }


}