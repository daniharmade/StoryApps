package com.dicoding.picodiploma.loginwithanimation.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<StoryDetail>>()
    val stories: LiveData<List<StoryDetail>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getLoginSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}