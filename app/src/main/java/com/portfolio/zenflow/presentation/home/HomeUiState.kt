package com.portfolio.zenflow.presentation.home

import com.portfolio.zenflow.domain.model.FocusMode
import com.portfolio.zenflow.domain.model.Habit

data class HomeUiState(
    val selectedMode: FocusMode = FocusMode.FOCUS,
    val totalSeconds: Int = FocusMode.FOCUS.durationMinutes * 60,
    val remainingSeconds: Int = FocusMode.FOCUS.durationMinutes * 60,
    val isTimerRunning: Boolean = false,
    val todayFocusMinutes: Int = 0,
    val habits: List<Habit> = emptyList(),
    val isDarkMode: Boolean = false,
    val isAddHabitSheetOpen: Boolean = false
) {
    val timerProgress: Float
        get() = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f

    val formattedTime: String
        get() {
            val mins = remainingSeconds / 60
            val secs = remainingSeconds % 60
            return "%02d:%02d".format(mins, secs)
        }
}

sealed interface HomeEvent {
    data class SelectMode(val mode: FocusMode) : HomeEvent
    data object ToggleTimer : HomeEvent
    data object ResetTimer : HomeEvent
    data class ToggleHabit(val habit: Habit) : HomeEvent
    data class AddHabit(val title: String, val subtitle: String, val iconName: String, val colorHex: Long) : HomeEvent
    data class DeleteHabit(val habit: Habit) : HomeEvent
    data object OpenAddHabitSheet : HomeEvent
    data object CloseAddHabitSheet : HomeEvent
    data object ToggleTheme : HomeEvent
}
