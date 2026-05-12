package com.kollama.app.di

import com.kollama.app.data.repository.ChatRepositoryImpl
import com.kollama.app.domain.repository.ChatRepository
import org.koin.dsl.module

/**
 * Модуль для связи интерфейсов репозиториев с их реализацией
 */
val repositoryModule = module {
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
}
