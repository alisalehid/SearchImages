package com.payback.codingchallenge.di

import com.payback.codingchallenge.Constants.BASE_URL
import com.payback.codingchallenge.data.remote.API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule  {


    @Singleton
    @Provides
    fun provideApi(): API {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(API::class.java )
    }
}