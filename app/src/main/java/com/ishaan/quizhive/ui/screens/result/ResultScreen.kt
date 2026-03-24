package com.ishaan.quizhive.ui.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ishaan.quizhive.ui.screens.quiz.QuizViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    timeTaken: Long,
    onRestart: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {

    val topScores by viewModel.topScores.collectAsStateWithLifecycle(emptyList())

    val percentage = if (total > 0)
        (score.toFloat() / total * 100).toInt()
    else 0
    val minutes = timeTaken / 60000
    val seconds = (timeTaken % 60000) / 1000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(30.dp))

        // 🎉 RESULT TITLE
        Text(
            text = when {
                percentage >= 80 -> "Excellent 🎉"
                percentage >= 60 -> "Great Job 👏"
                percentage >= 40 -> "Keep Practicing 💪"
                else -> "Try Again 😅"
            },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        // 🟢 SCORE CIRCLE CARD
        Card(
            modifier = Modifier.size(170.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "$score / $total",
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "$percentage%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ⏱ TIME TAKEN
        Text(
            "Completed in ${minutes}m ${seconds}s",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(28.dp))

        // 🏆 TOP SCORES CARD
        if (topScores.isNotEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Top Scores",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    topScores.take(5).forEachIndexed { index, scoreEntity ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                "#${index + 1}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                "${scoreEntity.score}/${scoreEntity.total}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                SimpleDateFormat("MMM dd", Locale.getDefault())
                                    .format(Date(scoreEntity.timestamp)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (index != 4) HorizontalDivider()
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        // 🚀 PLAY AGAIN BUTTON
        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Play Again",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}