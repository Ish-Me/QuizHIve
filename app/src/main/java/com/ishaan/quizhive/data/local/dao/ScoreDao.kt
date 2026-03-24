package com.ishaan.quizhive.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.ishaan.quizhive.data.local.entity.ScoreEntity

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<ScoreEntity>>
}