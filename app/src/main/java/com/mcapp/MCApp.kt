package com.mcapp

import android.app.Application
import com.mcapp.util.appStatusModule
import com.mcapp.util.databaseModule
import com.mcapp.util.reminderModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MCApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            // Android context
            androidContext(this@MCApp)
            // Modules
            modules(getModules())
        }
    }

    private fun getModules(): List<Module> {
        return listOf(
            reminderModule,
            appStatusModule,
            databaseModule,
        )
    }
}
