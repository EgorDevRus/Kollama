package com.kollama.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Описание сущности таблицы в БД
 *
 * @property dbId Идентификатор для Room (автогенерация), который будет в БД
 * @see com.kollama.app.domain.model.ChatMessage Остальные параметры
 */
@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class MessageEntity(

    @PrimaryKey(autoGenerate = true)
    val dbId : Long = 0,
    val id: String,
    val chatId: String,
    val text: String,
    val role: String,
    val timestamp: Long
)