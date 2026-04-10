package com.kollama.app.domain.model
/**
 * Модель данных для сообщения в чате
 *
 * @property id Уникальный идентификатор сообщения
 * @property text Текст сообщения
 * @property role Роль отправителя (Пользователь или ИИ)
 * @property timestamp Время отправки (в мс)
 * @see [MessageRole]
 */
data class ChatMessage(
    val id: String,
    val text: String,
    val role: MessageRole,
    val timestamp: Long

)
