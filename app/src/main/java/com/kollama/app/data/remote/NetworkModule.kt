package com.kollama.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


/**
 * Создаёт HTTP-клиент (Ktor) для работы с API
 *
 * Использует движок Android
 * Плагин [ContentNegotiation]: автоматически конвертирует JSON в объекты Kotlin и наоборот
 *  - [ignoreUnknownKeys]: Игнорирует неизвестные поля от сервера (защита от падения приложения)
 *  - [prettyPrint]: Форматирует JSON в логах
 *  - [isLenient]: Разрешает нестандартный JSON
 * Плагин [Logging]: выводит всё в логи (Logcat)
 * @return Экземпляр [HttpClient]
 */
fun provideHttpClient(): HttpClient {
    return HttpClient (Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.DEFAULT
        }
    }
}