package com.dicoding.picodiploma.loginwithanimation.data.retrofit.auth

import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}