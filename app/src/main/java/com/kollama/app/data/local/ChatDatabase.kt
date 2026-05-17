package com.kollama.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Объединяет таблицы сообщений и чатов и методы доступа к ним
 *
 * @see MessageEntity
 * @see ChatEntity
 *
 * @see ChatDao
 */
@Database(
    entities = [
        MessageEntity::class,
        ChatEntity::class
               ],
    version = 2,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}