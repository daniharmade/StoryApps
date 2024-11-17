package com.dicoding.picodiploma.loginwithanimation.data.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreferences
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.story.StoryConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AppInjection {
    fun provideRepository(context: Context): AppRepository {
        val userPreference = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getLoginSession().first() }
        val apiService = StoryConfig.getApiService(user.token)


        return AppRepository(apiService, userPreference)
    }
}