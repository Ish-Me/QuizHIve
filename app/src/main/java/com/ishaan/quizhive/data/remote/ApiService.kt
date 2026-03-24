package com.ishaan.quizhive.data.remote

import com.ishaan.quizhive.data.remote.dto.QuizResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): QuizResponseDto
}