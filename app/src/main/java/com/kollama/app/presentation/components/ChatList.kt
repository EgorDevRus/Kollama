package com.kollama.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.model.MessageRole

@Composable
fun ChatList(
    messages: List<ChatMessage>,
    listState: LazyListState,
    onDeleteMessage: (String) -> Unit,
    onRegenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Авто скролла нет так как не в самом низу
    val isAtTheBottom = remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                true
            } else {

                val lastVisibleItem = visibleItemsInfo.last()
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 1
            }
        }
    }

    // Авто скролл
    LaunchedEffect(messages.size, messages.lastOrNull()?.text?.length) {
        if (messages.isNotEmpty()) {
            // Экран следует за текстом если пользователь в самом низу
            if (isAtTheBottom.value) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = messages,
    key = { _, item -> item.id }
    ) { index, message ->
        ChatBubble(
            message = message,
            onDeleteClick = onDeleteMessage,
            onRegenerateClick = onRegenerateClick,
            isLastMessage = index == messages.lastIndex
        )
    }
}
}


@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    val testMessages = listOf(
        ChatMessage(
            id = "1",
            text = "Привет!",
            role = MessageRole.USER,
            chatId = "preview_chat",
            timestamp = System.currentTimeMillis()
        ),
        ChatMessage(
            id = "2",
            text = "Привет!",
            role = MessageRole.ASSISTANT,
            chatId = "preview_chat",
            timestamp = System.currentTimeMillis()
        ),
        ChatMessage(
            id = "3",
            text = "\n23\n32",
            role = MessageRole.USER,
            chatId = "preview_chat",
            timestamp = System.currentTimeMillis()
        )
    )

    ChatList(
        messages = testMessages,
        listState = rememberLazyListState(),
        onDeleteMessage = {},
        onRegenerateClick = {}
    )
}

