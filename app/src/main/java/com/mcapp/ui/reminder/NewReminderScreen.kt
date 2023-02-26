package com.mcapp.ui.reminder

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mcapp.data.entity.Reminder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun MakeNewReminder(viewModel: ReminderViewModel, onBack: () -> Unit) {
    val message = remember { mutableStateOf("") }
    val reminderTime = remember { mutableStateOf(LocalDateTime.now()) }
    val dateState = remember { mutableStateOf(reminderTime.value.toLocalDate()) }
    val timeState = remember { mutableStateOf(reminderTime.value.toLocalTime()) }

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
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
        Text(text = "Reminder time:")
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
        DatePicker(
            selectedDate = dateState.value,
            onDateChange = { date ->
                dateState.value = date
                reminderTime.value = LocalDateTime.of(date, timeState.value)
            },
            modifier = Modifier.padding(2.dp)
        )
        TimePicker(
            selectedTime = timeState.value,
            onTimeChange = {time ->
                timeState.value = time
                reminderTime.value = LocalDateTime.of(dateState.value, time)
            },
            modifier = Modifier.padding(2.dp)
        )
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
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

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            DatePicker(context).apply {
                init(
                    selectedDate.year,
                    selectedDate.monthValue - 1,
                    selectedDate.dayOfMonth
                ) { _, year, monthOfYear, dayOfMonth ->
                    val newDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                    onDateChange(newDate)
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun TimePicker(
    selectedTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            val timePickerState = mutableStateOf(selectedTime)
            TimePicker(context).apply {
                setIs24HourView(true)
                hour = timePickerState.value.hour
                minute = timePickerState.value.minute
                setOnTimeChangedListener { _, hourOfDay, minute ->
                    timePickerState.value = LocalTime.of(hourOfDay, minute)
                    onTimeChange(timePickerState.value)
                }
            }
        },
        update = { view ->
            val timePickerState = mutableStateOf(selectedTime)
            view.apply {
                setIs24HourView(true)
                hour = timePickerState.value.hour
                minute = timePickerState.value.minute
                setOnTimeChangedListener { _, hourOfDay, minute ->
                    timePickerState.value = LocalTime.of(hourOfDay, minute)
                    onTimeChange(timePickerState.value)
                }
            }
        },
        modifier = modifier
    )
}