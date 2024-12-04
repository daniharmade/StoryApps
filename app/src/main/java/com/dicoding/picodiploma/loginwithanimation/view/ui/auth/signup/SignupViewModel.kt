package com.dicoding.picodiploma.loginwithanimation.view.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.SignUpResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {
    val signUpResponse: LiveData<SignUpResponse> = repository.registerResponse

    fun postRegister(name: String, email: String, pass: String) {
        viewModelScope.launch {
            repository.register(name, email, pass)
        }
    }
}