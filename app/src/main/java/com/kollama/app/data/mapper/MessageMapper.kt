package com.kollama.app.data.mapper

import com.kollama.app.data.local.MessageEntity
import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.model.MessageRole

/**
 * Превращает модель из слоя Data в слой Domain
 */
fun MessageEntity.toDomain() = ChatMessage(
    id = id,
    chatId = chatId,
    text = text,
    role = MessageRole.valueOf(role), // Превращает строку в Enum
    timestamp = timestamp
)

/**
 * Превращает модель из слоя Domain в слой Data
 */
fun ChatMessage.toEntity() = MessageEntity(
    id = id,
    chatId = chatId,
    text = text,
    role = role.name, // Превращает Enum в строку
    timestamp = timestamp
)
