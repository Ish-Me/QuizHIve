package com.ishaan.quizhive.domain.repository

import com.ishaan.quizhive.data.local.entity.ScoreEntity
import com.ishaan.quizhive.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun getQuestions(
        amount: Int, category: Int?, difficulty: String?
    ): Result<List<Question>>
    suspend fun saveScore(score: ScoreEntity)
    fun getTopScores(): Flow<List<ScoreEntity>>
}