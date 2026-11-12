package com.portfolio.zenflow.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.portfolio.zenflow.domain.model.Habit

@Composable
fun HabitItemCard(
    habit: Habit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = Color(habit.accentHex)
    val checkBg by animateColorAsState(
        targetValue = if (habit.isCompletedToday) accent else MaterialTheme.colorScheme.surfaceVariant,
        label = "habitCheckColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (habit.isCompletedToday) accent.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Habit Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getHabitIcon(habit.iconName),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title, Subtitle & Streak
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (habit.isCompletedToday) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (habit.isCompletedToday) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = habit.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (habit.streakDays > 0) {
                        Text(
                            text = "  •  🔥 ${habit.streakDays}d streak",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE07A5F)
                        )
                    }
                }
            }

            // Animated Check Circle
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(checkBg),
                contentAlignment = Alignment.Center
            ) {
                if (habit.isCompletedToday) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

fun getHabitIcon(iconName: String): ImageVector {
    return when (iconName) {
        "water" -> Icons.Rounded.WaterDrop
        "book" -> Icons.Rounded.AutoStories
        "walk" -> Icons.Rounded.DirectionsWalk
        "meditate" -> Icons.Rounded.SelfImprovement
        else -> Icons.Rounded.CheckCircleOutline
    }
}
