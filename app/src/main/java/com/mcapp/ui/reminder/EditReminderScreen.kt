package com.mcapp.ui.reminder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mcapp.data.entity.Reminder
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditOrDeleteReminder(viewModel: ReminderViewModel, reminder: Reminder, onBack: () -> Unit) {
    val message = remember { mutableStateOf(reminder.message) }

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
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))

        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(199, 156, 199, 255),
                contentColor = Color(255, 255, 255)),
            onClick = {
                viewModel.insertOrUpdateReminder(
                    Reminder(
                        message = message.value,
                        locationX = 0,
                        locationY = 0,
                        reminderTime = LocalDateTime.of(
                            2023, 2, 15, 19, 29),
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
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))

        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(199, 50, 19, 255),
                contentColor = Color(255, 255, 255)),
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
