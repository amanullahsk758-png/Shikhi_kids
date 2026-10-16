package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Throwable) {
            Log.e("SpeechManager", "Could not initialize TextToSpeech: ${e.message}")
        }
    }

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentUtterance = MutableStateFlow<String?>(null)
    val currentUtterance: StateFlow<String?> = _currentUtterance.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    _currentUtterance.value = utteranceId
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentUtterance.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentUtterance.value = null
                }
            })
            Log.d("SpeechManager", "TTS successfully initialized")
        } else {
            Log.e("SpeechManager", "Failed to initialize TTS")
        }
    }

    fun speak(text: String, language: String = "bn", speechRate: Float = 0.85f) {
        if (!isInitialized || tts == null) return

        try {
            tts?.stop()

            val locale = if (language == "bn") {
                val bnBD = Locale("bn", "BD")
                val availability = tts?.isLanguageAvailable(bnBD)
                if (availability != TextToSpeech.LANG_NOT_SUPPORTED && availability != TextToSpeech.LANG_MISSING_DATA) {
                    bnBD
                } else {
                    Locale("bn")
                }
            } else {
                Locale.US
            }

            tts?.language = locale
            tts?.setSpeechRate(speechRate) // slightly slower for young kids
            tts?.setPitch(1.1f) // friendly, warm tone for children

            val utteranceId = "utterance_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error in speak: ${e.message}")
            _isSpeaking.value = false
        }
    }

    fun stop() {
        try {
            tts?.stop()
            _isSpeaking.value = false
            _currentUtterance.value = null
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error in stop: ${e.message}")
        }
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error in release: ${e.message}")
        }
    }
}
