package com.portfolio.zenflow.data.repository

import com.portfolio.zenflow.data.local.HabitDao
import com.portfolio.zenflow.data.local.toDomain
import com.portfolio.zenflow.data.local.toEntity
import com.portfolio.zenflow.domain.model.Habit
import com.portfolio.zenflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val dao: HabitDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> {
        return dao.getAllHabits().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun upsertHabit(habit: Habit) {
        dao.insertHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        dao.deleteHabit(habit.toEntity())
    }
}
