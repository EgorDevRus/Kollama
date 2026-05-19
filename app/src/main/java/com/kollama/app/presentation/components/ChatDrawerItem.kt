package com.kollama.app.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.domain.model.Chat
import com.kollama.app.R
/**
 * Элемент списка чатов внутри бокового меню
 */
@Composable
fun ChatDrawerItem(
    chat: Chat,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = stringResource(id = R.string.delete_chat_description),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        selected = isSelected,
        onClick = onSelect,
        modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun ChatDrawerItemPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Обычный чат
            ChatDrawerItem(
                chat = Chat(
                    id = "1",
                    name = "Привет",
                    timestamp = System.currentTimeMillis()
                ),
                isSelected = false,
                onSelect = {},
                onDelete = {}
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Выбранный чат
            ChatDrawerItem(
                chat = Chat(
                    id = "2",
                    name = "привет",
                    timestamp = System.currentTimeMillis()
                ),
                isSelected = true,
                onSelect = {},
                onDelete = {}
            )
        }
    }
}
