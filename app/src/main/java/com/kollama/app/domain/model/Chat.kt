package com.kollama.app.domain.model

/**
 * Модель чата
 *
 * @property id Айди чата
 * @property name Имя чата
 * @property timestamp Время создания чата
 */
data class Chat(
    val id: String,
    val name: String,
    val timestamp: Long
)
