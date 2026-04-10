package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository

/**
 * Удаление чата со всеми сообщениями
 */
class ClearHistoryUseCase (private val repository: ChatRepository){
    /**
     * Удаляет запись чата из локальной базы данных
     * @param chatId Уникальный идентификатор чата
     */
    suspend operator fun invoke(chatId: String) {
        repository.clearHistory(chatId)
    }
}