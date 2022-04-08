package com.example.zemoga.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.zemoga.R
import com.example.zemoga.presentation.home.adapter.ViewPagerAdapter
import com.example.zemoga.presentation.home.viewmodel.PostsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: PostsViewModel by activityViewModels()

    private val homeItems by lazy {
        listOf(
            getString(R.string.all),
            getString(R.string.favorites)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        addListeners()
    }

    private fun addListeners() {
        image_view_reload_posts.setOnClickListener {
            viewModel.fetchPost()
        }
    }

    private fun setupViewPager() {
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = homeItems[position]
        }.attach()
    }
}
