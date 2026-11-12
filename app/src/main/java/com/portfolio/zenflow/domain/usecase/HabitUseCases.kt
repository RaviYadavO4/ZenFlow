package com.portfolio.zenflow.domain.usecase

import com.portfolio.zenflow.domain.model.Habit
import com.portfolio.zenflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class HabitUseCases(
    val getHabits: GetHabitsUseCase,
    val addHabit: AddHabitUseCase,
    val toggleHabit: ToggleHabitUseCase,
    val deleteHabit: DeleteHabitUseCase
)

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> = repository.getAllHabits()
}

class AddHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        if (habit.title.isBlank()) return
        repository.upsertHabit(habit)
    }
}

class ToggleHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        val updatedStatus = !habit.isCompletedToday
        val updatedStreak = if (updatedStatus) habit.streakDays + 1 else (habit.streakDays - 1).coerceAtLeast(0)
        repository.upsertHabit(
            habit.copy(
                isCompletedToday = updatedStatus,
                streakDays = updatedStreak
            )
        )
    }
}

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        repository.deleteHabit(habit)
    }
}
