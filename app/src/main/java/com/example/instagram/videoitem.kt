package com.example.instagram

data class VideoItem(
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val thumbnail: String = "" ,
    var isWatched: Boolean = false
)
