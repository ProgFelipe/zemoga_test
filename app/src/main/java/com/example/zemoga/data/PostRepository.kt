package com.example.zemoga.data

import com.example.zemoga.data.network.dto.Comments
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.data.network.dto.User
import io.reactivex.Completable
import io.reactivex.Single

interface PostRepository {

    fun getPostsFromDB(): Single<List<Post>>

    fun fetchPosts() : Single<List<Post>>

    fun deletePost(post: Post): Single<Int>

    fun deleteAll(): Completable

    fun insertAll(posts: List<Post>) : Completable

    fun savePost(post: Post) : Completable

    fun fetchComments(postId: Int): Single<List<Comments>>

    fun fetchUser(userId: Int): Single<User>
}