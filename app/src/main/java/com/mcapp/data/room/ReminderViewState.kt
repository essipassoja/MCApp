package com.mcapp.data.room

import com.mcapp.data.entity.Reminder

sealed interface ReminderViewState {
    object Loading: ReminderViewState
    data class Success(
        val data: List<Reminder>
    ): ReminderViewState
}
