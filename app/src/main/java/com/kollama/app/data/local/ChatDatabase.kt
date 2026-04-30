package com.kollama.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Объединяет сущность таблицы сообщений и методы доступа к ней
 */
@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase(){
    abstract fun chatDao(): ChatDao
}