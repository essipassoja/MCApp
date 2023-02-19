package com.mcapp.util

import androidx.room.Room
import com.mcapp.data.room.MCDatabase
import com.mcapp.data.room.ReminderRepositoryImpl
import com.mcapp.ui.home.ReminderViewModel
import org.koin.dsl.module

val reminderModule = module {
    single { ReminderViewModel(get()) }
    single { ReminderRepositoryImpl(get()) }
}

val appStatusModule = module {
    single { AppStatus() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), MCDatabase::class.java, "mc_database")
            .build()
    }
    single {get<MCDatabase>().reminderDao()}
}
