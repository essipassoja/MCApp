package com.mcapp.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
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
        ChooseNewReminder(appStatus)
        Spacer(modifier = Modifier.height(15.dp))
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

@Composable
fun ChooseNewReminder(appStatus: AppStatus){
    var showMenu by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier) {
            Text(text="Choose a new reminder", )
            Box(modifier = Modifier.weight(0.5f)) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, "")
                }
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        appStatus.addNewReminder("New alarm 1") }) {
                    Text("New alarm 1")
                }
                DropdownMenuItem(
                    onClick = {
                        appStatus.addNewReminder("New alarm 2") }) {
                    Text("New alarm 2")
                }
            }
        }
    }
}
