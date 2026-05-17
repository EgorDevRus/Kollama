package com.kollama.app.domain.usecase.chats

import com.kollama.app.domain.repository.ChatRepository

/**
 * Обновляет название чата
 *
 * @property repository объект класса для логики взаимодействия
 *
 * @see ChatRepository
 */
class UpdateChatNameUseCase(private val repository: ChatRepository) {

    /**
     * @param chatId Уникальный идентификатор чата
     * @param newName Новое имя чата
     */

    suspend operator fun invoke(chatId: String, newName: String) {
        repository.updateChatName(chatId, newName)
    }
}