package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.GameType
import com.example.ui.MainViewModel
import com.example.ui.ScreenRoute
import com.example.ui.components.KidTopBar
import com.example.ui.components.SpeakerButton
import com.example.ui.theme.KidAmberOrange
import com.example.ui.theme.KidBerryPink
import com.example.ui.theme.KidCreamBackground
import com.example.ui.theme.KidDeepBlue
import com.example.ui.theme.KidMintGreen
import com.example.ui.theme.KidSkyBlue
import com.example.ui.theme.KidSunnyYellow

data class GameQuestion(
    val prompt: String,
    val visual: String,
    val spokenAudio: String,
    val audioLang: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class SpellingQuestion(
    val word: String,
    val letters: List<String>,
    val emoji: String,
    val hint: String
)

@Composable
fun GamePlayScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val game = viewModel.selectedGame.collectAsState().value
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()

    if (game == null) {
        viewModel.navigateBack()
        return
    }

    // If game is Tracing, navigate directly to TracingScreen
    if (game == GameType.LETTER_TRACING) {
        TracingScreen(viewModel = viewModel, modifier = modifier)
        return
    }

    var questionRoundIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var gameCompleted by remember { mutableStateOf(false) }

    // Spelling game specific state
    val assembledLetters = remember { mutableStateListOf<String>() }

    // Sample generated questions bank for the selected game
    val questions = remember(game) {
        when (game) {
            GameType.MATCH_LETTER -> listOf(
                GameQuestion("এই ছবির শুরুর বর্ণ কোনটি?", "🥭", "আম", "bn", listOf("ক", "আ", "ব"), 1, "আ-তে আম"),
                GameQuestion("ছবি দেখে প্রথম বর্ণটি বেছে নাও", "🍌", "কলা", "bn", listOf("ক", "ম", "প"), 0, "ক-তে কলা"),
                GameQuestion("What is the starting letter?", "🍎", "Apple", "en", listOf("B", "A", "C"), 1, "A is for Apple"),
                GameQuestion("শুরুর বর্ণটি চিহ্নিত করো", "🐟", "মাছ", "bn", listOf("র", "ম", "ল"), 1, "ম-তে মাছ"),
                GameQuestion("What is the starting letter?", "🐱", "Cat", "en", listOf("C", "D", "E"), 0, "C is for Cat")
            )
            GameType.FIND_PICTURE -> listOf(
                GameQuestion("নিচের কোনটি 'পাখি'?", "পাখি", "পাখি", "bn", listOf("🐦", "🐟", "🐄"), 0, "পাখি আকাশে ওড়ে"),
                GameQuestion("Which one is 'Dog'?", "Dog", "Dog", "en", listOf("🐶", "🐱", "🦁"), 0, "Dog is a loyal pet"),
                GameQuestion("নিচের কোনটি 'ছাতা'?", "ছাতা", "ছাতা", "bn", listOf("☂️", "👓", "🧺"), 0, "বৃষ্টিতে ছাতা লাগে"),
                GameQuestion("Which one is 'Book'?", "Book", "Book", "en", listOf("📚", "⚽", "🚗"), 0, "We read books"),
                GameQuestion("নিচের কোনটি 'ফুল'?", "ফুল", "ফুল", "bn", listOf("🌸", "🍎", "🍌"), 0, "সুন্দর ফুল")
            )
            GameType.LISTEN_CHOOSE -> listOf(
                GameQuestion("উচ্চারণ শুনে সঠিক বর্ণ বেছে নাও", "🔊", "ঈগল", "bn", listOf("ঈ", "উ", "এ"), 0, "ঈ-তে ঈগল"),
                GameQuestion("Listen and pick the letter", "🔊", "B", "en", listOf("D", "B", "P"), 1, "Letter B"),
                GameQuestion("উচ্চারণ শুনে সঠিক ছবি বেছে নাও", "🔊", "গরু", "bn", listOf("🐄", "🐎", "🦌"), 0, "গরু দুধ দেয়"),
                GameQuestion("Listen and choose the word", "🔊", "Sun", "en", listOf("☀️", "🌙", "🌧️"), 0, "The sun shines"),
                GameQuestion("উচ্চারণ শুনে সঠিক বর্ণটি বাছাই করো", "🔊", "সিংহ", "bn", listOf("স", "হ", "ম"), 0, "স-তে সিংহ")
            )
            GameType.MISSING_LETTER -> listOf(
                GameQuestion("ফাঁকা ঘরে সঠিক বর্ণ বসাও:\nক _ লা", "🍌", "কলা", "bn", listOf("লা", "মা", "না"), 0, "ক + লা = কলা"),
                GameQuestion("Fill the missing letter:\nA _ P L E", "🍎", "Apple", "en", listOf("P", "K", "T"), 0, "APPLE"),
                GameQuestion("ফাঁকা ঘরে সঠিক বর্ণ বসাও:\nপা _", "🐦", "পাখি", "bn", listOf("খি", "লি", "মি"), 0, "পা + খি = পাখি"),
                GameQuestion("Fill the missing letter:\nB A _ L", "⚽", "Ball", "en", listOf("L", "T", "R"), 0, "BALL"),
                GameQuestion("ফাঁকা ঘরে সঠিক বর্ণ বসাও:\nবা _", "👨", "বাবা", "bn", listOf("বা", "দা", "মা"), 0, "বা + বা = বাবা")
            )
            GameType.WORD_PICTURE_MATCH -> listOf(
                GameQuestion("শব্দটির সাথে সঠিক ছবি মেলাও:\n'নৌকা'", "⛵", "নৌকা", "bn", listOf("⛵", "🚗", "✈️"), 0, "নদীতে ভাসে নৌকা"),
                GameQuestion("Match word with picture:\n'Star'", "⭐", "Star", "en", listOf("⭐", "☀️", "🌙"), 0, "Star shines in the sky"),
                GameQuestion("শব্দটির সাথে সঠিক ছবি মেলাও:\n'গোলাপ'", "🌹", "গোলাপ", "bn", listOf("🌹", "🌻", "🌿"), 0, "লাল গোলাপ ফুল"),
                GameQuestion("Match word with picture:\n'Tree'", "🌳", "Tree", "en", listOf("🌳", "🌸", "🍂"), 0, "Green tree"),
                GameQuestion("শব্দটির সাথে সঠিক ছবি মেলাও:\n'চাঁদ'", "🌙", "চাঁদ", "bn", listOf("🌙", "☀️", "⭐"), 0, "রাতের আকাশে চাঁদ")
            )
            GameType.STORY_QUIZ -> listOf(
                GameQuestion("খরগোশ ও কচ্ছপের দৌড়ে কে জয়ী হয়েছিল?", "🐢", "কচ্ছপ জয়ী হয়েছিল", "bn", listOf("কচ্ছপ", "খরগোশ", "শেয়াল"), 0, "ধৈর্য ধরে কচ্ছপ জয়লাভ করল"),
                GameQuestion("পিপঁড়াকে বাঁচাতে ঘুঘু কী ফেলেছিল?", "🕊️", "সবুজ পাতা", "bn", listOf("সবুজ পাতা", "পাথর", "ফুল"), 0, "ঘুঘু একটি পাতা ফেলেছিল"),
                GameQuestion("তৃষ্ণার্ত কাক কলসিতে কী ফেলেছিল?", "🏺", "নুড়িপাথর", "bn", listOf("নুড়িপাথর", "বালি", "পাতা"), 0, "কাক নুড়িপাথর ফেলে পানি তুলল"),
                GameQuestion("কাঠুরিয়ার আসল কুঠার কিসের তৈরি ছিল?", "🪓", "লোহার কুঠার", "bn", listOf("লোহার", "সোনার", "রুপার"), 0, "সৎ কাঠুরিয়ার কুঠার ছিল লোহার"),
                GameQuestion("সিংহকে জাল কেটে কে সাহায্য করেছিল?", "🐭", "ছোট্ট ইঁদুর", "bn", listOf("ছোট্ট ইঁদুর", "বানর", "হাতি"), 0, "ইঁদুর দাঁত দিয়ে জাল কেটেছিল")
            )
            else -> emptyList()
        }
    }

    // Spelling question sets
    val spellingQuestions = remember {
        listOf(
            SpellingQuestion("CAT", listOf("A", "C", "T"), "🐱", "বিড়াল (C-A-T)"),
            SpellingQuestion("আম", listOf("ম", "আ"), "🥭", "মিষ্টি ফল (আ-ম)"),
            SpellingQuestion("SUN", listOf("U", "S", "N"), "☀️", "সূর্য (S-U-N)"),
            SpellingQuestion("বই", listOf("ই", "ব"), "📚", "পড়ার বই (ব-ই)"),
            SpellingQuestion("DOG", listOf("O", "D", "G"), "🐶", "কুকুর (D-O-G)")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = game.titleBn,
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!gameCompleted) {
                if (game == GameType.SPELL_WORD) {
                    // Spelling Puzzle Game
                    val currentSpell = spellingQuestions[questionRoundIndex]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(26.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "বানান মেলাও (${questionRoundIndex + 1}/${spellingQuestions.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidAmberOrange
                            )

                            Text(
                                text = currentSpell.emoji,
                                fontSize = 72.sp
                            )

                            Text(
                                text = currentSpell.hint,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KidDeepBlue
                            )

                            // User assembled answer display
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 0 until currentSpell.word.length) {
                                    val letter = assembledLetters.getOrNull(i)
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (letter != null) KidMintGreen.copy(alpha = 0.2f) else Color(0xFFF1F5F9))
                                            .border(
                                                2.dp,
                                                if (letter != null) KidMintGreen else Color.LightGray,
                                                RoundedCornerShape(14.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = letter ?: "_",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = KidDeepBlue
                                        )
                                    }
                                }
                            }

                            // Available scrambled letter tiles
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                currentSpell.letters.forEach { char ->
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(KidSkyBlue)
                                            .clickable {
                                                if (assembledLetters.size < currentSpell.word.length) {
                                                    assembledLetters.add(char)
                                                    viewModel.speak(char, if (currentSpell.word.any { it in 'A'..'Z' }) "en" else "bn")

                                                    // Check if word is complete
                                                    if (assembledLetters.joinToString("") == currentSpell.word) {
                                                        viewModel.speak("চমৎকার! সঠিক বানান!", "bn")
                                                        isCorrect = true
                                                    } else if (assembledLetters.size == currentSpell.word.length) {
                                                        viewModel.speak("আবার চেষ্টা করো সোনামণি!", "bn")
                                                        isCorrect = false
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // Actions: Reset & Next
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        assembledLetters.clear()
                                        isCorrect = null
                                    },
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "আবার সাজাও")
                                }

                                Button(
                                    onClick = {
                                        assembledLetters.clear()
                                        isCorrect = null
                                        if (questionRoundIndex < spellingQuestions.size - 1) {
                                            questionRoundIndex++
                                        } else {
                                            gameCompleted = true
                                            viewModel.awardStars("game_${game.name}", 3)
                                        }
                                    },
                                    enabled = isCorrect == true,
                                    shape = RoundedCornerShape(22.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = KidMintGreen)
                                ) {
                                    Text("পরের বানান 🌟", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // Standard Multiple Choice Question for other games
                    val activeQ = questions.getOrNull(questionRoundIndex)
                    if (activeQ != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(26.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "প্রশ্ন ${questionRoundIndex + 1} / ${questions.size}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidAmberOrange
                                )

                                Text(
                                    text = activeQ.prompt,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidDeepBlue,
                                    textAlign = TextAlign.Center
                                )

                                // Visual or Speaker button
                                if (game == GameType.LISTEN_CHOOSE) {
                                    SpeakerButton(
                                        onClick = {
                                            viewModel.speak(activeQ.spokenAudio, activeQ.audioLang)
                                        },
                                        isSpeaking = isSpeaking,
                                        sizeDp = 64,
                                        tint = KidAmberOrange,
                                        contentDescription = "কথা শোনো"
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(KidSunnyYellow.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = activeQ.visual,
                                            fontSize = 54.sp
                                        )
                                    }
                                }

                                // 3 Options
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    activeQ.options.forEachIndexed { optIdx, optText ->
                                        val isSelected = selectedOption == optIdx
                                        val isThisCorrect = optIdx == activeQ.correctIndex

                                        val bgColor = when {
                                            isSelected && isCorrect == true -> KidMintGreen.copy(alpha = 0.2f)
                                            isSelected && isCorrect == false -> Color.Red.copy(alpha = 0.15f)
                                            else -> KidCreamBackground
                                        }

                                        val borderColor = when {
                                            isSelected && isCorrect == true -> KidMintGreen
                                            isSelected && isCorrect == false -> Color.Red
                                            else -> Color.LightGray.copy(alpha = 0.6f)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(18.dp))
                                                .background(bgColor)
                                                .border(2.dp, borderColor, RoundedCornerShape(18.dp))
                                                .clickable {
                                                    selectedOption = optIdx
                                                    isCorrect = isThisCorrect
                                                    if (isThisCorrect) {
                                                        viewModel.speak("সাবাশ! সঠিক উত্তর!", "bn")
                                                    } else {
                                                        viewModel.speak("আবার চেষ্টা করো সোনামণি!", "bn")
                                                    }
                                                }
                                                .padding(14.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = optText,
                                                    fontSize = 22.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = KidDeepBlue,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                if (isSelected) {
                                                    if (isCorrect == true) {
                                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = KidMintGreen)
                                                    } else {
                                                        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Red)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Next Button
                                Button(
                                    onClick = {
                                        selectedOption = null
                                        isCorrect = null
                                        if (questionRoundIndex < questions.size - 1) {
                                            questionRoundIndex++
                                        } else {
                                            gameCompleted = true
                                            viewModel.awardStars("game_${game.name}", 3)
                                        }
                                    },
                                    enabled = isCorrect == true,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = KidMintGreen)
                                ) {
                                    Text(
                                        text = if (questionRoundIndex < questions.size - 1) "পরের খেলা ➡️" else "খেলা সম্পন্ন! 🌟",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Game Finished Celebration
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("🏆", fontSize = 68.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "চমৎকার খেলেছ সোনামণি!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KidAmberOrange
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${game.titleBn} সফলভাবে শেষ করে তুমি ৩টি স্টার পেয়েছ! 🌟🌟🌟",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = KidDeepBlue,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidAmberOrange)
                        ) {
                            Text("অন্যান্য খেলায় চলো", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
