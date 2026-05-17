package com.kollama.app.domain.usecase.messages

import com.kollama.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Перегенерация запроса
 *
 * @see MessageRepository
 */
class RegenerateResponseUseCase (private val repository: MessageRepository){
    /**
     * Запускает поток получения текста
     * @param chatId Уникальный идентификатор чата
     * @param prompt Текст вопроса пользователя
     * @param ip  Адрес сервера Ollama
     * @param model Название модели
     * @return Поток строк, ответ от нейросети
     */
    operator fun invoke(
        chatId: String,
        prompt: String,
        ip: String,
        model: String
    ): Flow<String> {
        return repository.getLiveResponse(
            chatId,
            prompt,
            ip,
            model
        )
    }
}