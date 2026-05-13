package com.kollama.app.di

import com.kollama.app.presentation.chat.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        ChatViewModel(
            chatId = "Defualt_chat",
            sendMessageUseCase = get(),
            getChatHistoryUseCase = get(),
            settingsManager = get(),
            getModelsUseCase = get()
        )
    }
}