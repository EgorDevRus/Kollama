package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

/**
 * Отправка запроса нейросети и ответ от нее в реальном времени
 */

class SendMessageUseCase (private val repository: ChatRepository) {
    /**
     * @param chatId Уникальный идентификатор чата
     * @param text Текст сообщения от пользователя
     * @return Поток строк (стриминг) с ответом от Ollama
     */
    suspend operator fun invoke(chatId: String, text: String): Flow<String> {
        repository.sendMessage(chatId, text)
        return repository.getLiveResponse(chatId,text)
    }
}