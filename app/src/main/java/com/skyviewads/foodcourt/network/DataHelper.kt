package com.skyviewads.foodcourt.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DataHelper<T>(val data: T?=null,val msg:String="") {

    class Success<T>(data:T) : DataHelper<T>(data = data)
    class Error<T>(msg: String) : DataHelper<T>(msg = msg)
    class Loading<T>() : DataHelper<T>()
}
