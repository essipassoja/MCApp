package com.mcapp.ui.reminder

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DatePickerDialog(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onBack: () -> Unit
) {
    val dialogState = remember { mutableStateOf(DialogState()) }

    AlertDialog(
        onDismissRequest = { dialogState.value = DialogState() },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    dialogState.value = DialogState()
                    onBack()
                }) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = {
                    onDateChange(dialogState.value.selectedDate)
                    dialogState.value = DialogState()
                    onBack()
                }) {
                    Text(text = "OK")
                }
            }
        },
        text = {
            AndroidView(
                factory = { context ->
                    DatePicker(context).apply {
                        init(
                            selectedDate.year,
                            selectedDate.monthValue - 1,
                            selectedDate.dayOfMonth
                        ) { view, year, monthOfYear, dayOfMonth ->
                            dialogState.value =
                                dialogState.value.copy(
                                    selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                                )
                        }
                    }
                }
            )
        }
    )

}

@Composable
fun TimePickerDialog(
    selectedTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    onBack: () -> Unit
) {
    val dialogState = remember { mutableStateOf(DialogState()) }

    AlertDialog(
        onDismissRequest = { dialogState.value = DialogState() },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    dialogState.value = DialogState()
                    onBack()
                }) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = {
                    onTimeChange(dialogState.value.selectedTime)
                    dialogState.value = DialogState()
                    onBack()
                }) {
                    Text(text = "OK")
                }
            }
        },
        text = {
            AndroidView(
                factory = { context ->
                    TimePicker(context).apply {
                        setIs24HourView(true)
                        hour = selectedTime.hour
                        minute = selectedTime.minute
                        setOnTimeChangedListener { _, hourOfDay, minute ->
                            dialogState.value =
                                dialogState.value.copy(
                                    selectedTime = LocalTime.of(
                                        hourOfDay,
                                        minute
                                    )
                                )
                        }
                    }
                }
            )
        }
    )
}

private data class DialogState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
)