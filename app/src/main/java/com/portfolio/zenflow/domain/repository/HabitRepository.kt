package com.portfolio.zenflow.domain.repository

import com.portfolio.zenflow.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun upsertHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
}
