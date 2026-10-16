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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.example.ui.MainViewModel
import com.example.ui.ScreenRoute
import com.example.ui.components.KidTopBar
import com.example.ui.components.SpeakerButton
import com.example.ui.theme.KidAmberOrange
import com.example.ui.theme.KidCreamBackground
import com.example.ui.theme.KidDeepBlue
import com.example.ui.theme.KidMintGreen
import com.example.ui.theme.KidSkyBlue
import com.example.ui.theme.KidSoftPurple
import com.example.ui.theme.KidStarGold
import com.example.ui.theme.KidSunnyYellow

@Composable
fun StoryReaderScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val story = viewModel.selectedStory.collectAsState().value
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()

    if (story == null) {
        viewModel.navigateBack()
        return
    }

    var currentPageIndex by remember { mutableIntStateOf(0) }
    // 0: Bengali, 1: English, 2: Bilingual
    var languageMode by remember { mutableIntStateOf(0) }
    var inQuizMode by remember { mutableStateOf(false) }

    // Quiz state
    var currentQuizIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var quizCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = story.titleBn,
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        if (!inQuizMode) {
            // Story Reader Mode
            val currentPage = story.pages[currentPageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Language Mode Selector Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val modes = listOf("বাংলা", "English", "উভয় ভাষা")
                    modes.forEachIndexed { idx, label ->
                        val isSelected = languageMode == idx
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) KidSoftPurple else Color.White)
                                .border(1.dp, if (isSelected) KidSoftPurple else Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .clickable { languageMode = idx }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else KidDeepBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Page Card
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
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Page Number Badge & Speaker
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(KidSoftPurple.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "পাতা ${currentPageIndex + 1} / ${story.pages.size}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidSoftPurple
                                )
                            }

                            SpeakerButton(
                                onClick = {
                                    val narrationText = when (languageMode) {
                                        0 -> currentPage.textBn
                                        1 -> currentPage.textEn
                                        else -> "${currentPage.textBn}। ${currentPage.textEn}"
                                    }
                                    val narrationLang = if (languageMode == 1) "en" else "bn"
                                    viewModel.speak(narrationText, narrationLang)
                                },
                                isSpeaking = isSpeaking,
                                sizeDp = 44,
                                tint = KidSoftPurple,
                                contentDescription = "গল্প শোনো"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Large Scene Illustration
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(KidSunnyYellow.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentPage.illustrationEmoji,
                                fontSize = 64.sp
                            )
                        }

                        Text(
                            text = currentPage.sceneTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidDeepBlue.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Story Text (based on languageMode)
                        if (languageMode == 0 || languageMode == 2) {
                            Text(
                                text = currentPage.textBn,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KidDeepBlue,
                                lineHeight = 26.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        if (languageMode == 2) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (languageMode == 1 || languageMode == 2) {
                            Text(
                                text = currentPage.textEn,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (languageMode == 2) KidDeepBlue.copy(alpha = 0.75f) else KidDeepBlue,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // If on final page, show the moral
                        if (currentPageIndex == story.pages.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(KidMintGreen.copy(alpha = 0.15f))
                                    .padding(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "🌟 গল্পের নীতিবাক্য (Moral):",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KidMintGreen
                                    )
                                    Text(
                                        text = story.moralBn,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = KidDeepBlue,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Page Navigation: Prev, Quiz Button, Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (currentPageIndex > 0) currentPageIndex-- },
                        enabled = currentPageIndex > 0,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KidSoftPurple)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("আগের পাতা", fontSize = 13.sp)
                    }

                    if (currentPageIndex == story.pages.size - 1) {
                        Button(
                            onClick = {
                                inQuizMode = true
                                currentQuizIndex = 0
                                selectedOptionIndex = null
                                isCorrect = null
                                quizCompleted = false
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidAmberOrange)
                        ) {
                            Text("কুইজ শুরু করো ❓", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { if (currentPageIndex < story.pages.size - 1) currentPageIndex++ },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidSoftPurple)
                        ) {
                            Text("পরের পাতা", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        } else {
            // Story Comprehension Quiz Mode
            val questions = story.questions
            val activeQuestion = questions.getOrNull(currentQuizIndex)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (activeQuestion != null && !quizCompleted) {
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
                                text = "গল্পের কুইজ (প্রশ্ন ${currentQuizIndex + 1}/${questions.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidAmberOrange
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = activeQuestion.questionBn,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = activeQuestion.questionEn,
                                fontSize = 13.sp,
                                color = KidDeepBlue.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Options List
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                activeQuestion.optionsBn.forEachIndexed { optIdx, optText ->
                                    val isSelected = selectedOptionIndex == optIdx
                                    val isThisCorrect = optIdx == activeQuestion.correctIndex

                                    val bgColor = when {
                                        isSelected && isCorrect == true -> KidMintGreen.copy(alpha = 0.2f)
                                        isSelected && isCorrect == false -> Color.Red.copy(alpha = 0.15f)
                                        else -> KidCreamBackground
                                    }

                                    val borderColor = when {
                                        isSelected && isCorrect == true -> KidMintGreen
                                        isSelected && isCorrect == false -> Color.Red
                                        else -> Color.LightGray.copy(alpha = 0.5f)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(bgColor)
                                            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                                            .clickable {
                                                selectedOptionIndex = optIdx
                                                val correct = isThisCorrect
                                                isCorrect = correct
                                                if (correct) {
                                                    viewModel.speak("সাবাশ! সঠিক উত্তর!", "bn")
                                                } else {
                                                    viewModel.speak("আবার চেষ্টা করো সোনামণি!", "bn")
                                                }
                                            }
                                            .padding(14.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${('ক'.code + optIdx).toChar()}) $optText",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = KidDeepBlue
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

                            Spacer(modifier = Modifier.height(14.dp))

                            // Next / Continue Quiz Button
                            Button(
                                onClick = {
                                    if (currentQuizIndex < questions.size - 1) {
                                        currentQuizIndex++
                                        selectedOptionIndex = null
                                        isCorrect = null
                                    } else {
                                        quizCompleted = true
                                        viewModel.awardStars("quiz_${story.id}", 3)
                                    }
                                },
                                enabled = isCorrect == true,
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidMintGreen)
                            ) {
                                Text(
                                    text = if (currentQuizIndex < questions.size - 1) "পরের প্রশ্ন ➡️" else "কুইজ সম্পন্ন! 🌟",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // Quiz Completed Celebration Card
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
                            Text("🎉", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "অসাধারণ! গল্প ও কুইজ সম্পন্ন!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = KidDeepBlue,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "তুমি এই গল্পটি পড়ে ৩টি সোনালী স্টার অর্জন করেছ! 🌟🌟🌟",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = KidDeepBlue.copy(alpha = 0.8f),
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
                                Text("গল্পের তালিকায় ফিরে যাও", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
