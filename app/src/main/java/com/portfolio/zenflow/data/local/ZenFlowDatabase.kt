package com.portfolio.zenflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
abstract class ZenFlowDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
}
