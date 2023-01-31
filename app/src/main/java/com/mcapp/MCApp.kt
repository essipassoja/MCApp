package com.mcapp

import android.app.Application
import com.mcapp.ui.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MCApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            // Android context
            androidContext(this@MCApp)
            // Modules
            modules(getModules())
        }
    }

    private fun getModules(): List<Module> {
        return listOf(
            loginModule
            // Add your Koin modules here
        )
    }
}