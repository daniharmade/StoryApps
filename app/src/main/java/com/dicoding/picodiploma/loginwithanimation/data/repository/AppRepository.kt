package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreferences
import com.dicoding.picodiploma.loginwithanimation.data.response.PostResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.story.StoryService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AppRepository(
    private val storyService: StoryService,
    private val userPreference: UserPreferences
) {

    suspend fun getStoriesWithLocation(): Result<StoryResponse> {
        return try {
            val response = storyService.getStoryListLocation(1)
            if (!response.error) {
                Result.DataSuccess(response)
            } else {
                Result.DataError(response.message)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PostResponse::class.java)
            Result.DataError(errorResponse.message)
        } catch (e: Exception) {
            Result.DataError("Can't get location")
        }
    }

    fun getStoryPagingSource(): LiveData<PagingData<StoryDetail>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyService)
            }
        ).liveData
    }

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(Result.DataLoading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = storyService.uploadStory(multipartBody, requestBody)
            emit(Result.DataSuccess(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PostResponse::class.java)
            emit(Result.DataError(errorResponse.message))
        }
    }

    companion object {
        fun getInstance(
            apiService: StoryService,
            userPreference: UserPreferences
        ) = AppRepository(apiService, userPreference)
    }
}