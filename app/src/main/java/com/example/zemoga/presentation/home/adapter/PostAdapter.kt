package com.example.zemoga.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zemoga.R
import com.example.zemoga.common.Constant.EMPTY_STRING
import com.example.zemoga.common.PostViewHolder
import com.example.zemoga.data.network.dto.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter(private val callBack: (selectedPost: Post) -> Unit) :
    ListAdapter<Post, RecyclerView.ViewHolder>(GenericDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(getItem(position)) {
            holder.itemView.constraint_layout_post_container.setOnClickListener {
                callBack(this)
            }
            holder.itemView.text_view_post_message.text = this.body ?: EMPTY_STRING
            holder.itemView.image_view_indicator.isVisible = this.blueDot == true
            holder.itemView.image_view_favorite.isVisible = this.favorite == true
        }
    }
}

class GenericDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.hashCode() == newItem.hashCode()
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}