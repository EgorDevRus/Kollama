package com.kollama.app.domain.usecase.messages

import com.kollama.app.domain.repository.MessageRepository

/**
 * Удаление чата со всеми сообщениями
 *
 * @see MessageRepository
 */
class ClearHistoryUseCase (private val repository: MessageRepository){
    /**
     * Удаляет запись чата из локальной базы данных
     * @param chatId Уникальный идентификатор чата
     */
    suspend operator fun invoke(chatId: String) {
        repository.clearHistory(chatId)
    }
}