package com.mcapp.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import java.time.LocalDateTime

@Composable
fun MakeNewReminder(viewModel: ReminderViewModel, onBack: () -> Unit) {
    val message = remember { mutableStateOf("") }
    val reminderTime = remember { mutableStateOf(LocalDateTime.now()) }
    var isChoosingDate by remember { mutableStateOf(false) }
    var isChoosingTime by remember { mutableStateOf(false) }

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
            Button(
                onClick = {
                    isChoosingDate = true
                },
                modifier = Modifier.padding(2.dp)
            ) {
                Text(text = "Select date")
            }
            Button(
                onClick = {
                    isChoosingTime = true
                },
                modifier = Modifier.padding(2.dp)
            ) {
                Text(text = "Select time")
            }
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            Button(
                onClick = {
                    viewModel.insertOrUpdateReminder(
                        Reminder(
                            message = message.value,
                            locationX = 0,
                            locationY = 0,
                            reminderTime = reminderTime.value,
                            creationTime = LocalDateTime.now(),
                            creatorId = 0,
                            reminderSeen = false,
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
}

