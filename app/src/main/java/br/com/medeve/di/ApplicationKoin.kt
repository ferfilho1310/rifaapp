package br.com.medeve.di

import android.app.Application
import br.com.medeve.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationKoin : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationKoin)
            androidLogger(Level.NONE)
            modules(
                listOf(
                    Modules.activityModules,
                    Modules.repository,
                    Modules.viewModel,
                    Modules.retrofit,
                    Modules.adapters
                )
            )
        }
    }
}