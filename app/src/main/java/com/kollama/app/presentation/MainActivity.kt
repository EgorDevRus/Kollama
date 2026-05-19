package com.kollama.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kollama.app.presentation.chat.ChatScreen
import com.kollama.app.presentation.theme.KollamaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KollamaTheme {
                ChatScreen(chatId = "")
                }
            }
        }
}
