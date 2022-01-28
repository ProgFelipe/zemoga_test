package com.example.zemoga.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zemoga.R
import com.example.zemoga.presentation.home.adapter.CommentsAdapter
import com.example.zemoga.presentation.home.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_detail.*

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val viewModel: PostsViewModel by activityViewModels()
    private val safeArgs by navArgs<PostDetailFragmentArgs>()

    private lateinit var adapter: CommentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.setupDetailPostView(safeArgs.postId)
        updateFavoriteDrawable()
        setListeners()
    }

    private fun setListeners() {
        image_view_back.setOnClickListener {
            activity?.onBackPressed()
        }
        image_view_favorite.setOnClickListener {
            viewModel.updatePostFavoriteStatus()
            updateFavoriteDrawable()
        }
    }

    private fun updateFavoriteDrawable() {
        image_view_favorite.setImageResource(if (viewModel.getStarCurrentFavoriteStatus()) R.drawable.ic_star else R.drawable.ic_empty_star)
    }

    private fun setupRecyclerView() {
        adapter = CommentsAdapter()
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view_comments.layoutManager = linearLayoutManager
        recycler_view_comments.adapter = adapter
        recycler_view_comments.addItemDecoration(
            DividerItemDecoration(
                recycler_view_comments.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner, {
            text_view_description.text = viewModel.selectedPost.body
            text_view_name.text = getString(R.string.name, it.name)
            text_view_email.text = getString(R.string.email, it.email)
            text_view_phone.text = getString(R.string.phone, it.phone)
            text_view_website.text = getString(R.string.website, it.website)
        })
        viewModel.commentsLiveData.observe(viewLifecycleOwner, {
            (recycler_view_comments.adapter as CommentsAdapter).updateData(it)
        })
        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            progress_bar.isVisible = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unsubscribe()
    }
}