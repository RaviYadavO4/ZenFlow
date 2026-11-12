package com.portfolio.zenflow.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.zenflow.data.local.FocusPreferences
import com.portfolio.zenflow.domain.model.FocusMode
import com.portfolio.zenflow.domain.model.Habit
import com.portfolio.zenflow.domain.usecase.HabitUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HabitUseCases,
    private val preferences: FocusPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var seededInitialHabits = false

    init {
        observeData()
    }

    private fun observeData() {
        combine(
            useCases.getHabits(),
            preferences.todayFocusMinutes,
            preferences.isDarkMode
        ) { habits, focusMins, isDark ->
            if (habits.isEmpty() && !seededInitialHabits) {
                seededInitialHabits = true
                seedDefaultHabits()
            }
            _uiState.update { state ->
                state.copy(
                    habits = habits,
                    todayFocusMinutes = focusMins,
                    isDarkMode = isDark
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun seedDefaultHabits() {
        viewModelScope.launch {
            useCases.addHabit(Habit(title = "Hydrate 2.5L", subtitle = "8 glasses of water", iconName = "water", streakDays = 4, accentHex = 0xFF38BDF8))
            useCases.addHabit(Habit(title = "Deep Reading", subtitle = "20 pages non-fiction", iconName = "book", streakDays = 7, accentHex = 0xFF6B9080))
            useCases.addHabit(Habit(title = "Mindful Walk", subtitle = "15 mins outdoors", iconName = "walk", streakDays = 2, accentHex = 0xFFE07A5F))
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectMode -> {
                timerJob?.cancel()
                val totalSecs = event.mode.durationMinutes * 60
                _uiState.update {
                    it.copy(
                        selectedMode = event.mode,
                        totalSeconds = totalSecs,
                        remainingSeconds = totalSecs,
                        isTimerRunning = false
                    )
                }
            }
            is HomeEvent.ToggleTimer -> {
                if (_uiState.value.isTimerRunning) pauseTimer() else startTimer()
            }
            is HomeEvent.ResetTimer -> {
                timerJob?.cancel()
                val totalSecs = _uiState.value.selectedMode.durationMinutes * 60
                _uiState.update {
                    it.copy(remainingSeconds = totalSecs, isTimerRunning = false)
                }
            }
            is HomeEvent.ToggleHabit -> {
                viewModelScope.launch { useCases.toggleHabit(event.habit) }
            }
            is HomeEvent.AddHabit -> {
                viewModelScope.launch {
                    useCases.addHabit(
                        Habit(
                            title = event.title,
                            subtitle = event.subtitle,
                            iconName = event.iconName,
                            accentHex = event.colorHex
                        )
                    )
                    _uiState.update { it.copy(isAddHabitSheetOpen = false) }
                }
            }
            is HomeEvent.DeleteHabit -> {
                viewModelScope.launch { useCases.deleteHabit(event.habit) }
            }
            is HomeEvent.OpenAddHabitSheet -> {
                _uiState.update { it.copy(isAddHabitSheetOpen = true) }
            }
            is HomeEvent.CloseAddHabitSheet -> {
                _uiState.update { it.copy(isAddHabitSheetOpen = false) }
            }
            is HomeEvent.ToggleTheme -> {
                viewModelScope.launch { preferences.setDarkMode(!_uiState.value.isDarkMode) }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0) {
                delay(1000L)
                _uiState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            // Timer Completed
            val completedMode = _uiState.value.selectedMode
            if (completedMode == FocusMode.FOCUS) {
                preferences.addFocusMinutes(completedMode.durationMinutes)
            }
            val resetSecs = completedMode.durationMinutes * 60
            _uiState.update {
                it.copy(isTimerRunning = false, remainingSeconds = resetSecs)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }
}
