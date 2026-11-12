package com.portfolio.zenflow.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "zenflow_prefs")

@Singleton
class FocusPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val focusMinutesKey = intPreferencesKey("today_focus_minutes")
    private val isDarkModeKey = booleanPreferencesKey("is_dark_mode")

    val todayFocusMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[focusMinutesKey] ?: 0
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[isDarkModeKey] ?: false // Default Warm Minimal Light Mode
    }

    suspend fun addFocusMinutes(minutes: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[focusMinutesKey] ?: 0
            prefs[focusMinutesKey] = current + minutes
        }
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[isDarkModeKey] = isDark
        }
    }
}
