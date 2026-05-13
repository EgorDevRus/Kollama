package com.kollama.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.model.MessageRole
import dev.jeziellago.compose.markdowntext.MarkdownText


/**
 * Элемент для отображения сообщения в чате
 *
 * @param message Данные сообщения
 * @see ChatMessage
 * @param modifier Модификатор для настройки внешнего вида и расположения
 */
@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {

    val isUser = message.role == MessageRole.USER

    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = alignment
    ){
        Surface(

            color = if (isUser) MaterialTheme
                .colorScheme
                .primaryContainer        // Цвет сообщения для пользователя
                else MaterialTheme
                    .colorScheme
                    .secondaryContainer, // Цвет сообщения для ИИ

            // Хвостик сообщения для пользователя и ИИ
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomEnd = if (isUser) 0.dp else 12.dp,
                bottomStart = if (isUser) 12.dp else 0.dp
            ),

        ) {
            // Рендер текста в Md формате
            MarkdownText(
                markdown = message.text,
                modifier = Modifier.padding( 7.dp ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Тестовое сообщение от пользователя
            ChatBubble(
                message = ChatMessage(
                    id = "1",
                    chatId = "test",
                    text = "Hi!",
                    role = MessageRole.USER,
                    timestamp = System.currentTimeMillis()
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Тестовое сообщение от Ollama
            ChatBubble(
                message = ChatMessage(
                    id = "2",
                    chatId = "test",
                    text = "Hi! simple code:\n```kotlin\nfun main() " +
                            "{\n   println(\"Hello, World!\")\n}\n```\n 12312",
                    role = MessageRole.ASSISTANT,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
