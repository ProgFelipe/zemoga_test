package com.example.zemoga.data.network

import com.example.zemoga.data.network.dto.Comments
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.data.network.dto.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {

    @GET("posts")
    fun fetchPost(): Single<List<Post>>

    @GET("comments")
    fun fetchCommentsByPostId(@Query("postId") postId: Int): Single<List<Comments>>

    @GET("users/{userId}")
    fun fetchUserById(@Path("userId") userId: Int): Single<User>
}