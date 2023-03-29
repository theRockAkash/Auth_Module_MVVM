package com.skyviewads.foodcourt.network

/**
 * Authentication result : success (user details) or error message.
 */
data class CommonResponse (
     val message: String,
     val status:Boolean
)