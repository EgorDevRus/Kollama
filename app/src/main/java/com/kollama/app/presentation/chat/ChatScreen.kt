package com.kollama.app.presentation.chat


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kollama.app.presentation.components.ChatDrawerItem
import com.kollama.app.presentation.components.ChatInputBar
import com.kollama.app.presentation.components.ChatList
import com.kollama.app.presentation.components.ChatTopBar
import com.kollama.app.presentation.components.SettingsDialog
import com.kollama.app.presentation.components.StatusInfoDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.kollama.app.R
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    viewModel: ChatViewModel = koinViewModel { parametersOf(chatId) }
) {
    val state by viewModel.state.collectAsState()

    val listState = rememberLazyListState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(ChatContract.Event.OnCreateChatClick)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(stringResource(id = R.string.new_chat))
                }

                Spacer(modifier = Modifier.height(8.dp))

                state.chats.forEach { chat ->
                    ChatDrawerItem(
                        chat = chat,
                        isSelected = chat.id == state.currentChatId,
                        onSelect = {
                            viewModel.onEvent(ChatContract.Event.OnChatSelect(chat.id))
                            scope.launch { drawerState.close() }
                        },
                        onDelete = {
                            viewModel.onEvent(ChatContract.Event.OnChatDelete(chat.id))
                        }
                    )
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    ChatTopBar(
                        connectionStatus = state.connectionStatus,
                        showSettings = { viewModel.onEvent(ChatContract.Event.SettingsClick) },
                        showStatusInfo = { viewModel.onEvent(ChatContract.Event.StatusInfoClick) },
                        openMenu = { scope.launch { drawerState.open() } }
                    )
                },
                bottomBar = {
                    ChatInputBar(
                        text = state.userInput,
                        isLoading = state.isLoading,
                        userTextInput = { viewModel.onEvent(ChatContract.Event.UserTextInput(it)) },
                        sendClick = { viewModel.onEvent(ChatContract.Event.SendClick) },
                        availableModels = state.availableModels,
                        selectedModel = state.selectedModel,
                        onModelSelect = { modelName ->
                            viewModel.onEvent(ChatContract.Event.SaveSettings(
                                ip = state.serverIp,
                                model = modelName
                            ))
                        }
                    )
                }
            ) { paddingValues ->
                ChatList(
                    messages = state.messages,
                    listState = listState,
                    onDeleteMessage = { messageId ->
                        viewModel.onEvent(ChatContract.Event.OnDeleteMessageClick(messageId))
                    },
                    onRegenerateClick = {
                        viewModel.onEvent(ChatContract.Event.OnRegenerateClick)
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    )
    if (state.isSettingsDialogVisible) {
        SettingsDialog(
            state = state,
            onClose = { viewModel.onEvent(ChatContract.Event.DismissDialogs) },
            onSave = { ip, model ->
                viewModel.onEvent(ChatContract.Event.SaveSettings(ip, model))
            },
            onRetry = {
                viewModel.onEvent(ChatContract.Event.OnRetryConnection)
            }
        )
    }
    if (state.isInfoDialogVisible) {
        StatusInfoDialog(
            state = state,
            onDismiss = { viewModel.onEvent(ChatContract.Event.DismissDialogs) }
        )
    }

}
