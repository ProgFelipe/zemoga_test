package com.example.zemoga.data.network.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.zemoga.common.Constant.EMPTY_STRING

@Entity
data class Post(
    @ColumnInfo(name = "user_id")
    val userId: Int? = 0,
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo
    val title: String? = EMPTY_STRING,
    @ColumnInfo
    val body: String? = EMPTY_STRING,
    @ColumnInfo(name = "blue_dot")
    var blueDot: Boolean = false,
    @ColumnInfo
    var favorite: Boolean = false
)
