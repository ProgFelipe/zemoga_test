package com.example.zemoga.domain

import com.example.zemoga.data.PostRepository
import com.example.zemoga.data.network.dto.Post
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.anyList
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostsUseCaseTest {

    lateinit var sut: PostsUseCase

    @Mock
    lateinit var postRepositoryMock: PostRepository

    @Before
    fun setup() {
        sut = PostsUseCase(postRepositoryMock)
    }

    @Test
    fun getPosts_invokedWithNoListOnDb_returnListWithFirstTwentyItemsDotAsTrue() {
        // given
        val listSize = 21
        given(postRepositoryMock.getPostsFromDB()).willReturn(Single.just(emptyList()))
        given(postRepositoryMock.fetchPosts()).willReturn(Single.just(List(listSize) { Post().copy() }))
        given(postRepositoryMock.insertAll(anyList())).willReturn(Completable.complete())
        // when
        val result = sut.getPosts(false).blockingGet()
        // then
        assertEquals(listSize, result.size)
        result.forEachIndexed { index, post ->
            if (index < 20) {
                assertTrue(post.blueDot)
            } else {
                assertFalse(post.blueDot)
            }
        }
    }
}