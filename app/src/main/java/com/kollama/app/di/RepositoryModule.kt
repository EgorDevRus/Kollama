package com.kollama.app.di

import com.kollama.app.data.repository.ChatRepositoryImpl
import com.kollama.app.domain.repository.ChatRepository
import com.kollama.app.domain.repository.MessageRepository
import org.koin.dsl.module

/**
 * Модуль для связи интерфейсов репозиториев с их реализацией
 */
val repositoryModule = module {
    single { ChatRepositoryImpl(get(), get() ) }

    single<ChatRepository> { get<ChatRepositoryImpl> () }
    single<MessageRepository> { get<ChatRepositoryImpl> () }
}
