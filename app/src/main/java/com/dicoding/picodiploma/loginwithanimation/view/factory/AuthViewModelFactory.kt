package com.dicoding.picodiploma.loginwithanimation.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.di.AuthInjection
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.view.ui.login.LoginViewModel
import com.dicoding.picodiploma.loginwithanimation.view.ui.main.MainViewModel
import com.dicoding.picodiploma.loginwithanimation.view.ui.signup.SignUpViewModel

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context) = AuthViewModelFactory(
            AuthInjection.provideAuthRepository(context)
        )
    }
}