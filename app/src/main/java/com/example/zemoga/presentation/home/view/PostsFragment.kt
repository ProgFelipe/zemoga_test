package com.example.zemoga.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zemoga.R
import com.example.zemoga.presentation.home.adapter.PostAdapter
import com.example.zemoga.presentation.home.adapter.SwipeToDeleteCallback
import com.example.zemoga.presentation.home.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_posts.*

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val viewModel: PostsViewModel by activityViewModels()

    private lateinit var adapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        addListeners()
        viewModel.init()
    }

    private fun addListeners() {
        fab.setOnClickListener {
            viewModel.deleteAllPosts()
        }
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(it.id))
        }
        recycler_view_posts.addItemDecoration(
            DividerItemDecoration(
                recycler_view_posts.context,
                DividerItemDecoration.VERTICAL
            )
        )

        //Swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(this.requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentList =  adapter.currentList.toMutableList()
                currentList.removeAt(viewHolder.adapterPosition)
                viewModel.updateList(currentList)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler_view_posts)

        recycler_view_posts.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view_posts.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.postLiveData.observe(viewLifecycleOwner, {
            (recycler_view_posts.adapter as PostAdapter).submitList(it)
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