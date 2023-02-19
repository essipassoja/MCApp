package com.mcapp.ui.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcapp.data.entity.ReminderEntity
import com.mcapp.data.room.ReminderDaoImpl
import com.mcapp.util.AppStatus
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import java.time.LocalDateTime

//class HomeViewModel : ViewModel() {
//    fun addReminder(reminderDao: ReminderDaoImpl, reminder: ReminderEntity) {
//        viewModelScope.launch {
//            reminderDao.insertOrUpdate(reminder)
//        }
//    }
//}

class HomeViewModel(private val reminderDao: ReminderDaoImpl) : ViewModel() {
    fun addReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            reminderDao.insertOrUpdate(reminder)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(context: Context, isAuth: MutableState<Boolean>) {
    val appStatus: AppStatus = get()
    val viewModel: HomeViewModel = get()
//    val db: MCDatabase = get()
//    val reminders = db.reminderDao().getAllReminders(0)
    var isMakingNewReminder by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("Pressing the button")
                    isMakingNewReminder = true },
                contentColor = Color.Blue,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
        },
    ) {
        if (isMakingNewReminder) {
            println("Making a new reminder")
            MakeNewReminder(
                viewModel = viewModel,
                onBack = { isMakingNewReminder = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "Reminder 1",
                            modifier = Modifier.padding(it)
                        )
                        Text(
                            text = "Reminder 2",
                            modifier = Modifier.padding(it)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(bottom = 200.dp).height(10.dp))
                Spacer(modifier = Modifier.padding(top = 200.dp).height(10.dp))
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
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeNewReminder(viewModel: HomeViewModel, onBack: () -> Unit) {
//    val reminderDao: ReminderDaoImpl = get()
    val message = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text(text = "Reminder message:") },
            modifier = Modifier
                .padding(all = 2.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(2.dp).height(10.dp))

        Spacer(modifier = Modifier.padding(2.dp).height(10.dp))
        Button(
            onClick = {
                    viewModel.addReminder(
//                        reminderDao,
                        ReminderEntity(
                            message = message.value,
                            locationX = 0,
                            locationY = 0,
                            reminderTime = LocalDateTime.of(
                                2023, 2, 15, 19, 29),
                            creationTime = LocalDateTime.now(),
                            creatorId = 0,
                            reminderSeen = false
                        )
                    )
                    onBack()
            },
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .size(55.dp)
        ) {
            Text("Save reminder")
        }
    }
}
