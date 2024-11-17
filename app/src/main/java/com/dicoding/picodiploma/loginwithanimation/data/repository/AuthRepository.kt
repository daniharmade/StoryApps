package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreferences
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.PostResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.SignUpResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.auth.AuthService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository(
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) {
    private val _registerResponse = MutableLiveData<SignUpResponse>()
    val registerResponse: LiveData<SignUpResponse> = _registerResponse
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<SignUpResponse> {
        return try {
            val response = authService.postSignUp(name, email, password)
            if (!response.error) {
                Result.DataSuccess(response)
            } else {
                Result.DataError(response.message)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PostResponse::class.java)
            Result.DataError(errorResponse.message)
        } catch (e: Exception) {
            Result.DataError("Registration failed")
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.postLogin(email, password)
            if (!response.error) {
                Result.DataSuccess(response)
            } else {
                Result.DataError(response.message)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PostResponse::class.java)
            Result.DataError(errorResponse.message)
        } catch (e: Exception) {
            Result.DataError("Login failed")
        }
    }

    suspend fun saveLoginSession(user: UserModel) {
        userPreferences.saveLoginSession(user)
    }

    fun getLoginSession(): Flow<UserModel> {
        return userPreferences.getLoginSession()
    }

    suspend fun logout() {
        userPreferences.logoutSession()
    }

    companion object {
        fun getInstance(
            authApiService: AuthService,
            userPreference: UserPreferences
        ) = AuthRepository(authApiService, userPreference)
    }
}