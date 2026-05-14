package com.kollama.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = messages,
            key = {it.id}
        ) { message ->
            ChatBubble(message = message, onDeleteClick = onDeleteMessage)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    val TestMessages = listOf(
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
        messages = TestMessages,
        listState = rememberLazyListState(),
        onDeleteMessage = {}
    )
}

