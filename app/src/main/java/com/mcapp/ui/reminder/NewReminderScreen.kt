package com.mcapp.ui.reminder

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import com.mcapp.util.makeReminderRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MakeNewReminder(
    viewModel: ReminderViewModel,
    context: Context,
    onBack: () -> Unit) {

    val message = remember { mutableStateOf("") }
    val reminderTime = remember { mutableStateOf(LocalDateTime.now()) }
    var isChoosingDate by remember { mutableStateOf(false) }
    var isChoosingTime by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    if (isChoosingDate) {
        DatePickerDialog(
            selectedDate = reminderTime.value.toLocalDate(),
            onDateChange = { date ->
                reminderTime.value = LocalDateTime.of(date, reminderTime.value.toLocalTime())
            },
            onBack = { isChoosingDate = false }
        )
    }
    if (isChoosingTime) {
        TimePickerDialog(
            selectedTime = reminderTime.value.toLocalTime(),
            onTimeChange = { time ->
                reminderTime.value = LocalDateTime.of(reminderTime.value.toLocalDate(), time)
            },
            onBack = { isChoosingTime = false }
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = message.value,
                onValueChange = { text -> message.value = text },
                label = { Text(text = "Reminder message:") },
                modifier = Modifier
                    .padding(all = 2.dp)
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            Text(text = "Reminder time:")
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            TextButton(
                onClick = {
                    isChoosingDate = true
                },
                modifier = Modifier.padding(2.dp)
            ) {
                Text(text = reminderTime.value.format(dateFormatter))
            }
            TextButton(
                onClick = {
                    isChoosingTime = true
                },
                modifier = Modifier.padding(2.dp)
            ) {
                Text(text = reminderTime.value.format(timeFormatter))
            }
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            val newReminder = Reminder(
                message = message.value,
                locationX = 0,
                locationY = 0,
                reminderTime = reminderTime.value,
                creationTime = LocalDateTime.now(),
                creatorId = 0,
                reminderSeen = false,
            )
            Button(
                onClick = {
                    viewModel.insertOrUpdateReminder(newReminder)
                    viewModel.getNewestReminder() { reminder ->
                        reminder?.let {
                            makeReminderRequest(context, reminder)
                            Log.d("NEW REMINDER", "Making notification for the reminder $reminder")
                        }
                    }
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
}

