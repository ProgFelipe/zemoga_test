package com.example.zemoga.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zemoga.R
import com.example.zemoga.common.CommentsViewHolder
import com.example.zemoga.common.Constant.EMPTY_STRING
import com.example.zemoga.data.network.dto.Comments
import kotlinx.android.synthetic.main.comments_item.view.*

class CommentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _data: ArrayList<Comments> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text_view_comment.text = (_data[position]).body ?: EMPTY_STRING
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Comments>) {
        this._data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = _data.size
}
