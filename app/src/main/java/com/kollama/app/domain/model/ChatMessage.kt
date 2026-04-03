package com.kollama.app.domain.model

data class ChatMessage(
    val id: String,
    val text: String,
    val role: MessageRole,
    val timestamp: Long

)
