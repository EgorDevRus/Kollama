package com.kollama.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.kollama.app.R
import com.kollama.app.presentation.chat.ChatContract.ConnectionStatus
import com.kollama.app.presentation.theme.StatusBlue
import com.kollama.app.presentation.theme.StatusGreen
import com.kollama.app.presentation.theme.StatusRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    connectionStatus: ConnectionStatus,
    showSettings: () -> Unit,
    showStatusInfo: () -> Unit,
    openMenu: () -> Unit = {}

) {
    val colorStatus = when (connectionStatus) {
        is ConnectionStatus.Connected -> StatusGreen
        is ConnectionStatus.Connecting -> StatusBlue
        is ConnectionStatus.Error -> StatusRed
    }

    CenterAlignedTopAppBar(

        // Кнопка чатов
        navigationIcon = {
            IconButton(onClick = openMenu) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(id = R.string.menu_description)
                )
            }
        },

        // Название приложения (статус)
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color  = colorStatus,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { showStatusInfo() }
            )
        },

        // Настройки IP
        actions = {
            IconButton(onClick = showSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings_description)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatTopBarPreview() {
    MaterialTheme {
        ChatTopBar(
            openMenu = {},
            showSettings = {},
            showStatusInfo = {},
            connectionStatus = ConnectionStatus.Connecting
        )
    }
}
