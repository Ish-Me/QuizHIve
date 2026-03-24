package com.ishaan.quizhive.domain.model

data class Question(
    val id: String,
    val category: String,
    val question: String,
    val correctAnswer: String,
    val shuffledOptions: List<String>,
    val difficulty: String
)