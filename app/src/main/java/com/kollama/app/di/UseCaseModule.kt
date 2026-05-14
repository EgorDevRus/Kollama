package com.kollama.app.di

import com.kollama.app.domain.usecase.ClearHistoryUseCase
import com.kollama.app.domain.usecase.DeleteMessageUseCase
import com.kollama.app.domain.usecase.GetChatHistoryUseCase
import com.kollama.app.domain.usecase.GetModelsUseCase
import com.kollama.app.domain.usecase.RegenerateResponseUseCase
import com.kollama.app.domain.usecase.SendMessageUseCase
import org.koin.dsl.module

/**
 * Модуль для сценариев использования (UseCase)
 * Каждый UseCase создается заново при запросе (factory)
 */
val useCaseModule = module {
    factory { ClearHistoryUseCase(get()) }
    factory { GetChatHistoryUseCase(get()) }
    factory { RegenerateResponseUseCase(get()) }
    factory { SendMessageUseCase(get()) }
    factory { GetModelsUseCase(get()) }
    factory { DeleteMessageUseCase(get()) }
}