package com.ishaan.quizhive.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuizResponseDto(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("results") val results: List<QuestionDto>
)

data class QuestionDto(
    @SerializedName("category") val category: String,
    @SerializedName("question") val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>,
    @SerializedName("difficulty") val difficulty: String
)