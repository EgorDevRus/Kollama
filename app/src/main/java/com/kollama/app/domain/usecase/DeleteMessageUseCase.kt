package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository

/**
 * Удаление сообщения в чате
 * @see ChatRepository
 */
class DeleteMessageUseCase (private val repository: ChatRepository){

    /** Удаляет сообщение из чата
     * @param id Айди сообщения в чате
     */

    suspend operator fun invoke (id: String){
        repository.deleteMessage(id)
    }
}