package com.mcapp.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.mcapp.ui.home.Home
import com.mcapp.ui.login.Login
import com.mcapp.ui.theme.MCAppTheme
import com.mcapp.util.KEY_AUTH
import com.mcapp.util.PREFS_NAME

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MCAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun App() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isAuth = rememberSaveable { mutableStateOf(sharedPref.getBoolean(KEY_AUTH, false)) }

        if (isAuth.value) {
            Home(this, isAuth)
        } else {
            Login(this, isAuth)
        }
    }
}