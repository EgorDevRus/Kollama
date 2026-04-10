package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
/**
 * Перегенерация запроса
 */
class RegenerateResponseUseCase (private val repository: ChatRepository){
    /**
     * Запускает поток получения текста
     * @param chatId Уникальный идентификатор чата
     * @param prompt Текст вопроса пользователя
     * @return Поток строк, ответ от нейросети
     */
    operator fun invoke(chatId: String, prompt: String): Flow<String> {
        return repository.getLiveResponse(chatId, prompt)
    }
}