package com.mcapp.ui.reminder

import GetLocation
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.mcapp.data.entity.Reminder
import com.mcapp.data.repository.ReminderRepository
import com.mcapp.util.calculateDistance
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent

@Composable
fun SelectLocationAndFindNearbyReminders(
    onBack: () -> Unit
) {
    val selectedLocation = remember { mutableStateOf(LatLng(0.0, 0.0)) }
    GetLocation(
        onBack = {
            location ->
            selectedLocation.value = location
            }
    )
    Log.d("SET VIRTUAL LOCATION", "User has selected location $selectedLocation")
    Box(modifier = Modifier.fillMaxSize()) {
        ShowRemindersNearLocation(
            selectedLocation = selectedLocation.value,
            onBack = { onBack() }
        )
    }
}

@Composable
fun ShowRemindersNearLocation(
    selectedLocation: LatLng,
    onBack: () -> Unit) {

    val reminders = getRemindersFromDatabase()

    val nearbyReminders = mutableListOf<Reminder>()
    for (reminder in reminders) {
        if (reminder.locationX != null && reminder.locationY != null) {
            if (calculateDistance(LatLng(reminder.locationX, reminder.locationY), selectedLocation) < 1000) {
                nearbyReminders.add(reminder)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Reminders near selected location:")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(nearbyReminders) { reminder ->


                Text(text = reminder.message)
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Text(text = "Go back")
        }
    }
}

fun getRemindersFromDatabase(): List<Reminder> {
    // Retrieve reminder locations from Room database
    val reminderRepository = KoinJavaComponent.getKoin().get<ReminderRepository>()

    var reminderList = listOf<Reminder>()

    runBlocking {
        reminderRepository.getAllReminders(0).collect { reminders ->
            reminderList = reminders
        }
    }
    Log.d("TEST", "$reminderList")
    return reminderList
}