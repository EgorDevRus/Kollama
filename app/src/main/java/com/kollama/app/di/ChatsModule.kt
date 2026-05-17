package com.kollama.app.di

import com.kollama.app.domain.usecase.chats.ChatsUseCase
import com.kollama.app.domain.usecase.chats.CreateChatUseCase
import com.kollama.app.domain.usecase.chats.DeleteChatUseCase
import com.kollama.app.domain.usecase.chats.GetAllChatsUseCase
import com.kollama.app.domain.usecase.chats.UpdateChatNameUseCase
import org.koin.dsl.module

/**
 * Модуль для чатов
 * Каждый UseCase создается заново при запросе (factory)
 */
val chatsModule = module {
    factory { CreateChatUseCase(get()) }
    factory { DeleteChatUseCase(get()) }
    factory { GetAllChatsUseCase(get()) }
    factory { UpdateChatNameUseCase(get()) }

    factory { ChatsUseCase(
        get(),
        get(),
        get(),
        get())
    }
}