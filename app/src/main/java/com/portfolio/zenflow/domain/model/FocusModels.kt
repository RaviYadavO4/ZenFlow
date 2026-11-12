package com.portfolio.zenflow.domain.model

enum class FocusMode(
    val label: String,
    val durationMinutes: Int,
    val accentHex: Long
) {
    FOCUS("Deep Focus", 25, 0xFF6B9080),       // Calm Sage Green
    SHORT_BREAK("Short Break", 5, 0xFFE07A5F), // Warm Terracotta
    LONG_BREAK("Long Break", 15, 0xFF818CF8)   // Soft Periwinkle
}

data class Habit(
    val id: Int = 0,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val isCompletedToday: Boolean = false,
    val streakDays: Int = 0,
    val accentHex: Long = 0xFF6B9080
)
