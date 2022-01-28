package com.example.zemoga.di

import com.example.zemoga.BuildConfig.BASE_URL
import com.example.zemoga.BuildConfig.DEBUG
import com.example.zemoga.data.network.PostsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val TIME_LIMIT = 30000L
    }

    @Provides
    @Singleton
    fun rickAndMortyApi(retrofit: Retrofit): PostsApi = retrofit.create(PostsApi::class.java)

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIME_LIMIT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_LIMIT, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_LIMIT, TimeUnit.MILLISECONDS)
        
        if (DEBUG) {
            val httpLoginInterceptor = HttpLoggingInterceptor()
            httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(httpLoginInterceptor)
        }

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.createWithScheduler(
                Schedulers.io()
            )
        )
        .client(okHttpClient)
        .build()

}