package com.portfolio.zenflow.di

import android.app.Application
import androidx.room.Room
import com.portfolio.zenflow.data.local.ZenFlowDatabase
import com.portfolio.zenflow.data.repository.HabitRepositoryImpl
import com.portfolio.zenflow.domain.repository.HabitRepository
import com.portfolio.zenflow.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ZenFlowDatabase {
        return Room.databaseBuilder(
            app,
            ZenFlowDatabase::class.java,
            "zenflow_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(db: ZenFlowDatabase): HabitRepository {
        return HabitRepositoryImpl(db.habitDao)
    }

    @Provides
    @Singleton
    fun provideHabitUseCases(repository: HabitRepository): HabitUseCases {
        return HabitUseCases(
            getHabits = GetHabitsUseCase(repository),
            addHabit = AddHabitUseCase(repository),
            toggleHabit = ToggleHabitUseCase(repository),
            deleteHabit = DeleteHabitUseCase(repository)
        )
    }
}
