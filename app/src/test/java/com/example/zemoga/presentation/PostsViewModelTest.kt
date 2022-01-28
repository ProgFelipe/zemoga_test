package com.example.zemoga.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.zemoga.RxTrampolineSchedulerRule
import com.example.zemoga.data.PostRepository
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.domain.PostsUseCase
import com.example.zemoga.presentation.home.viewmodel.PostsViewModel
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostsViewModelTest {

    private lateinit var sut: PostsViewModel

    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var postRepositoryMock: PostRepository

    @Mock
    private lateinit var loadingLiveDataObserverMock: Observer<Boolean>

    @Before
    fun setup() {
        sut = PostsViewModel(PostsUseCase(postRepositoryMock))
    }

    @Test
    fun fetchPost_invoked_updateLiveData() {
        // given
        val expectedList = listOf(Post())
        given(postRepositoryMock.getPostsFromDB()).willReturn(Single.just(emptyList()))
        given(postRepositoryMock.fetchPosts()).willReturn(Single.just(expectedList))
        given(postRepositoryMock.insertAll(anyList())).willReturn(Completable.complete())
        sut.loadingLiveData.observeForever(loadingLiveDataObserverMock)
        // when
        sut.fetchPost()
        // then
        assertEquals(expectedList, sut.postLiveData.value)
        verify(loadingLiveDataObserverMock, times(1)).onChanged(false)
        verify(loadingLiveDataObserverMock, times(1)).onChanged(true)
    }
}