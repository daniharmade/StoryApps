package com.dicoding.picodiploma.loginwithanimation.view.ui

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail

class PaggingData : PagingSource<Int, StoryDetail>() {
    override fun getRefreshKey(state: PagingState<Int, StoryDetail>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryDetail> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapshot(items: List<StoryDetail>): PagingData<StoryDetail> {
            return PagingData.from(items)
        }
    }
}