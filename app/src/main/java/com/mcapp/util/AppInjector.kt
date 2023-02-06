package com.mcapp.util

import com.mcapp.ui.home.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    single { HomeViewModel() }
}

val appStatusModule = module {
    single { AppStatus() }
}