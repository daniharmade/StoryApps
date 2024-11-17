package com.dicoding.picodiploma.loginwithanimation.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.di.AppInjection
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import com.dicoding.picodiploma.loginwithanimation.view.ui.home.HomeViewModel
import com.dicoding.picodiploma.loginwithanimation.view.ui.upload.UploadViewModel

class StoryViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = StoryViewModelFactory(AppInjection.provideRepository(context))
    }
}
