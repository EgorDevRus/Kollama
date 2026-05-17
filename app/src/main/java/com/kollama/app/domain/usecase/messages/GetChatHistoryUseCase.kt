package com.kollama.app.domain.usecase.messages

import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

/**
 * История чатов
 *
 * @see MessageRepository
 */

class GetChatHistoryUseCase (private val repository: MessageRepository){
    /**
     * @param chatId Уникальный идентификатор чата
     * @return Поток со списком всех сообщений, отсортированных по времени
     */
    operator fun invoke(chatId: String) : Flow<List<ChatMessage>> {
        return repository.getChatHistoryFlow(chatId)
    }
}
