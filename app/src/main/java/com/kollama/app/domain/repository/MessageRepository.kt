package com.kollama.app.domain.repository

import com.kollama.app.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    /** Получает поток всех сообщений, отсортированных по дате */
    fun getChatHistoryFlow(chatId: String) : Flow<List<ChatMessage>>

    /** Сохраняет новое сообщение пользователя в базу данных */
    suspend fun sendMessage(chatId: String, text: String)

    /** Запрашивает поток ответа у нейросети (стриминг) */
    fun getLiveResponse(
        chatId: String,
        prompt: String,
        ip: String,
        model: String
    ): Flow<String>

    /** Удаляет историю чата */
    suspend fun clearHistory(chatId: String)

    /** Получаем список моделей от Ollama */
    suspend fun getModels(ip: String): List<String>

    /** Удаление собщения в чате */
    suspend fun deleteMessage(id: String)
}