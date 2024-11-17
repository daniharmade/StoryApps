package com.dicoding.picodiploma.loginwithanimation.data.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreferences
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.auth.AuthConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AuthInjection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val userPreference = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getLoginSession().first() }
        val authApiService = AuthConfig.getApiService(user.token)
        return AuthRepository(authApiService, userPreference)
    }
}