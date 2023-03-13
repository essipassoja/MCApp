package com.mcapp.ui.reminder

import ReminderLocation
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mcapp.data.entity.Reminder
import com.mcapp.util.deleteNotification
import com.mcapp.util.updateNotification
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditOrDeleteReminder(
    viewModel: ReminderViewModel,
    context: Context,
    reminder: Reminder,
    onBack: () -> Unit) {

    // Initialize Reminder variables with known reminder information
    val message = remember { mutableStateOf(reminder.message) }
    val locationX = remember { mutableStateOf(reminder.locationX) }
    val locationY = remember { mutableStateOf(reminder.locationY) }
    val reminderTimes = remember { mutableStateOf(reminder.reminderTimes) }

    // Initialize date and time pickers
    var isChoosingDate by remember { mutableStateOf(false) }
    var isChoosingTime by remember { mutableStateOf(false) }

    // Define date and time formatters
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Initialize location picker
    var isChoosingLocation by remember { mutableStateOf(false) }

    if (isChoosingDate) {
        reminderTimes.value?.last()?.let {
            DatePickerDialog(
                selectedDate = it.toLocalDate(),
                onDateChange = { date ->
                    reminderTimes.value = reminderTimes.value!!.toMutableList().apply {
                        set(lastIndex, LocalDateTime.of(date, reminderTimes.value!!.last().toLocalTime()))
                    }
                },
                onBack = { isChoosingDate = false }
            )
        }
    }
    if (isChoosingTime) {
        reminderTimes.value?.last()?.let {
            TimePickerDialog(
                selectedTime = it.toLocalTime(),
                onTimeChange = { time ->
                    reminderTimes.value = reminderTimes.value!!.toMutableList().apply {
                        set(lastIndex, LocalDateTime.of(reminderTimes.value!!.last().toLocalDate(), time))
                    }
                },
                onBack = { isChoosingTime = false }
            )
        }
    }
    if (isChoosingLocation) {
        Dialog(onDismissRequest = { isChoosingLocation = false }) {
            ReminderLocation(
                onBack = {
                    locationX.value = it.latitude
                    locationY.value = it.longitude
                    isChoosingLocation = false
                }
            )
        }
    }
    else {
        Column(
            horizontalAlignment = Alignment.Start,
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
            Text(text = "New notification:")
            reminderTimes.value?.forEachIndexed { index, time ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    TextButton(
                        onClick = { isChoosingDate = true },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = time.format(dateFormatter))
                    }
                    TextButton(
                        onClick = { isChoosingTime = true },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = time.format(timeFormatter))
                    }
                    IconButton(
                        onClick = {
                            reminderTimes.value = reminderTimes.value!!.toMutableList().apply {
                                removeAt(index)
                            }
                        },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete notification"
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    reminderTimes.value = reminderTimes.value?.toMutableList()?.apply {
                        add(LocalDateTime.now())
                    }
                },
                modifier = Modifier.padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add notification"
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            Text(text = "Add a location for the reminder (Optional):")
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            if (locationX.value == null) {
                IconButton(
                    onClick = {
                        isChoosingLocation = true
                    },
                    modifier = Modifier.padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add location based notification"
                    )
                }
            }
            else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    TextButton(
                        onClick = { isChoosingLocation = true },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text("${locationX.value} ${locationY.value}")
                    }
                    IconButton(
                        onClick = {
                            locationX.value = null
                            locationY.value = null
                        },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete location based notification"
                        )
                    }
                }
            }
            val editedReminder = Reminder(
                reminderId = reminder.reminderId,
                message = message.value,
                locationX = reminder.locationX,
                locationY = reminder.locationY,
                reminderTimes = reminderTimes.value,
                creationTime = reminder.creationTime,
                creatorId = reminder.creatorId,
                reminderSeen = false
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(199, 156, 199, 255),
                    contentColor = Color(255, 255, 255)
                ),
                onClick = {
                    viewModel.insertOrUpdateReminder(editedReminder)
                    updateNotification(context, editedReminder)
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
                    deleteNotification(context, reminder)
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
