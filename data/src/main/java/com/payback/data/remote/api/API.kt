package com.payback.data.remote.api


import com.payback.data.remote.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") search:String,
        @Query("per_page") per_page: Int? = null,
        @Query("page") page: Int? = null,
        ): ImageResponse
}