package com.kollama.app.presentation.chat


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kollama.app.presentation.components.ChatInputBar
import com.kollama.app.presentation.components.ChatList
import com.kollama.app.presentation.components.ChatTopBar
import com.kollama.app.presentation.components.SettingsDialog
import com.kollama.app.presentation.components.StatusInfoDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            ChatTopBar(
                connectionStatus = state.connectionStatus,
                showSettings = { viewModel.onEvent(ChatContract.Event.SettingsClick) },
                showStatusInfo = { viewModel.onEvent(ChatContract.Event.StatusInfoClick) }
            )
        },
        bottomBar = {
            ChatInputBar(
                text = state.userInput,
                isLoading = state.isLoading,
                userTextInput = {
                    viewModel.onEvent(ChatContract.Event.UserTextInput(it))
                                },
                sendClick = { viewModel.onEvent(ChatContract.Event.SendClick) },
            )
        }
    ) { paddingValues ->
        ChatList(
            messages = state.messages,
            listState = listState,
            modifier = Modifier.padding(paddingValues)
        )
    }
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
