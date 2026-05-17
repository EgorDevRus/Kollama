package com.kollama.app.domain.repository

import com.kollama.app.domain.model.Chat
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для управления данными чата
 */
interface ChatRepository {

    /** Создание чата */
    suspend fun createChat(chatId: String, name: String)

    /** Удаление чата */
    suspend fun deleteChat(chatId: String)

    /** Обновление наименования чата */
    suspend fun updateChatName(chatId: String, newName: String)

    /** получение всех чатов*/
    fun getAllChats(): Flow<List<Chat>>

}
