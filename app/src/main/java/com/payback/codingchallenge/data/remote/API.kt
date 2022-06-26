package com.payback.codingchallenge.data.remote

import com.payback.codingchallenge.BuildConfig
import com.payback.codingchallenge.data.remote.model.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("/api")
    suspend fun searchForImage(
        @Query("q") searchQuery:String,
        @Query("key") apiKey: String = BuildConfig.API_KEY

    ): Response<ImageResponse>
}