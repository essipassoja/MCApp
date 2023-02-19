package com.mcapp.util

import androidx.room.Room
import com.mcapp.data.MCDatabase
import com.mcapp.data.datasource.ReminderDataSource
import com.mcapp.data.datasource.ReminderDataSourceImpl
import com.mcapp.data.repository.ReminderRepositoryImpl
import com.mcapp.ui.reminder.ReminderViewModel
import org.koin.dsl.module

val reminderModule = module {
    single { ReminderViewModel(get()) }
    single<ReminderDataSource> { ReminderDataSourceImpl(get()) }
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
