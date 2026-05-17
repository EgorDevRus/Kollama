package com.kollama.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность таблицы чатов в БД
 *
 * @property id Уникальный номер чата
 * @property name Название чата
 * @property timestamp Время последнего сообщения для сортировки
 */
@Entity("Chats")
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val timestamp: Long
)
