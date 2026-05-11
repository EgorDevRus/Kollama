package com.kollama.app.domain.usecase

import com.kollama.app.domain.repository.ChatRepository

/**
 * Юзкейс для получения списка установленных моделей с сервера Ollama
 *
 * Используется для:
 * 1. Проверки доступности сервера (пинга)
 * 2. Предоставления пользователю списка моделей для выбора в настройках
 */
class GetModelsUseCase(private val repository: ChatRepository) {

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