package com.mcapp.util

import androidx.room.Room
import com.mcapp.data.room.MCDatabase
import com.mcapp.data.room.ReminderDao
import com.mcapp.data.room.ReminderDaoImpl
import com.mcapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single { ReminderDaoImpl(get()) }
    viewModel { HomeViewModel(get()) }
}

val appStatusModule = module {
    single { AppStatus() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), MCDatabase::class.java, "app_database")
            .build()
    }
    single<ReminderDao> { ReminderDaoImpl(get()) }
}