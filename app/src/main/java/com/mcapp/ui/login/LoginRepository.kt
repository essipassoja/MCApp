package com.mcapp.ui.login

import android.annotation.SuppressLint

class LoginRepository {
    @SuppressLint("RestrictedApi")
    fun login(username: String, password: String): Result<User> {
        return if (username == "test" && password == "password") {
            Result.Success(User("test", "Test User"))
        } else {
            Result.Error(Exception("Incorrect username or password"))
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

data class User(val username: String, val name: String)
