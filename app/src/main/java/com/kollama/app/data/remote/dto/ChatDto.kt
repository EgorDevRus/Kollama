package com.kollama.app.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Запрос к Ollama для генерации ответа в чате
 *
 * @property model Название используемой модели
 * @property messages Список предыдущих сообщений для сохранения контекста диалога
 * @property stream Флаг потоковой передачи ответа (true для получения текста по частям)
 */
@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessageDto>,
    val stream: Boolean = true
)

/**
 * Сообщение в формате Ollama
 *
 * @property role Роль отправителя: пользователь или ИИ
 * @property content Текстовое содержимое сообщения
 */
@Serializable
data class ChatMessageDto (
    val role: String,
    val content: String
)

/**
 * Ответ от Ollama
 *
 * @property message Объект сообщения, содержащий ответ от нейросети
 * Может быть пустым (null) в самом конце переписки
 * @property done Флаг завершения генерации ответа
 */
@Serializable
data class ChatResponse(
    val message: ChatMessageDto ? = null,
    val done: Boolean
)