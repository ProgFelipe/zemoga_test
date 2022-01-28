package com.example.zemoga.presentation.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zemoga.data.network.dto.Comments
import com.example.zemoga.data.network.dto.Post
import com.example.zemoga.data.network.dto.User
import com.example.zemoga.domain.PostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsUseCase: PostsUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val observeOnScheduler by lazy { AndroidSchedulers.mainThread() }
    private val subscribeOnScheduler by lazy { Schedulers.io() }

    private val _postLiveData = MutableLiveData<MutableList<Post>>()
    val postLiveData get() = _postLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData get() = _loadingLiveData

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData get() = _userLiveData

    private val _commentsLiveData = MutableLiveData<List<Comments>>()
    val commentsLiveData get() = _commentsLiveData

    private var _selectedPost = Post()
    val selectedPost: Post
        get() = _selectedPost

    // region posts home
    fun init() {
        if (_postLiveData.value == null) {
            getPosts(false)
        }
    }

    fun fetchPost() {
        getPosts(true)
    }

    private fun getPosts(loadFromService: Boolean) {
        _loadingLiveData.value = true
        compositeDisposable.add(
            postsUseCase.getPosts(loadFromService).subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe(
                    {
                        _loadingLiveData.value = false
                        _postLiveData.value = it.toMutableList()
                    }, {
                        _loadingLiveData.value = false
                    }
                )
        )
    }

    fun deleteAllPosts() {
        compositeDisposable.add(
            postsUseCase.deleteAll().subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe({
                    _postLiveData.value = mutableListOf()
                }, {
                    //Todo handle error
                })
        )
    }

    private fun updatePosts(posts: List<Post>, onSuccess: () -> Unit) {
        compositeDisposable.add(
            postsUseCase.insertAll(posts)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe({
                    onSuccess()
                }, {
                    //Todo handle error
                })
        )
    }

    fun updateList(posts: MutableList<Post>) {
        updatePosts(posts) { _postLiveData.value = posts }
    }

    fun deleteFavoritePost(deletedPosition: Int) {
        val post = getFavorites().toMutableList()[deletedPosition]
        val currentList = _postLiveData.value?.toMutableList()
        currentList?.remove(post)
        currentList?.let {
            updatePosts(currentList) { _postLiveData.value = currentList }
        }
    }
    // endregion

    // region favorites post a home
    fun getFavorites(): List<Post> {
        return postLiveData.value?.filter { it.favorite }?.toList() ?: mutableListOf()
    }
    // endregion

    // region post detail
    fun setupDetailPostView(postId: Int) {
        _postLiveData.value?.firstOrNull { it.id == postId }?.let {
            _selectedPost = it
            setDotIndicatorToFalse()
            _selectedPost.userId?.let { userId -> getUser(userId) }
            getSelectedPostComments(_selectedPost.id)
        }
    }

    private fun savePost(post: Post) {
        compositeDisposable.add(
            postsUseCase.savePost(post).subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe({}, {
                    it.message
                })
        )
    }

    private fun setDotIndicatorToFalse() {
        _selectedPost.blueDot = false
        savePost(_selectedPost)
    }

    fun updatePostFavoriteStatus() {
        _selectedPost.favorite = _selectedPost.favorite.not()
        savePost(_selectedPost)
    }

    fun getStarCurrentFavoriteStatus() = _selectedPost.favorite

    private fun getUser(userId: Int) {
        _loadingLiveData.value = true
        compositeDisposable.add(
            postsUseCase.fetchUser(userId).subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe(
                    {
                        _loadingLiveData.value = false
                        _userLiveData.value = it
                    }, {
                        _loadingLiveData.value = false
                        //Todo handle error
                    }
                )
        )
    }

    private fun getSelectedPostComments(postId: Int) {
        _loadingLiveData.value = true
        compositeDisposable.add(
            postsUseCase.fetchComments(postId).subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler).subscribe(
                    {
                        _loadingLiveData.value = false
                        _commentsLiveData.value = it
                    }, {
                        _loadingLiveData.value = false
                        //Todo handle error
                    }
                )
        )
    }
    // endregion

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}