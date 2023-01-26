package com.mcapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // start Koin
        startKoin {
            //  declare used Android context
            androidContext(this@App)
            androidLogger(Level.INFO)
            // declare modules
//            modules(myModule)
        }
    }
}