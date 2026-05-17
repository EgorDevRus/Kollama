package com.kollama.app.di

import com.kollama.app.domain.usecase.messages.ClearHistoryUseCase
import com.kollama.app.domain.usecase.messages.DeleteMessageUseCase
import com.kollama.app.domain.usecase.messages.GetChatHistoryUseCase
import com.kollama.app.domain.usecase.messages.GetModelsUseCase
import com.kollama.app.domain.usecase.messages.MessagesUseCase
import com.kollama.app.domain.usecase.messages.RegenerateResponseUseCase
import com.kollama.app.domain.usecase.messages.SendMessageUseCase
import org.koin.dsl.module

/**
 * Модуль для сообщений
 * Каждый UseCase создается заново при запросе (factory)
 */
val messagesModule = module {
    factory { ClearHistoryUseCase(get()) }
    factory { GetChatHistoryUseCase(get()) }
    factory { RegenerateResponseUseCase(get()) }
    factory { SendMessageUseCase(get()) }
    factory { GetModelsUseCase(get()) }
    factory { DeleteMessageUseCase(get()) }

    factory { MessagesUseCase(
        get(),
        get(),
        get(),
        get(),
        get(),
        get()
    ) }
}