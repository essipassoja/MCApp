package com.mcapp.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState

const val PREFS_NAME = "auth_prefs"
const val KEY_AUTH = "is_auth"

class AppStatus {
    fun login(context: Context, username: String, password: String, isAuth: MutableState<Boolean>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        registerNewUser(context, "test", "password")  // debug
//        registerNewUser(context, "test2", "pwd")  // debug
        val authenticated = ((username == sharedPref.getString("username", username)) &&
                (password == sharedPref.getString("password", password)))
        println("Authenticated = $authenticated")  // debug

        if (authenticated) {
            sharedPref.edit().putBoolean(KEY_AUTH, true).apply()
            isAuth.value =  sharedPref.getBoolean(KEY_AUTH, false)
        } else {
            Toast.makeText(
                context, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(context: Context, isAuth: MutableState<Boolean>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(KEY_AUTH, false).apply()
        println("LOGGED OUT")  // debug
        isAuth.value =  sharedPref.getBoolean(KEY_AUTH, false)
    }

    fun registerNewUser(context: Context, username: String, password: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
}

