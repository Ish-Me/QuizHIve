package com.ishaan.quizhive.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ishaan.quizhive.ui.components.OptionButton
import com.ishaan.quizhive.ui.components.OptionState
import com.ishaan.quizhive.ui.components.TimerBar

@Composable
fun QuizScreen(
    navController: NavController,
    category: Int? = null,
    difficulty: String? = null,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Pass category and difficulty to loadQuiz
    LaunchedEffect(category, difficulty) {
        viewModel.loadQuiz(category = category, difficulty = difficulty)
    }

    when (val state = uiState) {
        is QuizViewModel.UiState.Loading -> LoadingContent()
        is QuizViewModel.UiState.Error   -> ErrorContent(state.message) {
            viewModel.loadQuiz(category = category, difficulty = difficulty)
        }
        is QuizViewModel.UiState.Active  -> ActiveQuizContent(state, viewModel)
        is QuizViewModel.UiState.Result  -> {
            LaunchedEffect(Unit) {
                navController.navigate("result/${state.score}/${state.total}/${state.timeTaken}")
            }
        }
        else -> Unit
    }
}

@Composable
fun ActiveQuizContent(state: QuizViewModel.UiState.Active, viewModel: QuizViewModel) {

    val progress = (state.currentIndex + 1).toFloat() / state.questions.size.toFloat()
    val question = state.questions[state.currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔝 TOP BAR (Progress + Score)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(12.dp)) {

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${state.currentIndex + 1} / ${state.questions.size}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "Score: ${state.score}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ⏱ TIMER
        TimerBar(timeLeft = state.timeLeft, maxTime = 15)

        Spacer(Modifier.height(20.dp))

        // 📌 CATEGORY
        AssistChip(
            onClick = {},
            label = { Text(question.category) }
        )

        Spacer(Modifier.height(16.dp))

        // ❓ QUESTION CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // 🎯 OPTIONS (Wrapped in a Card for cleaner grouping)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(12.dp)) {

                question.shuffledOptions.forEach { option ->

                    OptionButton(
                        text = option,
                        state = when {
                            !state.isAnswered -> OptionState.UNANSWERED
                            option == question.correctAnswer
                                    && state.selectedAnswer == option -> OptionState.CORRECT
                            option == question.correctAnswer
                                    && state.selectedAnswer != option -> OptionState.CORRECT_ANSWER
                            option == state.selectedAnswer -> OptionState.WRONG
                            else -> OptionState.UNANSWERED
                        },
                        onClick = { viewModel.selectAnswer(option) }
                    )

                    Spacer(Modifier.height(10.dp))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // 🚀 NEXT BUTTON
        if (state.isAnswered) {
            Button(
                onClick = { viewModel.nextQuestion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    if (state.currentIndex + 1 < state.questions.size)
                        "Next Question"
                    else
                        "See Result",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
@Composable
fun LoadingContent() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text("Loading Quiz...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Something went wrong",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Retry")
        }
    }
}