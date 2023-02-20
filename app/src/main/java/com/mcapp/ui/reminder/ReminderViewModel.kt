package com.mcapp.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcapp.data.entity.Reminder
import com.mcapp.data.repository.ReminderRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReminderViewModel(private val reminderRepositoryImpl: ReminderRepositoryImpl): ViewModel() {
    private val _viewReminders = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)
    val reminders: StateFlow<ReminderViewState> = _viewReminders

    fun insertOrUpdateReminder(reminder: Reminder) {
        viewModelScope.launch { reminderRepositoryImpl.insertOrUpdate(reminder) }
    }

    // TODO make a notification for new reminder successfully made

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch { reminderRepositoryImpl.delete(reminder) }
    }

    fun getListOfAllReminders(creatorId: Long) {
        viewModelScope.launch {
            reminderRepositoryImpl.getAllReminders(creatorId).map {
                _viewReminders.value = ReminderViewState.Success(it)
            } }
    }
}