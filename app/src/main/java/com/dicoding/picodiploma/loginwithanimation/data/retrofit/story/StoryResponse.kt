package com.dicoding.picodiploma.loginwithanimation.data.retrofit.story

import com.dicoding.picodiploma.loginwithanimation.data.response.PostResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StoryService {
    @GET("stories")
    suspend fun getStoryList(
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostResponse

}