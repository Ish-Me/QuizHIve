package com.ishaan.quizhive.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimerBar(timeLeft: Int, maxTime: Int) {
    val progress = timeLeft.toFloat() / maxTime.toFloat()
    val color = when {
        progress > 0.5f -> MaterialTheme.colorScheme.primary
        progress > 0.25f -> Color(0xFFFFA000)  // amber
        else -> MaterialTheme.colorScheme.error
    }
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(6.dp),
        color = color
    )
}