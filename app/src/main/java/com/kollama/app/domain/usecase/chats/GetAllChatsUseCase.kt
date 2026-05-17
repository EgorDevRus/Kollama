package com.kollama.app.domain.usecase.chats

import com.kollama.app.domain.model.Chat
import com.kollama.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow


/**
 * Поулчение всех чатов
 *
 * @see ChatRepository
 */
class GetAllChatsUseCase(private val repository: ChatRepository) {
    operator fun invoke(): Flow<List<Chat>> {
        return repository.getAllChats()
    }
}