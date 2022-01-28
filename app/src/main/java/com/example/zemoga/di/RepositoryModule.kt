package com.example.zemoga.di

import android.content.Context
import androidx.room.Room
import com.example.zemoga.data.database.AppDatabase
import com.example.zemoga.data.PostRepository
import com.example.zemoga.data.PostRepositoryImpl
import com.example.zemoga.data.network.PostsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room
            .databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun providesPostRepository(postsApi: PostsApi, appDatabase: AppDatabase): PostRepository = PostRepositoryImpl(postsApi, appDatabase)
}