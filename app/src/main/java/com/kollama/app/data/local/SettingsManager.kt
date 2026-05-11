package com.kollama.app.data.local

import android.content.Context
import com.kollama.app.utils.Constants
import androidx.core.content.edit

/**
 * Менеджер настроек приложения
 *
 * Использует SharedPreferences для постоянного хранения конфигурационных данных
 * IP-адрес сервера Ollama и название выбранной модели
 * При запуске будут настройки сохранённые до этого
 */
class SettingsManager (context: Context) {
    private val prefs = context.getSharedPreferences(
        "settings",
        Context.MODE_PRIVATE // Доступ к настройкам только для этого приложения
    )

    /**
     * Сохраняет настройки сервера в постоянную память
     * @param ip IP-адрес сервера
     * @param model Название модели нейросети
     */

    fun saveSettings(ip: String, model: String) {
        prefs.edit {
            putString("server_ip", ip)
            putString("selected_model", model)
        }
    }

    /** @return Сохраненный IP-адрес или значение по умолчанию */
    fun getIp(): String = prefs.getString(
        "server_ip",
        Constants.IP
    ) ?: Constants.IP

    /** @return Сохраненное название выбранной модели или значение по умолчанию */
    fun getModel(): String = prefs.getString(
        "selected_model",
        Constants.MODEL
    ) ?: Constants.MODEL
}
