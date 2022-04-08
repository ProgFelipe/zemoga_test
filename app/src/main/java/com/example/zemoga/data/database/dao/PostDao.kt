package com.example.zemoga.data.database.dao

import androidx.room.*
import com.example.zemoga.data.network.dto.Post
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getAll(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post): Completable

    @Delete
    fun delete(post: Post): Single<Int>

    @Query("DELETE FROM post")
    fun dropTable(): Completable
}
