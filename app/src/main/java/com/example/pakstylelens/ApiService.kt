package com.example.pakstylelens

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @Multipart
    @POST("/search") // Replace with actual endpoint
    fun sendSearchRequest(
        @Part("caption") caption: RequestBody?,
        @Part("user_id") userId: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<SearchResponse>
}
