package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SpeechManager
import com.example.data.model.AlphabetItem
import com.example.data.model.LearningLevel
import com.example.data.model.StoryItem
import com.example.data.repository.CurriculumRepository
import com.example.data.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScreenRoute {
    HOME,
    BENGALI_LEARNING,
    ENGLISH_LEARNING,
    TRACING,
    STORIES,
    STORY_READER,
    GAMES,
    GAME_PLAY,
    NUMBERS_SHAPES,
    ACHIEVEMENTS,
    PARENT_DASHBOARD
}

enum class GameType(val titleBn: String, val titleEn: String, val icon: String, val descBn: String) {
    MATCH_LETTER("অক্ষর মেলাও", "Match the Letter", "🔤", "ছবির শুরুর বর্ণটি বেছে নাও"),
    FIND_PICTURE("সঠিক ছবি খোঁজো", "Find the Picture", "🖼️", "শব্দ দেখে সঠিক ছবি নির্বাচন করো"),
    LETTER_TRACING("বর্ণ আঁকো ও হাত ঘোরাও", "Letter Tracing", "✍️", "স্ক্রিনে সুন্দর করে বর্ণ আঁকা শেখো"),
    SPELL_WORD("বানান মেলাও", "Spell the Word", "🧩", "অক্ষর সাজিয়ে সঠিক বানান তৈরি করো"),
    LISTEN_CHOOSE("শুনে উত্তর দাও", "Listen and Choose", "🎧", "উচ্চারণ শুনে সঠিক বর্ণ বা ছবি বেছে নাও"),
    WORD_PICTURE_MATCH("শব্দ ও ছবি জোড়া", "Word-Picture Match", "🃏", "শব্দ ও ছবির সঠিক জোড়া মিলিয়ে নাও"),
    MISSING_LETTER("হারানো বর্ণ খোঁজো", "Missing Letter", "🔍", "শব্দটির ফাঁকা স্থানে সঠিক বর্ণ বসাও"),
    STORY_QUIZ("গল্পের কুইজ", "Story Quiz", "📖", "পড়া গল্পের উপর মজার প্রশ্নের উত্তর দাও")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val progressRepository = ProgressRepository(application)
    val speechManager = SpeechManager(application)

    // Navigation state
    private val _currentRoute = MutableStateFlow(ScreenRoute.HOME)
    val currentRoute: StateFlow<ScreenRoute> = _currentRoute.asStateFlow()

    // Navigation back stack for hardware & top-bar back
    private val routeStack = mutableListOf<ScreenRoute>()

    // Selected story for story reader
    private val _selectedStory = MutableStateFlow<StoryItem?>(null)
    val selectedStory: StateFlow<StoryItem?> = _selectedStory.asStateFlow()

    // Selected game
    private val _selectedGame = MutableStateFlow<GameType?>(null)
    val selectedGame: StateFlow<GameType?> = _selectedGame.asStateFlow()

    // Selected tracing item
    private val _selectedTracingItem = MutableStateFlow<AlphabetItem?>(null)
    val selectedTracingItem: StateFlow<AlphabetItem?> = _selectedTracingItem.asStateFlow()

    // Star celebration popup state
    private val _celebrationStars = MutableStateFlow<Int?>(null)
    val celebrationStars: StateFlow<Int?> = _celebrationStars.asStateFlow()

    init {
        // Default tracing item: 'অ'
        _selectedTracingItem.value = CurriculumRepository.bengaliVowels.first()
    }

    fun navigateTo(route: ScreenRoute) {
        if (_currentRoute.value != route) {
            routeStack.add(_currentRoute.value)
            _currentRoute.value = route
        }
    }

    fun navigateBack(): Boolean {
        if (routeStack.isNotEmpty()) {
            _currentRoute.value = routeStack.removeAt(routeStack.size - 1)
            return true
        }
        return false
    }

    fun navigateHome() {
        routeStack.clear()
        _currentRoute.value = ScreenRoute.HOME
        speechManager.stop()
    }

    fun openStory(story: StoryItem) {
        _selectedStory.value = story
        navigateTo(ScreenRoute.STORY_READER)
    }

    fun openGame(gameType: GameType) {
        _selectedGame.value = gameType
        navigateTo(ScreenRoute.GAME_PLAY)
    }

    fun setTracingItem(item: AlphabetItem) {
        _selectedTracingItem.value = item
    }

    fun speak(text: String, language: String = "bn") {
        if (progressRepository.soundEnabled.value && progressRepository.ttsEnabled.value) {
            speechManager.speak(text, language)
        }
    }

    fun awardStars(lessonId: String, amount: Int = 1) {
        viewModelScope.launch {
            val isFirstTime = progressRepository.addStarsForLesson(lessonId, amount)
            _celebrationStars.value = amount
        }
    }

    fun dismissCelebration() {
        _celebrationStars.value = null
    }

    fun setLearningLevel(level: LearningLevel) {
        progressRepository.setLearningLevel(level)
    }

    fun setInterfaceLanguage(language: String) {
        progressRepository.setInterfaceLanguage(language)
    }

    fun setSoundEnabled(enabled: Boolean) {
        progressRepository.setSoundEnabled(enabled)
    }

    fun setTtsEnabled(enabled: Boolean) {
        progressRepository.setTtsEnabled(enabled)
    }

    fun resetProgress() {
        progressRepository.resetProgress()
        navigateHome()
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.release()
    }
}
