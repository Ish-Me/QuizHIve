package com.ishaan.quizhive.ui.screens.quiz

import com.ishaan.quizhive.data.local.entity.ScoreEntity
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishaan.quizhive.domain.model.Question
import com.ishaan.quizhive.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    // Sealed class for clean UI state management
    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Active(
            val questions: List<Question>,
            val currentIndex: Int,
            val selectedAnswer: String?,
            val isAnswered: Boolean,
            val score: Int,
            val timeLeft: Int       // 15-second timer per question
        ) : UiState()
        data class Result(val score: Int, val total: Int, val timeTaken: Long) : UiState()
        data class Error(val message: String) : UiState()
    }
    val topScores: Flow<List<ScoreEntity>> = repository.getTopScores()
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTime = 0L

    fun loadQuiz(category: Int? = null, difficulty: String? = null) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getQuestions(10, category, difficulty)
                .onSuccess { questions ->

                    if (questions.isEmpty()) {
                        _uiState.value = UiState.Error("No questions found for selected options")
                        return@launch
                    }
                    startTime = System.currentTimeMillis()
                    _uiState.value = UiState.Active(
                        questions = questions,
                        currentIndex = 0,
                        selectedAnswer = null,
                        isAnswered = false,
                        score = 0,
                        timeLeft = 15
                    )

                    startTimer()
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value as? UiState.Active ?: return
        if (state.isAnswered) return
        timerJob?.cancel()
        val isCorrect = answer == state.questions[state.currentIndex].correctAnswer
        _uiState.value = state.copy(
            selectedAnswer = answer,
            isAnswered = true,
            score = if (isCorrect) state.score + 1 else state.score
        )
    }

    fun nextQuestion() {
        val state = _uiState.value as? UiState.Active ?: return
        if (state.currentIndex + 1 >= state.questions.size) {
            val timeTaken = System.currentTimeMillis() - startTime
            viewModelScope.launch {
                repository.saveScore(
                    ScoreEntity(score = state.score, total = state.questions.size,
                        timestamp = System.currentTimeMillis())
                )
            }
            _uiState.value = UiState.Result(state.score, state.questions.size, timeTaken)
        } else {
            _uiState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedAnswer = null,
                isAnswered = false,
                timeLeft = 15
            )
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 15 downTo 0) {
                val state = _uiState.value as? UiState.Active ?: break
                _uiState.value = state.copy(timeLeft = i)
                if (i == 0) { selectAnswer("") ; break }
                delay(1000)
            }
        }
    }

    fun resetQuiz() { _uiState.value = UiState.Idle }
}