package com.mcapp.ui.reminder

import GetLocation
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import com.mcapp.util.makeNewNotification
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MakeNewReminder(
    viewModel: ReminderViewModel,
    context: Context,
    onBack: () -> Unit) {

    // Initialize Reminder variables
    val message = remember { mutableStateOf("") }
    val locationX = remember { mutableStateOf<Double?>(null) }
    val locationY = remember { mutableStateOf<Double?>(null) }
    val reminderTimes = remember { mutableStateOf(listOf(LocalDateTime.now())) }

    // Initialize date and time pickers
    var isChoosingDate by remember { mutableStateOf(false) }
    var isChoosingTime by remember { mutableStateOf(false) }

    // Define date and time formatters
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Initialize location picker
    var isChoosingLocation by remember { mutableStateOf(false) }

    if (isChoosingDate) {
        DatePickerDialog(
            selectedDate = reminderTimes.value.last().toLocalDate(),
            onDateChange = { date ->
                reminderTimes.value = reminderTimes.value.toMutableList().apply {
                    set(lastIndex, LocalDateTime.of(date, reminderTimes.value.last().toLocalTime()))
                }
            },
            onBack = { isChoosingDate = false }
        )
    }
    if (isChoosingTime) {
        TimePickerDialog(
            selectedTime = reminderTimes.value.last().toLocalTime(),
            onTimeChange = { time ->
                reminderTimes.value = reminderTimes.value.toMutableList().apply {
                    set(lastIndex, LocalDateTime.of(reminderTimes.value.last().toLocalDate(), time))
                }
            },
            onBack = { isChoosingTime = false }
        )
    }
    if (isChoosingLocation) {
        GetLocation(
            onBack = { selectedLocation ->
                locationX.value = selectedLocation.latitude
                locationY.value = selectedLocation.longitude
                isChoosingLocation = false
            }
        )
    }
    else {
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
            Text(text = "New notification:")
            Spacer(
                modifier = Modifier
                    .padding(2.dp)
                    .height(10.dp)
            )
            reminderTimes.value.forEachIndexed { index, time ->
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
                            reminderTimes.value = reminderTimes.value.toMutableList().apply {
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
                    reminderTimes.value = reminderTimes.value.toMutableList().apply {
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
            val newReminder = Reminder(
                message = message.value,
                locationX = locationX.value,
                locationY = locationY.value,
                reminderTimes = reminderTimes.value,
                creationTime = LocalDateTime.now(),
                creatorId = 0,
                reminderSeen = false,
            )
            Button(
                onClick = {
                    // Send the reminder to the database
                    viewModel.insertOrUpdateReminder(newReminder)
                    // Wait 1s to make sure the new reminder is saved to the database
                    // before trying to fetch it
                    Thread.sleep(1000)
                    // Make notifications for the reminder according to user specs. If no notification time
                    // is given, makeReminderRequest is not executed.
                    viewModel.getNewestReminder { reminder ->
                        reminder?.let {
                            if (reminder.reminderTimes?.isNotEmpty() == true) {
                                makeNewNotification(context, reminder)
                                Log.d("NEW REMINDER", "Making notification for the reminder $reminder")
                            }
                        }
                    }
                    onBack()
                },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .size(55.dp)
            )
            {
                Text("Save reminder")
            }
        }
    }
}