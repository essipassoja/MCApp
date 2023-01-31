package com.mcapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.mcapp.ui.login.Login
import com.mcapp.ui.login.LoginViewModel
import com.mcapp.ui.theme.MCAppTheme
import org.koin.androidx.compose.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MCAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Login(LoginViewModel(get()))
                }
            }
        }
    }
}