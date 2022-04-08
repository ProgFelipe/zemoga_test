package com.example.zemoga.domain

import com.example.zemoga.data.PostRepository
import com.example.zemoga.data.network.dto.Comments
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.data.network.dto.User
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

open class PostsUseCase @Inject constructor(private val postsRepository: PostRepository) {

    companion object {
        private const val TWENTY = 20
    }

    fun getPosts(loadFromService: Boolean) = postsRepository.getPostsFromDB().flatMap {
        if (it.isEmpty() || loadFromService) {
            val posts = postsRepository.fetchPosts().blockingGet()
            posts.setFirstTwentyItemsDot()
            val result = postsRepository.insertAll(posts).blockingGet()
            if (result is Throwable) {
                Single.error(result)
            } else {
                Single.just(posts)
            }
        } else {
            Single.just(it)
        }
    }

    fun deleteAll(): Completable = postsRepository.deleteAll()

    fun insertAll(posts: List<Post>): Completable = postsRepository.insertAll(posts)

    fun savePost(post: Post): Completable = postsRepository.savePost(post)

    fun fetchComments(postId: Int): Single<List<Comments>> = postsRepository.fetchComments(postId)

    fun fetchUser(userId: Int): Single<User> = postsRepository.fetchUser(userId)

    private fun List<Post>.setFirstTwentyItemsDot() {
        this.mapIndexed { index, post ->
            if (index < TWENTY) {
                post.blueDot = true
            } else {
                return
            }
        }
    }
}
