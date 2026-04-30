package com.kollama.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для взаимодействия с таблицей сообщений в БД
 */

@Dao
interface ChatDao {
    /**
     * Получает список всех сообщений для конкретного чата
     */
    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessageByChatId(chatId: String): Flow<List<MessageEntity>>

    /**
     * Сохраняет новое сообщение в БД
     * Если ID совпадает, сообщение будет перезаписано
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    /**
     * Полностью удаляет историю сообщений конкретного чата
     */
    @Query("DELETE FROM chat_messages WHERE chatId = :chatId")
    suspend fun clearChatHistory(chatId: String)

}