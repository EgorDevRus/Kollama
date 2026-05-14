package com.kollama.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.domain.model.ChatMessage
import com.kollama.app.domain.model.MessageRole
import dev.jeziellago.compose.markdowntext.MarkdownText
import com.kollama.app.R


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
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val isUser = message.role == MessageRole.USER

    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
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
                modifier = Modifier.padding(7.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            )
        }
            // Иконка корзины для удаления сообщения
            Row(
                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 2.dp)
            ) {
                IconButton(
                    onClick = { onDeleteClick(message.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(
                            id = R.string.delete_message_description
                        ),
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
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
                ),
                onDeleteClick = {}
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
                ),
                onDeleteClick = {}
            )
        }
    }
}
