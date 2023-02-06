package com.mcapp.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.mcapp.util.AppStatus
import org.koin.androidx.compose.get

class HomeViewModel : ViewModel() {

    fun sayHello() : String{
        return "Hello"
    }
}

@Composable
fun Home(viewModel: HomeViewModel, context: Context, isAuth: MutableState<Boolean>) {
    val appStatus: AppStatus = get()

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Text(text = viewModel.sayHello())
                Text(text = "Reminder 1")
                Text(text = "Reminder 2")
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Button(
            onClick = {
                appStatus.logout(context, isAuth)
                      },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .size(45.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = "Log out")
            }
        }
    }
}