package com.kollama.app.data.repository

import android.util.Log
import com.kollama.app.data.local.ChatDao
import com.kollama.app.data.mapper.toDomain
import com.kollama.app.data.mapper.toEntity
import com.kollama.app.data.remote.dto.*
import com.kollama.app.domain.model.*
import com.kollama.app.domain.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import java.util.UUID

/**
 * Реализация репозитория для управления данными чата.
 *
 * Класс объединяет работу с локальной базой данных Room [ChatDao]
 * и сетевыми запросами к API Ollama через [HttpClient]
 */
class ChatRepositoryImpl (
    private val dao: ChatDao,
    private val client: HttpClient
) : ChatRepository {
    private fun baseUrl(ip: String) = "http://$ip:11434/api"
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    /**
     * Загружает историю сообщений из локальной БД
     * @see ChatRepository.getChatHistoryFlow
     */
    override fun getChatHistoryFlow(chatId: String): Flow<List<ChatMessage>> =
        dao.getMessageFlow(chatId).map {
            list -> list.map { it.toDomain() }
        }

    /**
     * Сохраняет новое сообщение пользователя в БД
     * @see ChatRepository.sendMessage
     */
    override suspend fun sendMessage(chatId: String, text: String) {
        val userMsg = ChatMessage (
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            text = text,
            role = MessageRole.USER,
            timestamp = System.currentTimeMillis()

        )
        dao.insertMessage(userMsg.toEntity())
    }

    /**
     * Выполняет сетевой запрос к Ollama и сохраняет полученный ответ
     * @see ChatRepository.getLiveResponse
     */
    override fun getLiveResponse(
        chatId: String,
        prompt: String,
        ip: String,
        model: String
    ): Flow<String> = flow {
        var accumulatedText = ""
        val botMessageId = UUID.randomUUID().toString()

        val fullUrl = "${baseUrl(ip)}/chat"

        Log.d("OLLAMA_API", "Запрос: $fullUrl | Модель: $model")
        //  Сразу кладем в БД пустое сообщение от бота
        val initialBotMsg = ChatMessage(
            id = botMessageId,
            chatId = chatId,
            text = "",
            role = MessageRole.ASSISTANT,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMessage(initialBotMsg.toEntity())
        try {
            client.preparePost(fullUrl) {
                setBody(ChatRequest(
                    model = model,
                    messages = listOf(ChatMessageDto(role = "user", content = prompt)),
                    stream = true
                ))
            }.execute { response ->
                val channel = response.bodyAsChannel()
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line() ?: break
                    if (line.isBlank()) continue

                    val chunk = jsonParser.decodeFromString<ChatResponse>(line)
                    val newPart = chunk.message?.content ?: ""
                    accumulatedText += newPart

                    dao.updateMessageText(botMessageId, accumulatedText)
                    emit(accumulatedText)
                }
            }
        } catch (e: Exception) {
            emit("Ошибка: ${e.localizedMessage}")
        }

    }
    override suspend fun getModels(ip: String): List<String> {
        return try {
            val url = "${baseUrl(ip)}/tags"

            val response = client.get(url).body<ModelsResponse>()
            response.models.map { it.name }
        } catch (_: Exception) {
            emptyList() // Если сервер спит, возвращаем пустой список
        }
    }

    /**
     * Очищает всю историю сообщений для текущего чата
     * @see ChatRepository.clearHistory
     */
    override suspend fun clearHistory(chatId: String) = dao.clearChatHistory(chatId)

    /**
     * Удаление сообщения
     * @see ChatRepository.deleteMessage
     */
    override suspend fun deleteMessage(id: String) = dao.deleteMessage(id)
}