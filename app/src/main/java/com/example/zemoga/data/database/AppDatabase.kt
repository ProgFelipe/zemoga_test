package com.example.zemoga.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zemoga.data.database.dao.PostDao
import com.example.zemoga.data.network.dto.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    companion object{
        const val DB_NAME = "post_db"
    }
    abstract fun postDao(): PostDao
}