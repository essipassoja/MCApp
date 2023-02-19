package com.mcapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcapp.data.entity.Reminder
import com.mcapp.data.room.ReminderDataSourceImpl
import com.mcapp.data.room.ReminderViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReminderViewModel(private val reminderDataSource: ReminderDataSourceImpl): ViewModel() {
    private val _viewReminders = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)
    val reminders: StateFlow<ReminderViewState> = _viewReminders

    fun insertOrUpdateReminder(reminder: Reminder) {
        viewModelScope.launch { reminderDataSource.insertOrUpdate(reminder) }
    }

    // TODO make a notification for new reminder successfully made

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch { reminderDataSource.delete(reminder) }
    }

    fun getListOfAllReminders(creatorId: Long) {
        viewModelScope.launch {
            reminderDataSource.getAllReminders(creatorId).map {
                _viewReminders.value = ReminderViewState.Success(it)
            } }
    }
}