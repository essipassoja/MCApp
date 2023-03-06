package com.mcapp.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import com.mcapp.ui.reminder.EditOrDeleteReminder
import com.mcapp.ui.reminder.MakeNewReminder
import com.mcapp.ui.reminder.ReminderViewModel
import com.mcapp.ui.reminder.ReminderViewState
import com.mcapp.util.AppStatus
import org.koin.androidx.compose.get
import java.time.LocalDateTime

@Composable
fun Home(reminderViewModel: ReminderViewModel, context: Context, isAuth: MutableState<Boolean>) {
    val appStatus: AppStatus = get()
    val creatorId: Long = 0
    var showAllReminders by remember { mutableStateOf(false) }
    var isMakingNewReminder by remember { mutableStateOf(false) }
    val reminderUpdated = remember { mutableStateOf(false) }
    LaunchedEffect(reminderUpdated.value) {
        reminderViewModel.getListOfAllReminders(creatorId)
    }

    if (isMakingNewReminder) {
        MakeNewReminder(
            viewModel = reminderViewModel,
            context = context,
            onBack = { isMakingNewReminder = false
                       reminderUpdated.value = false}
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReminderList(
                    context,
                    reminderViewModel,
                    reminderUpdated,
                    showAllReminders
                )
                Spacer(modifier = Modifier
                    .padding(bottom = 20.dp)
                    .height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { showAllReminders = !showAllReminders },
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (showAllReminders) "Hide unnotified reminders" else "Show all reminders"
                        )
                    }
                }
                Spacer(modifier = Modifier
                    .padding(top = 20.dp)
                    .height(10.dp))
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

            FloatingActionButton(
                onClick = {
                    isMakingNewReminder = true
                    reminderUpdated.value = true},
                contentColor = Color.Blue,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun ReminderList(
    context: Context,
    reminderViewModel: ReminderViewModel,
    reminderUpdated: MutableState<Boolean>,
    showAllReminders: Boolean
) {

    val reminderViewState by reminderViewModel.reminders.collectAsState()
    when (reminderViewState) {
        is ReminderViewState.Loading -> {}
        is ReminderViewState.Success -> {
            val reminderList = (reminderViewState as ReminderViewState.Success).data
            Log.d("REMINDERS", "Found reminders: $reminderList")

            LazyColumn(
                contentPadding = PaddingValues(),
                verticalArrangement = Arrangement.Center
            ) {
                items(reminderList) { item ->
                    ReminderListItem(
                        reminder = item,
                        viewModel = reminderViewModel,
                        context = context,
                        reminderUpdated = reminderUpdated,
                        showAllReminders = showAllReminders
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    viewModel: ReminderViewModel,
    context: Context,
    reminderUpdated: MutableState<Boolean>,
    showAllReminders: Boolean
) {
    var isEditingReminder by remember { mutableStateOf(false) }

    if ((!reminder.reminderSeen && reminder.reminderTimes.firstOrNull {
            !it.isBefore(LocalDateTime.now()) } != null) || showAllReminders)
    {
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(100, 75, 100),
                contentColor = Color(255, 255, 255)
            ),
            onClick = { isEditingReminder = true },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .size(55.dp)
        ) {
            Text(reminder.message)
        }

        if (isEditingReminder) {
            EditOrDeleteReminder(
                viewModel = viewModel,
                context = context,
                reminder = reminder,
                onBack = {
                    isEditingReminder = false
                    reminderUpdated.value = !reminderUpdated.value
                }
            )
        }
    }
}
