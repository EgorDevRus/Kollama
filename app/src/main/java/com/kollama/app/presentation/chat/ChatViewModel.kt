package com.kollama.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kollama.app.R
import com.kollama.app.data.local.SettingsManager
import com.kollama.app.domain.model.MessageRole
import com.kollama.app.domain.usecase.chats.ChatsUseCase
import com.kollama.app.domain.usecase.messages.MessagesUseCase
import com.kollama.app.presentation.theme.StatusBlue
import com.kollama.app.presentation.theme.StatusGreen
import com.kollama.app.presentation.theme.StatusRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


/**
 * ViewModel для управления состоянием экрана чата
 *
 * Связь между пользовательским интерфейсом и бизнес-логикой (UseCase)
 * обрабатывая действия пользователя и обновляя состояние экрана
 *
 * @property messagesUseCase Взаимодествие с сообщениями
 * @see messagesUseCase
 *
  * @property settingsManager Хранение состояний настроек
 * @see SettingsManager
 *
 * @property chatsUseCase Взаимодествие с чатами
 * @see chatsUseCase
 *
 */
class ChatViewModel (

    private val messagesUseCase: MessagesUseCase,
    private val settingsManager: SettingsManager,
    private val chatsUseCase: ChatsUseCase

) : ViewModel(){
    private val _state = MutableStateFlow(ChatContract.State(
        serverIp = settingsManager.getIp(),
        selectedModel = settingsManager.getModel()
    ))
    val state = _state.asStateFlow()
    private var messagesJob: kotlinx.coroutines.Job? = null
    private var apiJob: kotlinx.coroutines.Job? = null


    init {
        viewModelScope.launch {

            // Загрузка всех чатов
            loadChats()

            val startChatId = _state.value.currentChatId.ifBlank {
                UUID.randomUUID().toString()
            }


            _state.update { it.copy(currentChatId = startChatId) }

            // Вывод сообщений из БД
            loadMessages(startChatId)

            // Проверка подключения
            checkConnection()
        }

    }

    fun onEvent(event: ChatContract.Event) {
        when (event) {

            /** Пользователь вводит текст */
            is ChatContract.Event.UserTextInput -> {

                // Обновление текст ввода
                _state.update { it.copy(userInput = event.text) }
            }

            /** Пользователь отправляет запрос */
            is ChatContract.Event.SendClick -> send()

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
                    messagesUseCase.deleteMessage(event.id)

                    // Нажали на кнопку у последнего сообщения в чате
                    if (_state.value.messages.size <= 1) {

                        // Удаляем чат из БД
                        chatsUseCase.deleteChat(_state.value.currentChatId)

                        createNewChat()
                    }
                }
            }

            /** Нажатие на кнопку повторной генерации */
            is ChatContract.Event.OnRegenerateClick -> regenerateLastResponse()

            //
                //          Чаты
            //
            /** Нажатие на кнопку создания чата */
            is ChatContract.Event.OnCreateChatClick -> createNewChat()

            /** Выбор чата */
            is ChatContract.Event.OnChatSelect -> loadMessages(event.chatId)

            /** Удаление чата */
            is ChatContract.Event.OnChatDelete -> removeChat(event.chatId)

            /** Обновление названия чата */
            is ChatContract.Event.OnChatRename -> renameChat(event.chatId, event.newName)


        }
    }

    private fun regenerateLastResponse() {
        if (_state.value.isLoading) return

        val currentChat = _state.value.currentChatId
        val messagesList = _state.value.messages

        apiJob?.cancel()

        apiJob = viewModelScope.launch {
            // Находим последнее сообщение от ИИ и пользователя
            val lastAssistantMessage = messagesList.lastOrNull { it.role == MessageRole.ASSISTANT }
            val lastUserMessage = messagesList.lastOrNull { it.role == MessageRole.USER }

            if (lastUserMessage != null) {
                _state.update { it.copy(isLoading = true) }
                // Удаляем ответ нейронки
                try {
                    lastAssistantMessage?.let {
                        messagesUseCase.deleteMessage(it.id)
                        delay(50)
                    }
                    // Повторно отправляем текст последнего вопроса
                    messagesUseCase.regenerateResponse(
                        chatId = currentChat,
                        prompt = lastUserMessage.text,
                        ip = _state.value.serverIp,
                        model = _state.value.selectedModel
                    ).collect {
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }




    /** Показывает все сообщения в чате */
    private fun loadMessages(chatId: String) {
        _state.update { it.copy(currentChatId = chatId) }

        messagesJob?.cancel()

        messagesJob = viewModelScope.launch {
            messagesUseCase.getChatHistory(chatId).collect { list ->
                _state.update { it.copy(messages = list) }
            }
        }
    }

    /** Отправка сообщения к нейронке */
    private fun send() {
        // Текст пользователя
        val textToSend = _state.value.userInput
        if (textToSend.isBlank() || _state.value.isLoading) return

        val currentChat = _state.value.currentChatId
        val chatExists = _state.value.chats.any { it.id == currentChat }
        val isFirstMessage = !chatExists

        _state.update {
            it.copy(
                userInput = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            try {
                // Чат по названию первого сообщения
                if (isFirstMessage) {
                    val cleanName = if (textToSend.length > 10) {
                        textToSend.take(10)
                    } else {
                        textToSend
                    }
                    chatsUseCase.createChat(currentChat, cleanName)
                }

                messagesUseCase.sendMessage(
                    chatId = currentChat,
                    text = textToSend,
                    ip = _state.value.serverIp,
                    model = _state.value.selectedModel
                ).collect {
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }

    }

    /** Проверка подключения */
    private fun checkConnection() {
        viewModelScope.launch {

            // Статус: подключение
            _state.update { it.copy(
                connectionStatus = ChatContract.ConnectionStatus.Connecting,
                connectionStatusText = R.string.status_connecting,
                connectionStatusColor = StatusBlue
            )}

            val currentIp = settingsManager.getIp()
            val models = messagesUseCase.getModelsUseCase(currentIp)

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

    // Логика чатов


    private fun  loadChats() {
        viewModelScope.launch {
            chatsUseCase.getAllChats().collect { chatList ->
                _state.update { it.copy(chats = chatList) }
            }
        }
    }

    private fun createNewChat() {

        val newId = UUID.randomUUID().toString()

        _state.update { it.copy(messages = emptyList()) }

        loadMessages(newId)
    }
    private fun removeChat(chatId: String) {
        viewModelScope.launch {
            chatsUseCase.deleteChat(chatId)

            if (_state.value.currentChatId == chatId){
                createNewChat()
            }
        }
    }
    private fun renameChat(chatId: String, newName: String) {
        viewModelScope.launch {
            chatsUseCase.updateChatName(chatId = chatId, newName = newName)
        }

    }

}
