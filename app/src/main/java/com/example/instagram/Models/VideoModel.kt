package com.example.instagram.Models
data class VideoModel(
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val thumbnail: String = "",
    var isWatched: Boolean = false
)
