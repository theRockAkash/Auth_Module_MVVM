package com.skyviewads.foodcourt.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("customer/login")
    suspend fun userLogIn(
        @Field("phone_number") phone_number: String,
        @Field("password") password: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("customer/password/forget")
    suspend fun userForgotPassword(
        @Field("phone_number") phone_number: String,
    ): Response<CommonResponse>

    @POST("logout")
    suspend fun userLogOut(): Response<CommonResponse>

    @FormUrlEncoded
    @POST("customer/register")
    suspend fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone_number") phone_number: String,
        @Field("password") password: String,
        @Field("country_id") country_id: Int = 3,
        @Field("city_id") city_id: Int = 11
    ): Response<CommonResponse>
}