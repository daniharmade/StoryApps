package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class StoryDetail(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Any,

    @field:SerializedName("lon")
    val lon: Any,

    )