package com.portfolio.zenflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.portfolio.zenflow.domain.model.Habit

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val isCompletedToday: Boolean,
    val streakDays: Int,
    val accentHex: Long
)

fun HabitEntity.toDomain() = Habit(
    id = id,
    title = title,
    subtitle = subtitle,
    iconName = iconName,
    isCompletedToday = isCompletedToday,
    streakDays = streakDays,
    accentHex = accentHex
)

fun Habit.toEntity() = HabitEntity(
    id = id,
    title = title,
    subtitle = subtitle,
    iconName = iconName,
    isCompletedToday = isCompletedToday,
    streakDays = streakDays,
    accentHex = accentHex
)
