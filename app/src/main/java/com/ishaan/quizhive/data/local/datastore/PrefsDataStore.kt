package com.ishaan.quizhive.data.local.datastore



import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "quiz_prefs")

@Singleton
class PrefsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val CATEGORY_KEY = intPreferencesKey("category")
        val DIFFICULTY_KEY = stringPreferencesKey("difficulty")
    }

    val savedCategory: Flow<Int?> = context.dataStore.data
        .map { it[CATEGORY_KEY] }

    val savedDifficulty: Flow<String?> = context.dataStore.data
        .map { it[DIFFICULTY_KEY] }

    suspend fun saveCategory(category: Int?) {
        context.dataStore.edit { prefs ->
            if (category != null) prefs[CATEGORY_KEY] = category
            else prefs.remove(CATEGORY_KEY)
        }
    }

    suspend fun saveDifficulty(difficulty: String?) {
        context.dataStore.edit { prefs ->
            if (difficulty != null) prefs[DIFFICULTY_KEY] = difficulty
            else prefs.remove(DIFFICULTY_KEY)
        }
    }
}