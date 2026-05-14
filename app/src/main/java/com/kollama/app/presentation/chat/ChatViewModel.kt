package com.kollama.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kollama.app.R
import com.kollama.app.data.local.SettingsManager
import com.kollama.app.domain.usecase.DeleteMessageUseCase
import com.kollama.app.domain.usecase.GetChatHistoryUseCase
import com.kollama.app.domain.usecase.GetModelsUseCase
import com.kollama.app.domain.usecase.SendMessageUseCase
import com.kollama.app.presentation.theme.StatusBlue
import com.kollama.app.presentation.theme.StatusGreen
import com.kollama.app.presentation.theme.StatusRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * ViewModel для управления состоянием экрана чата
 *
 * Связь между пользовательским интерфейсом и бизнес-логикой (UseCase)
 * обрабатывая действия пользователя и обновляя состояние экрана
 *
 * @property chatId Уникальный идентификатор текущего диалога
 *
 * @property sendMessageUseCase Отправка сообщения и получение ответа
 * @see SendMessageUseCase
 *
 * @property getChatHistoryUseCase Получения истории сообщений из локальной БД
 * @see GetChatHistoryUseCase
 *
 * @property settingsManager Хранение состояний настроек
 * @see SettingsManager
 *
 * @property getModelsUseCase Полуечение списка моделей
 * @see GetModelsUseCase
 *
 * @property deleteMessageUseCase Удаление сообщения в чате
 * @see DeleteMessageUseCase
 */
class ChatViewModel (
    private val chatId: String,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val settingsManager: SettingsManager,
    private val getModelsUseCase: GetModelsUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase

) : ViewModel(){
    private val _state = MutableStateFlow(ChatContract.State(
        serverIp = settingsManager.getIp(),
        selectedModel = settingsManager.getModel()
    ))
    val state = _state.asStateFlow()



    init {

        // Вывод сообщений из БД
        loadMessages(chatId)

        // Проверка подключения
        checkConnection()
    }

    fun onEvent(event: ChatContract.Event) {
        when (event) {

            /** Пользователь вводит текст */
            is ChatContract.Event.UserTextInput -> {

                // Обновление текст ввода
                _state.update { it.copy(userInput = event.text) }
            }

            /** Пользователь отправляет запрос */
            is ChatContract.Event.SendClick -> {
                send()
            }

            /** Нажатие кнопки настроек */
            is ChatContract.Event.SettingsClick -> {
                _state.update { it.copy(isSettingsDialogVisible = true) }

            }


            /** Показать информацию */
            is ChatContract.Event.StatusInfoClick -> {
                _state.update { it.copy(isInfoDialogVisible = true) }
                checkConnection() // Переподключение при открытии настроек
            }

            /** Сохранить настройки */
            is ChatContract.Event.SaveSettings -> {
                settingsManager.saveSettings(event.ip, event.model)
                _state.update {
                    it.copy(
                        serverIp = event.ip,
                        selectedModel = event.model,
                        isSettingsDialogVisible = false
                    )
                }
                checkConnection()
            }

            /** Закрытие настроек и информации */
            is ChatContract.Event.DismissDialogs -> {
                _state.update {
                    it.copy(
                        isSettingsDialogVisible = false,
                        isInfoDialogVisible = false
                    )
                }
            }

            /** Повторное подключение */
            is ChatContract.Event.OnRetryConnection -> {
                checkConnection()
            }

            /** Нажатие на кнопку удаления */
            is ChatContract.Event.OnDeleteMessageClick -> {
                viewModelScope.launch {
                    deleteMessageUseCase(event.id)
                }
            }
        }
    }

    /** Показывает все сообщения в чате */
    private fun loadMessages(id: String) {
        viewModelScope.launch {
            getChatHistoryUseCase(id).collect { list ->
                _state.update { it.copy(messages = list) }
            }
        }
    }

    /** Отправка сообщения к нейронке */
    private fun send() {
        // Текст пользователя
        val textToSend = _state.value.userInput

        if (textToSend.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(userInput = "", isLoading = true) }
            sendMessageUseCase(
                chatId = chatId,
                text = textToSend,
                ip = _state.value.serverIp,
                model = _state.value.selectedModel
                ).collect { _ ->
                    _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /** Проверка подключения */
    private fun checkConnection() {
        viewModelScope.launch(Dispatchers.IO) {

            // Статус: подключения
            _state.update { it.copy(
                connectionStatus = ChatContract.ConnectionStatus.Connecting,
                connectionStatusText = R.string.status_connecting,
                connectionStatusColor = StatusBlue
            )}

            val currentIp = settingsManager.getIp()
            val models = getModelsUseCase(currentIp)

            // Статус: подключён
            if (models.isNotEmpty()) {
                _state.update { it.copy(
                    connectionStatus = ChatContract.ConnectionStatus.Connected,
                    connectionStatusText = R.string.status_online,
                    connectionStatusColor = StatusGreen,
                    availableModels = models
                )}
            } else {
                // В остальном случае не подключён
                _state.update { it.copy(
                    connectionStatus = ChatContract.ConnectionStatus.Error,
                    connectionStatusText = R.string.status_offline,
                    connectionStatusColor = StatusRed
                )}
            }
        }
    }

}
