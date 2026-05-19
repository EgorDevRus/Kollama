package com.kollama.app.presentation.chat

import androidx.compose.ui.graphics.Color
import com.kollama.app.R
import com.kollama.app.domain.model.Chat
import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.presentation.theme.StatusBlue
import com.kollama.app.utils.Constants

/**
 * Контракт взаимодействия для экрана чата
 * Состояние экрана (State) и возможные действия пользователя (Event)
 */
interface ChatContract {

    /**
     * Состояние интерфейса чата
     *
     * @property messages Список всех сообщений в текущем диалоге
     * @property isLoading Флаг, указывающий на процесс ожидания ответа от нейросети
     * @property userInput Сообщение пользователя
     * @property isSettingsDialogVisible Состояние открыто/закрыто окно настроек
     * @property isInfoDialogVisible Состояние открыто/закрыто окно информации
     * @property availableModels Доступные модели ollama
     * @property selectedModel Выбранная модель
     * @property serverIp IP-адрес сервера
     * @property connectionStatus Статус подключения
     * @property connectionStatusText текст при определённом статусе
     * @property connectionStatusColor Цвет при определённом статусе
     * @property currentChatId Айди текущего чата
     * @property chats Список всех созданных чатов
     */
    data class State(

        // Сообщения
        val messages: List<ChatMessage> = emptyList(),
        val isLoading: Boolean = false,
        val userInput: String = "",

        // Настройки и статус
        val isSettingsDialogVisible: Boolean = false,
        val isInfoDialogVisible: Boolean = false,
        val availableModels: List<String> = emptyList(),
        val selectedModel: String = Constants.MODEL,
        val serverIp: String = Constants.IP,
        val connectionStatus: ConnectionStatus = ConnectionStatus.Connecting,
        val connectionStatusText: Int = R.string.status_connecting,
        val connectionStatusColor: Color = StatusBlue,

        // Чаты
        val currentChatId: String = "",
        val chats: List<Chat> = emptyList()
    )
    /** Список действий пользователя на экране */
    sealed interface Event {

        /** Ввод текста пользователем */
        data class UserTextInput(val text: String) : Event

        /** Нажатие на кнопку отправки сообщения */
        object SendClick : Event

        /** Нажатие на кнопку настроек */
        object SettingsClick : Event

        /** Нажатие на кнопку информации (kollama) */
        object StatusInfoClick : Event

        /** Закрытие окон настроек и информации */
        object DismissDialogs : Event

        /** Сохранение настроек */
        data class SaveSettings(val ip: String, val model: String) : Event

        /** Повторное подключение */
        object OnRetryConnection : Event

        /** Нажатие на кнопку удаления сообщения */
        data class OnDeleteMessageClick(val id: String) : Event

            // Взаимодействие с чатами
        /** Нажатие на кнопку создания нового чата */
        object OnCreateChatClick : Event

        /** Выбор чата */
        data class OnChatSelect(val chatId: String) : Event

        /** Удаление чата */
        data class OnChatDelete(val chatId: String) : Event

        /** Обновление названия чата */
        data class OnChatRename(val chatId: String, val newName: String) : Event

        /** Нажатие на кнопку повтороной генерации */
        object OnRegenerateClick : Event

    }

    /** Все статусы сервера */
    sealed interface ConnectionStatus {
        object Connected : ConnectionStatus    // Зеленый
        object Connecting : ConnectionStatus   // Синий
        object Error : ConnectionStatus        // Красный
    }
}