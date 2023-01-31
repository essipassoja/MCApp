package com.mcapp.ui

import com.mcapp.ui.login.LoginRepository
import com.mcapp.ui.login.LoginViewModel
import org.koin.dsl.module

val loginModule = module {
    factory { LoginRepository() }
    single { LoginViewModel(get()) }
}