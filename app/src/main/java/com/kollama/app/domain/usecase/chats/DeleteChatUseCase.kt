package com.kollama.app.domain.usecase.chats

import com.kollama.app.domain.repository.ChatRepository

/**
 * Удаление чата
 *
 * @see ChatRepository
 */
class DeleteChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String) {
        repository.deleteChat(chatId)
    }
}