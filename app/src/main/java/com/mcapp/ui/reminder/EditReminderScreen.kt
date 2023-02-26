package com.mcapp.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditOrDeleteReminder(viewModel: ReminderViewModel, reminder: Reminder, onBack: () -> Unit) {
    val message = remember { mutableStateOf(reminder.message) }
    val reminderTime = remember { mutableStateOf(reminder.reminderTime) }
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
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(199, 156, 199, 255),
                    contentColor = Color(255, 255, 255)
                ),
                onClick = {
                    viewModel.insertOrUpdateReminder(
                        Reminder(
                            reminderId = reminder.reminderId,
                            message = message.value,
                            locationX = 0,
                            locationY = 0,
                            reminderTime = reminderTime.value,
                            creationTime = reminder.creationTime,
                            creatorId = reminder.creatorId,
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
                Text("Save changes")
            }
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(199, 50, 19, 255),
                    contentColor = Color(255, 255, 255)
                ),
                onClick = {
                    viewModel.deleteReminder(reminder)
                    onBack()
                },
                modifier = Modifier
                    .padding(0.dp)
                    .width(150.dp)
                    .size(50.dp)
            ) {
                Text("Delete reminder")
            }
        }
    }
}
