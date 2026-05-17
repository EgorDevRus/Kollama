package com.kollama.app.domain.usecase.messages

/** Хранит в себе все use case сообщений */
data class MessagesUseCase(
    val clearHistory: ClearHistoryUseCase,
    val deleteMessage: DeleteMessageUseCase,
    val getChatHistory: GetChatHistoryUseCase,
    val regenerateResponse: RegenerateResponseUseCase,
    val sendMessage: SendMessageUseCase,
    val getModelsUseCase: GetModelsUseCase
)
