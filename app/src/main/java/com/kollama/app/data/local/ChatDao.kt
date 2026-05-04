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
     * Получает поток сообщений для конкретного чата
     * Список автоматически обновляется при любом изменении в таблице
     */
    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessageFlow(chatId: String): Flow<List<MessageEntity>>

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

    /**
     * Обновление текста для стриминга
     */
    @Query("UPDATE chat_messages SET text = :newText WHERE id = :messageId")
    suspend fun updateMessageText(messageId: String, newText: String)

}