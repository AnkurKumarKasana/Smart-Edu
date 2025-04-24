package com.example.instagram

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: String
)
