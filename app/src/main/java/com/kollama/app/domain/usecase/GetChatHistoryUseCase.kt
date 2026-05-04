package com.kollama.app.domain.usecase

import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

/**
 * История чатов
 */

class GetChatHistoryUseCase (private val repository: ChatRepository){
    /**
     * @param chatId Уникальный идентификатор чата
     * @return Поток со списком всех сообщений, отсортированных по времени
     */
    operator fun invoke(chatId: String) : Flow<List<ChatMessage>> {
        return repository.getChatHistoryFlow(chatId)
    }
}
