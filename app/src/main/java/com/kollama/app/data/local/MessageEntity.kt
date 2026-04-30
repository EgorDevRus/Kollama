package com.kollama.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
     * Описание сущности таблицы в БД
 */
@Entity(tableName = "chat_messages")
data class MessageEntity(
    /** Идентификатор для Room (автогенерация), который будет в БД */
    @PrimaryKey(autoGenerate = true)
    val dbId : Long = 0,

    /**
     * @see com.kollama.app.domain.model.ChatMessage - остальные параметры
     */
    val id: String,
    val chatId: String,
    val text: String,
    val role: String,
    val timestamp: Long
)