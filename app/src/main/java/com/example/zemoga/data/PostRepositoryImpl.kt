package com.example.zemoga.data

import com.example.zemoga.data.database.AppDatabase
import com.example.zemoga.data.network.PostsApi
import com.example.zemoga.data.network.dto.Comments
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.data.network.dto.User
import io.reactivex.Single

class PostRepositoryImpl(private val postsApi: PostsApi, appDatabase: AppDatabase) : PostRepository {

    private val postDao = appDatabase.postDao()

    override fun getPostsFromDB(): Single<List<Post>> = postDao.getAll()

    override fun fetchPosts(): Single<List<Post>> = postsApi.fetchPost()

    override fun insertAll(posts: List<Post>) = postDao.insertAll(posts)

    override fun deletePost(post: Post) = postDao.delete(post)

    override fun savePost(post: Post) = postDao.insert(post)

    override fun deleteAll() = postDao.dropTable()

    override fun fetchUser(userId: Int): Single<User> = postsApi.fetchUserById(userId)

    override fun fetchComments(postId: Int): Single<List<Comments>> = postsApi.fetchCommentsByPostId(postId)
}