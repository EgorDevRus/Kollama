package com.kollama.app.di

import com.kollama.app.presentation.chat.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { (chatId: String) ->
        ChatViewModel(
            messagesUseCase = get(),
            settingsManager = get(),
            chatsUseCase = get()

        )
    }
}