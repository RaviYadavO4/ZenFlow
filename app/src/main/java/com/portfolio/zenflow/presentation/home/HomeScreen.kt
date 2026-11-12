package com.portfolio.zenflow.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portfolio.zenflow.presentation.components.AddHabitSheet
import com.portfolio.zenflow.presentation.components.CircularTimerSection
import com.portfolio.zenflow.presentation.components.HabitItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val completedCount = state.habits.count { it.isCompletedToday }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ZenFlow",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${state.todayFocusMinutes}m focused today  •  $completedCount/${state.habits.size} habits",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(HomeEvent.ToggleTheme) },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = if (state.isDarkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                CircularTimerSection(
                    selectedMode = state.selectedMode,
                    formattedTime = state.formattedTime,
                    progress = state.timerProgress,
                    isRunning = state.isTimerRunning,
                    onSelectMode = { viewModel.onEvent(HomeEvent.SelectMode(it)) },
                    onToggleTimer = { viewModel.onEvent(HomeEvent.ToggleTimer) },
                    onResetTimer = { viewModel.onEvent(HomeEvent.ResetTimer) }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Rituals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    FilledTonalButton(
                        onClick = { viewModel.onEvent(HomeEvent.OpenAddHabitSheet) },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Habit", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            items(items = state.habits, key = { it.id }) { habit ->
                HabitItemCard(
                    habit = habit,
                    onToggle = { viewModel.onEvent(HomeEvent.ToggleHabit(habit)) }
                )
            }
        }

        if (state.isAddHabitSheetOpen) {
            AddHabitSheet(
                onDismiss = { viewModel.onEvent(HomeEvent.CloseAddHabitSheet) },
                onSave = { title, subtitle, icon, color ->
                    viewModel.onEvent(HomeEvent.AddHabit(title, subtitle, icon, color))
                }
            )
        }
    }
}
