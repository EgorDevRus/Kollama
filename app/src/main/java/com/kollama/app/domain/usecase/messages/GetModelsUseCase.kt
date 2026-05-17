package com.kollama.app.domain.usecase.messages

import com.kollama.app.domain.repository.MessageRepository

/**
 * Юзкейс для получения списка установленных моделей с сервера Ollama
 *
 * Используется для:
 * 1. Проверки доступности сервера (пинга)
 * 2. Предоставления пользователю списка моделей для выбора в настройках
 *
 * @see MessageRepository
 */
class GetModelsUseCase(private val repository: MessageRepository) {

    /**
     * Запрашивает список имен моделей у репозитория
     *
     * @param ip Текущий IP-адрес сервера для выполнения запроса
     * @return Список строк с названиями моделей
     */
    suspend operator fun invoke(ip: String): List<String> {
        return repository.getModels(ip)
    }
}