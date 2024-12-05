package com.dicoding.picodiploma.loginwithanimation.data.pref

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.story.StoryService

class StoryPagingSource(private val apiService: StoryService) : PagingSource<Int, StoryDetail>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryDetail> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoryList(position, params.loadSize).listStory

            Log.d("StoryPagingSource", "Data dari API: $responseData")

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Kesalahan: ${exception.message}", exception)
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}