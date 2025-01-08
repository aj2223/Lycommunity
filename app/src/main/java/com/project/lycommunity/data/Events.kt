package com.project.lycommunity.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Events(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(), // Long type for timestamp
    val likes: Int = 0,
    val dislikes: Int = 0
) : Parcelable
