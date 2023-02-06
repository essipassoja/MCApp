package com.mcapp.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState

const val PREFS_NAME = "auth_prefs"
const val KEY_AUTH = "is_auth"

class AppStatus {
    fun login(context: Context, username: String, password: String, isAuth: MutableState<Boolean>) {
        val authenticated = (username == "test" && password == "pass")

        if (authenticated) {
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean(KEY_AUTH, true).apply()
            println("Username and password are OK")
            isAuth.value =  sharedPref.getBoolean(KEY_AUTH, false)
        } else {
            Toast.makeText(
                context, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(context: Context, isAuth: MutableState<Boolean>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(KEY_AUTH, false).apply()
        println("LOGGED OUT")
        isAuth.value =  sharedPref.getBoolean(KEY_AUTH, false)
    }

    fun addNewReminder(reminder: String) {
        println("Trying to make new reminder: $reminder")
    }
}

