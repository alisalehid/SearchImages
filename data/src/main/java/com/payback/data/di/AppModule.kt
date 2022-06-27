package com.payback.data.di

import android.app.Application
import android.content.Context
import com.payback.data.BuildConfig
import com.payback.data.Constants
import com.payback.data.Constants.CACHE_NAME
import com.payback.data.Constants.IMAGE_TYPE
import com.payback.data.Constants.KEY
import com.payback.data.local.db.LocalDB
import com.payback.data.local.repository.SearchImagesRepository
import com.payback.data.local.repository.SearchImagesRepositoryImpl
import com.payback.data.remote.api.API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor



@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext applicationContext: Context): LocalDB {
        return LocalDB.invoke(applicationContext)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesRepository(
        pixaBayApi: API,
        pixaBayRoomDb: LocalDB,
        @ApplicationContext context: Context
    ): SearchImagesRepository = SearchImagesRepositoryImpl(pixaBayApi, pixaBayRoomDb)


    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit) : API = retrofit.create(API::class.java)

     val apiInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(KEY.first, KEY.second)
            .addQueryParameter(IMAGE_TYPE.first, IMAGE_TYPE.second)
            .build()
        request.url(url)
        chain.proceed(request.build())
    }

    private val cacheInterceptor = Interceptor { chain ->
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(30, TimeUnit.DAYS)
            .build()
        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }

    @Provides
    @Singleton
    fun provideCache(app: Application): Cache {
        return Cache(
            File(app.applicationContext.cacheDir, CACHE_NAME),
            10485760L
        )
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(cacheInterceptor)
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }
}