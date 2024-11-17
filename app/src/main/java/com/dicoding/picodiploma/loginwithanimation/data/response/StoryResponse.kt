package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<StoryDetail> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)