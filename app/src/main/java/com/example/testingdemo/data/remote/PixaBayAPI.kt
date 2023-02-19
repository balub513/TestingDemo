package com.example.testingdemo.data.remote

import com.example.testingdemo.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaBayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") key: String = BuildConfig.API_KEY_PIXBAY
    ): Response<ImageResponse>
}