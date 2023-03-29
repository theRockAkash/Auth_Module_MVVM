package com.skyviewads.foodcourt.utils

import android.content.Context
import com.skyviewads.foodcourt.utils.Constants.DEVICE_ID
import com.skyviewads.foodcourt.utils.Constants.MACHINE_ID
import com.skyviewads.foodcourt.utils.Constants.QUESTION_ON
import com.skyviewads.foodcourt.utils.Constants.TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefrenceHelper @Inject constructor(@ApplicationContext  context: Context) {
    val prefrence=context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE)
    companion object{

        val languageCode:String="languageCode"
        private val sharePrefName:String="SharePrefName"

        fun getSavedValue(context: Context, key:String):String{
            val prefrence=context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE)
            val token=prefrence.getString(key,"")
            return token!!
        }

        fun saveStringValue(context: Context, key:String, value: String){
            val prefrence=context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE)
            val editor=prefrence.edit()
            editor.putString(key,value)
            editor.apply()
        }
    }
    fun getToken(): String {

        val token=prefrence.getString(TOKEN,"")
        return token!!
    }
    fun getLang(): String {

        val token=prefrence.getString("languageCode","en")
        return token!!
    }
    fun getMachineId(): String {
        val token=prefrence.getString(MACHINE_ID,"")
        return token!!
    }
    fun saveToken(value: String) {

        val editor=prefrence.edit()
        editor.putString(TOKEN,value)
        editor.apply()
    }
    fun logOut() {
        val editor=prefrence.edit()
        editor.putString(TOKEN,"")
        editor.putString(MACHINE_ID,"")
        editor.apply()
    }
    fun saveMachineId(value: String) {
        val editor=prefrence.edit()
        editor.putString(MACHINE_ID,value)
        editor.apply()
    }

    fun saveQuestionOn(showQuestion: String) {
        val editor=prefrence.edit()
        if (showQuestion.equals("on",true))
            editor.putBoolean(QUESTION_ON,true)
        else editor.putBoolean(QUESTION_ON,false)
        editor.apply()
    }
    fun saveDeviceID(id: String) {
        val editor=prefrence.edit()
        editor.putString(DEVICE_ID,id)
        editor.apply()
    }
    fun getDeviceId(): String {
        return prefrence.getString(DEVICE_ID,"1")!!
    }
    fun isQuestionOn(): Boolean {
        return prefrence.getBoolean(QUESTION_ON,true)
    }
}
