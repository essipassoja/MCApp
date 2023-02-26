package com.mcapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
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
import org.koin.androidx.compose.get

class MainActivity : AppCompatActivity() {
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

    @Composable
    fun App() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isAuth = rememberSaveable { mutableStateOf(sharedPref.getBoolean(KEY_AUTH, false)) }

        if (isAuth.value) {
            Home(get(), this, isAuth)
        } else {
            Login(this, isAuth)
        }
    }
}