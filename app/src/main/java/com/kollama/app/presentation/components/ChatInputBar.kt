package com.kollama.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.R
/**
 *
 * */
@Composable
fun ChatInputBar(
    text: String,
    isLoading: Boolean,
    userTextInput: (String) -> Unit,
    sendClick: () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp, bottom = 18.dp, start = 7.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            value = text,
            onValueChange = { userTextInput(it) },
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = stringResource(id = R.string.chat_input_placeholder)) },
            enabled = !isLoading,
            shape = RoundedCornerShape(24.dp),
            maxLines = 5,
            singleLine = false

        )
        IconButton(onClick = sendClick,
            modifier = Modifier
                .padding(3.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape),
            enabled = !isLoading && text.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription =  stringResource(id = R.string.send_description),

            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputBarPreview ()
{

   ChatInputBar(
       text = "Текс запроса",
       isLoading = false,
       userTextInput = {},
       sendClick = {},
   )
}

@Preview(showBackground = true)
@Composable
fun ChatInputBarLoadingPreview() {
    ChatInputBar(
        text = "ИИ думает",
        isLoading = true,
        userTextInput = {},
        sendClick = {},
    )
}