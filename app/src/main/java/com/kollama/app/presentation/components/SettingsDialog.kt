package com.kollama.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kollama.app.R
import com.kollama.app.presentation.chat.ChatContract
import com.kollama.app.presentation.theme.KollamaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    state: ChatContract.State,
    onClose: () -> Unit,
    onSave: (String, String) -> Unit,
    onRetry: () -> Unit
) {
    var ip by remember { mutableStateOf(state.serverIp) }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Поле ввода IP-адреса
                    OutlinedTextField(
                        value = ip,
                        onValueChange = { ip = it },
                        label = {
                            Text(stringResource(id = R.string.settings_server_ip))
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

               },


        // Кнопка сохранения настоек
        confirmButton = {
            Button(
                onClick = {
                    onSave(ip,state.selectedModel)
                    onRetry()
                }
            ) {
                Text(stringResource(id = R.string.settings_save))
            }
        },

        // Кнопка закрытия
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(stringResource(id = R.string.settings_cancel))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SettingsDialogPreview() {
    KollamaTheme {
        SettingsDialog(
            state = ChatContract.State(),
            onClose = {},
            onSave = { _, _ -> },
            onRetry = {}
        )
    }
}