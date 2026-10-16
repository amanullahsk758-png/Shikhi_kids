package com.example.data.model

enum class LearningLevel(val labelBn: String, val labelEn: String, val ageRange: String) {
    BEGINNER("শিশু শিক্ষার্থী", "Beginner", "বয়স ৩-৪"),
    EARLY_LEARNER("প্রথম শিক্ষার্থী", "Early Learner", "বয়স ৪-৬"),
    GROWING_READER("ছোট পাঠক", "Growing Reader", "বয়স ৬-৮")
}

data class AlphabetItem(
    val id: String,
    val letter: String,
    val uppercase: String = letter,
    val lowercase: String = letter,
    val name: String,
    val language: String, // "bn" or "en"
    val category: String, // "স্বরবর্ণ", "ব্যঞ্জনবর্ণ", "কারচিহ্ন", "Alphabet"
    val pronunciation: String,
    val phonicsHint: String = "",
    val exampleWord: String,
    val exampleWordMeaning: String,
    val exampleWordPronunciation: String,
    val emoji: String,
    val exampleSentenceBn: String = "",
    val exampleSentenceEn: String = ""
)

data class VocabularyItem(
    val id: String,
    val category: String,
    val englishWord: String,
    val bengaliWord: String,
    val pronunciation: String,
    val exampleSentence: String,
    val emoji: String
)

data class StoryPage(
    val pageNumber: Int,
    val textBn: String,
    val textEn: String,
    val illustrationEmoji: String,
    val sceneTitle: String
)

data class StoryQuizQuestion(
    val questionBn: String,
    val questionEn: String,
    val optionsBn: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int,
    val explanationBn: String
)

data class StoryItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val ageGroup: String,
    val moralBn: String,
    val moralEn: String,
    val coverEmoji: String,
    val pages: List<StoryPage>,
    val questions: List<StoryQuizQuestion>
)

data class NumberItem(
    val number: Int,
    val digitBn: String,
    val digitEn: String,
    val wordBn: String,
    val wordEn: String,
    val countEmoji: String
)

data class ShapeItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val emoji: String
)

data class ColorItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val hexColor: Long,
    val emoji: String
)

data class BadgeItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val descBn: String,
    val descEn: String,
    val iconEmoji: String,
    val requiredStars: Int
)
