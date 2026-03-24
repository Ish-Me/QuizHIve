package com.ishaan.quizhive.data.repository


import android.text.Html
import com.ishaan.quizhive.data.local.dao.ScoreDao
import com.ishaan.quizhive.data.local.entity.ScoreEntity
import com.ishaan.quizhive.data.remote.ApiService
import com.ishaan.quizhive.domain.model.Question
import com.ishaan.quizhive.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val scoreDao: ScoreDao
) : QuizRepository {

    override suspend fun getQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?
    ): Result<List<Question>> = runCatching {

        val response = apiService.getQuestions(
            amount = amount,
            category = category,
            difficulty = difficulty?.lowercase(),
            type = "multiple"
        )

        // ✅ FIX 2: Handle API response_code
        if (response.responseCode != 0) {
            return@runCatching emptyList()
        }

        response.results.map { dto ->
            Question(
                id = UUID.randomUUID().toString(),
                category = Html.fromHtml(dto.category, Html.FROM_HTML_MODE_LEGACY).toString(),
                question = Html.fromHtml(dto.question, Html.FROM_HTML_MODE_LEGACY).toString(),
                correctAnswer = Html.fromHtml(dto.correctAnswer, Html.FROM_HTML_MODE_LEGACY).toString(),
                shuffledOptions = (dto.incorrectAnswers + dto.correctAnswer)
                    .map { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString() }
                    .shuffled(),
                difficulty = dto.difficulty
            )
        }
    }

    override suspend fun saveScore(score: ScoreEntity) = scoreDao.insert(score)
    override fun getTopScores(): Flow<List<ScoreEntity>> = scoreDao.getTopScores()
}