package com.kollama.app.domain.usecase.chats

import com.kollama.app.domain.repository.ChatRepository

/** Создание нового чата
 *
 * @see ChatRepository
 */
class CreateChatUseCase(private val repository: ChatRepository) {

    /**
     * @param chatId Уникальный идентификатор чата
     *
     * @param name Наименование чата
     */
    suspend operator fun invoke(chatId: String, name: String) {
        repository.createChat(chatId, name)
    }
}