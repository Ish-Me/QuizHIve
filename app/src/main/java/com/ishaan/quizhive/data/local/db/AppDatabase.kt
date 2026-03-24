package com.ishaan.quizhive.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ishaan.quizhive.data.local.dao.ScoreDao
import com.ishaan.quizhive.data.local.entity.ScoreEntity

@Database(
    entities = [ScoreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}