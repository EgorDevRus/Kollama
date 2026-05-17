package com.kollama.app.domain.usecase.chats

/** Все use case чатов */
data class ChatsUseCase(
    val createChat: CreateChatUseCase,
    val deleteChat: DeleteChatUseCase,
    val getAllChats: GetAllChatsUseCase,
    val updateChatName: UpdateChatNameUseCase
)
