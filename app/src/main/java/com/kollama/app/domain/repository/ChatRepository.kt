package com.kollama.app.domain.repository

import com.kollama.app.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для управления данными чата
 */
interface ChatRepository {
    /** Получает поток всех сообщений, отсортированных по дате */
    fun getChatHistory() : Flow<List<ChatMessage>>
    /** Сохраняет новое сообщение пользователя в базу данных */
    suspend fun sendMessage(text: String)
    fun getLiveResponse(prompt: String): Flow<String>
    suspend fun clearHistory()
}
