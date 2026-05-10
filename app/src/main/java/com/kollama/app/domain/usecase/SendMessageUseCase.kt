package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Отправка запроса нейросети и ответ от нее в реальном времени
 */

class SendMessageUseCase (private val repository: ChatRepository) {
    /**
     * @param chatId Уникальный идентификатор чата
     * @param text Текст сообщения от пользователя
     * @param ip  Адрес сервера Ollama
     * @param model Название модели
     * @return Поток строк (стриминг) с ответом от Ollama
     */
    operator fun invoke(
        chatId: String,
        text: String,
        ip: String,
        model: String
    ): Flow<String> = flow {
        repository.sendMessage(chatId, text)

        // Вывод пока текст от нейронки не закончится
        emitAll(repository.getLiveResponse(
            chatId,
            text,
            ip = ip,
            model = model
        ))
    }
}