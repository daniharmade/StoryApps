package com.dicoding.picodiploma.loginwithanimation.view.ui.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail

class HomeViewModel(private val repository: AppRepository) : ViewModel() {
    val stories: LiveData<PagingData<StoryDetail>> =
        repository.getStoryPagingSource().cachedIn(viewModelScope)
}