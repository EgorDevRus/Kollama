package com.kollama.app.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kollama.app.R
import com.kollama.app.presentation.chat.ChatContract
import com.kollama.app.presentation.theme.KollamaTheme
import com.kollama.app.presentation.theme.StatusBlue

@Composable
fun StatusInfoDialog(
    state: ChatContract.State,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.status_dialog_title)) },
        text = {
            Text(
                text = stringResource(
                    R.string.status_info_full,
                    stringResource(state.connectionStatusText),
                    state.serverIp
                ),
                color = state.connectionStatusColor
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.status_dialog_confirm))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StatusInfoDialogPreview() {
    KollamaTheme {
        StatusInfoDialog(
            state = ChatContract.State(
                connectionStatusText = R.string.status_connecting,
                serverIp = "192.168.1.21121",
                connectionStatusColor = StatusBlue
            ),
            onDismiss = {}
        )
    }
}
