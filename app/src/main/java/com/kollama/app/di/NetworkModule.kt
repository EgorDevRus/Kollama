package com.kollama.app.di

import com.kollama.app.data.local.SettingsManager
import com.kollama.app.data.remote.provideHttpClient
import org.koin.dsl.module

/**
 * Модуль Koin для настройки сети
 * Создаётся только один раз за работу приложения (single)
 * provideHttpClient()
 * SettingsManager - Запоминание ip по умолчанию
 */
val  networkModule = module {
    single { provideHttpClient() }
    single { SettingsManager(get()) }
}

