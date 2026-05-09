package com.kollama.app.di

import androidx.room.Room
import com.kollama.app.data.local.ChatDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Модуль для работы с БД Room
 */
val databaseModule = module {
    // Создаем экземпляр БД (single)
    single {
        Room.databaseBuilder(
            androidContext(),
            ChatDatabase::class.java,
            "kollama_database"
        ).build()
    }

    /** Предоставляем DAO для работы с сообщениями
     * Чтобы не писать в репозитории .ChatDao()
     */
    single { get<ChatDatabase>().chatDao() }
}
