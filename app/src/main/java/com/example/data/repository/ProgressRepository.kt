package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.LearningLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProgressRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("shikhi_kids_progress", Context.MODE_PRIVATE)

    private val _stars = MutableStateFlow(prefs.getInt(KEY_STARS, 5)) // Start with 5 welcome stars!
    val stars: StateFlow<Int> = _stars.asStateFlow()

    private val _streakDays = MutableStateFlow(prefs.getInt(KEY_STREAK, 1))
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _completedLessons = MutableStateFlow(
        prefs.getStringSet(KEY_COMPLETED_LESSONS, emptySet()) ?: emptySet()
    )
    val completedLessons: StateFlow<Set<String>> = _completedLessons.asStateFlow()

    private val _learningLevel = MutableStateFlow(
        try {
            LearningLevel.valueOf(
                prefs.getString(KEY_LEARNING_LEVEL, LearningLevel.EARLY_LEARNER.name)
                    ?: LearningLevel.EARLY_LEARNER.name
            )
        } catch (_: Exception) {
            LearningLevel.EARLY_LEARNER
        }
    )
    val learningLevel: StateFlow<LearningLevel> = _learningLevel.asStateFlow()

    private val _interfaceLanguage = MutableStateFlow(
        prefs.getString(KEY_LANGUAGE, "bn") ?: "bn"
    )
    val interfaceLanguage: StateFlow<String> = _interfaceLanguage.asStateFlow()

    private val _soundEnabled = MutableStateFlow(prefs.getBoolean(KEY_SOUND, true))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _ttsEnabled = MutableStateFlow(prefs.getBoolean(KEY_TTS, true))
    val ttsEnabled: StateFlow<Boolean> = _ttsEnabled.asStateFlow()

    init {
        checkAndUpdateStreak()
    }

    private fun checkAndUpdateStreak() {
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastDateStr = prefs.getString(KEY_LAST_ACTIVE_DATE, "")

        if (lastDateStr != todayStr) {
            val currentStreak = prefs.getInt(KEY_STREAK, 1)
            // check if last active was yesterday
            val yesterdayCal = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.DAY_OF_YEAR, -1)
            }
            val yesterdayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterdayCal.time)

            val newStreak = if (lastDateStr == yesterdayStr) {
                currentStreak + 1
            } else if (lastDateStr.isNullOrEmpty()) {
                1
            } else {
                1
            }

            prefs.edit()
                .putString(KEY_LAST_ACTIVE_DATE, todayStr)
                .putInt(KEY_STREAK, newStreak)
                .apply()
            _streakDays.value = newStreak
        }
    }

    fun addStarsForLesson(lessonId: String, amount: Int = 1): Boolean {
        val currentSet = _completedLessons.value
        val isFirstTime = !currentSet.contains(lessonId)

        val newCompleted = currentSet + lessonId
        _completedLessons.value = newCompleted

        val newStars = _stars.value + amount
        _stars.value = newStars

        prefs.edit()
            .putInt(KEY_STARS, newStars)
            .putStringSet(KEY_COMPLETED_LESSONS, newCompleted)
            .apply()

        return isFirstTime
    }

    fun setLearningLevel(level: LearningLevel) {
        _learningLevel.value = level
        prefs.edit().putString(KEY_LEARNING_LEVEL, level.name).apply()
    }

    fun setInterfaceLanguage(language: String) {
        _interfaceLanguage.value = language
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun setSoundEnabled(enabled: Boolean) {
        _soundEnabled.value = enabled
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
    }

    fun setTtsEnabled(enabled: Boolean) {
        _ttsEnabled.value = enabled
        prefs.edit().putBoolean(KEY_TTS, enabled).apply()
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
        _stars.value = 0
        _streakDays.value = 1
        _completedLessons.value = emptySet()
        _learningLevel.value = LearningLevel.EARLY_LEARNER
        _interfaceLanguage.value = "bn"
        _soundEnabled.value = true
        _ttsEnabled.value = true
        checkAndUpdateStreak()
    }

    companion object {
        private const val KEY_STARS = "stars_count"
        private const val KEY_STREAK = "streak_days"
        private const val KEY_COMPLETED_LESSONS = "completed_lessons"
        private const val KEY_LEARNING_LEVEL = "learning_level"
        private const val KEY_LANGUAGE = "interface_language"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_TTS = "tts_enabled"
        private const val KEY_LAST_ACTIVE_DATE = "last_active_date"
    }
}
