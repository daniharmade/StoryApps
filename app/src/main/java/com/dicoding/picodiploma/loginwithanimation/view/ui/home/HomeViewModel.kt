package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.Result
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<StoryDetail>>()
    val stories: LiveData<List<StoryDetail>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadStories() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getStories()

                if (response is Result.DataSuccess) {
                    val storyResponse = response.data
                    _stories.value = storyResponse.listStory.orEmpty()
                } else if (response is Result.DataError) {
                    _errorMessage.value = response.error
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}