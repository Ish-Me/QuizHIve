package com.ishaan.quizhive.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class OptionState { UNANSWERED, CORRECT, WRONG, CORRECT_ANSWER }

@Composable
fun OptionButton(text: String, state: OptionState, onClick: () -> Unit) {

    val green = Color(0xFF2E7D32)        // dark green
    val greenBg = Color(0xFFE8F5E9)      // light green background
    val red = Color(0xFFC62828)          // dark red
    val redBg = Color(0xFFFFEBEE)        // light red background
    val orangeBg = Color(0xFFFFF3E0)     // light orange background for correct answer reveal
    val orange = Color(0xFFE65100)       // dark orange text

    val containerColor by animateColorAsState(
        targetValue = when (state) {
            OptionState.CORRECT        -> greenBg
            OptionState.WRONG          -> redBg
            OptionState.CORRECT_ANSWER -> orangeBg
            OptionState.UNANSWERED     -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 300),
        label = "containerColor"
    )

    val contentColor = when (state) {
        OptionState.CORRECT        -> green
        OptionState.WRONG          -> red
        OptionState.CORRECT_ANSWER -> orange
        OptionState.UNANSWERED     -> MaterialTheme.colorScheme.onSurface
    }

    val borderColor = when (state) {
        OptionState.CORRECT        -> green
        OptionState.WRONG          -> red
        OptionState.CORRECT_ANSWER -> orange
        OptionState.UNANSWERED     -> MaterialTheme.colorScheme.outline
    }

    val fontWeight = when (state) {
        OptionState.UNANSWERED -> FontWeight.Normal
        else                   -> FontWeight.SemiBold
    }

    // Label shown below option text to explain the state
    val label = when (state) {
        OptionState.CORRECT        -> "Correct!"
        OptionState.WRONG          -> "Wrong!"
        OptionState.CORRECT_ANSWER -> "Correct answer"
        OptionState.UNANSWERED     -> null
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (state == OptionState.UNANSWERED) 1.dp else 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        enabled = state == OptionState.UNANSWERED
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = text,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.bodyLarge
            )
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
        }
    }
}