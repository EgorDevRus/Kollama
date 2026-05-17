package com.kollama.app.domain.usecase.messages

import com.kollama.app.domain.repository.MessageRepository

/**
 * Удаление сообщения в чате
 * @see MessageRepository
 */
class DeleteMessageUseCase (private val repository: MessageRepository){

    /** Удаляет сообщение из чата
     * @param id Айди сообщения в чате
     */

    suspend operator fun invoke (id: String){
        repository.deleteMessage(id)
    }
}