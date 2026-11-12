package com.portfolio.zenflow.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, subtitle: String, iconName: String, colorHex: Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("water") }
    val colors = listOf(0xFF6B9080, 0xFF38BDF8, 0xFFE07A5F, 0xFF818CF8, 0xFFF59E0B)
    var selectedColor by remember { mutableLongStateOf(colors.first()) }
    val icons = listOf("water" to "Hydrate", "book" to "Read", "walk" to "Move", "meditate" to "Mindful")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "New Daily Habit",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Habit Name (e.g., Morning Stretch)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Goal / Note (e.g., 15 mins daily)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                icons.forEach { (key, label) ->
                    FilterChip(
                        selected = selectedIcon == key,
                        onClick = { selectedIcon = key },
                        label = { Text(label) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                colors.forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(hex))
                            .border(
                                width = if (selectedColor == hex) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = hex }
                    )
                }
            }

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title.trim(), subtitle.ifBlank { "Daily ritual" }, selectedIcon, selectedColor)
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Create Habit", fontWeight = FontWeight.Bold)
            }
        }
    }
}
