package com.kollama.app

import android.app.Application
import com.kollama.app.di.databaseModule
import com.kollama.app.di.networkModule
import com.kollama.app.di.presentationModule
import com.kollama.app.di.repositoryModule
import com.kollama.app.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KollamaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KollamaApp)
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                useCaseModule,
                presentationModule
            )
        }
    }
}