package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CurriculumRepository
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

@Composable
fun EnglishScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    val completedLessons by viewModel.progressRepository.completedLessons.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("A–Z বর্ণমালা", "শব্দ ভাণ্ডার (Vocabulary)")

    val alphabetList = CurriculumRepository.englishAlphabet
    var letterIndex by remember { mutableIntStateOf(0) }
    val activeItem = alphabetList.getOrElse(letterIndex) { alphabetList.first() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "English & Phonics",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        // Sub-tabs: A-Z vs Vocabulary
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = KidSkyBlue,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = KidSkyBlue,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) KidSkyBlue else KidDeepBlue
                        )
                    }
                )
            }
        }

        if (selectedTabIndex == 0) {
            // A-Z Alphabet Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header: Letter Name & Phonics hint badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(KidSkyBlue.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Letter ${activeItem.uppercase} ${activeItem.lowercase} (${letterIndex + 1}/26)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidSkyBlue
                                )
                            }

                            if (completedLessons.contains(activeItem.id)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = KidMintGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Learned",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidMintGreen
                                    )
                                }
                            }
                        }

                        // Large Uppercase and Lowercase Display
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = activeItem.uppercase,
                                fontSize = 80.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = KidSkyBlue
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = activeItem.lowercase,
                                fontSize = 68.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidAmberOrange
                            )
                        }

                        // Phonics Sound Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(KidSunnyYellow.copy(alpha = 0.3f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Phonics: ${activeItem.phonicsHint}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KidDeepBlue
                            )
                        }

                        // Audio button (distinguishes letter name and phonics sound!)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    viewModel.speak("${activeItem.uppercase}. ${activeItem.lowercase}", "en")
                                    viewModel.awardStars(activeItem.id, 1)
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidSkyBlue)
                            ) {
                                Text("Letter Name 🔊", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.speak("${activeItem.lowercase} is for ${activeItem.exampleWord}", "en")
                                    viewModel.awardStars(activeItem.id, 1)
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidAmberOrange)
                            ) {
                                Text("Word & Sound 🍎", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Example Word & Emoji
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFE8F4F8), Color(0xFFFDE8E9))
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    text = activeItem.emoji,
                                    fontSize = 44.sp
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = activeItem.exampleWord,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidDeepBlue
                                    )
                                    Text(
                                        text = activeItem.exampleWordMeaning,
                                        fontSize = 13.sp,
                                        color = KidDeepBlue.copy(alpha = 0.75f)
                                    )
                                }
                            }
                        }

                        // Example Sentence
                        Text(
                            text = "“${activeItem.exampleSentenceEn}”",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = KidDeepBlue,
                            textAlign = TextAlign.Center
                        )

                        // Trace Button
                        Button(
                            onClick = {
                                viewModel.setTracingItem(activeItem)
                                viewModel.navigateTo(ScreenRoute.TRACING)
                            },
                            modifier = Modifier
                                .testTag("english_trace_button")
                                .fillMaxWidth(0.75f)
                                .height(46.dp),
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidBerryPink)
                        ) {
                            Icon(imageVector = Icons.Default.Brush, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Trace '${activeItem.uppercase}'",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Prev / Horizontal Strip / Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (letterIndex > 0) letterIndex-- else letterIndex = alphabetList.size - 1
                        },
                        modifier = Modifier
                            .testTag("english_prev_button")
                            .size(48.dp)
                            .background(KidSkyBlue.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous letter",
                            tint = KidDeepBlue
                        )
                    }

                    LazyRow(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        itemsIndexed(alphabetList) { index, item ->
                            val isSelected = letterIndex == index
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) KidSkyBlue else Color.White)
                                    .border(
                                        1.dp,
                                        if (isSelected) KidSkyBlue else Color.LightGray.copy(alpha = 0.5f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { letterIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.uppercase,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else KidDeepBlue
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            if (letterIndex < alphabetList.size - 1) letterIndex++ else letterIndex = 0
                        },
                        modifier = Modifier
                            .testTag("english_next_button")
                            .size(48.dp)
                            .background(KidSkyBlue.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next letter",
                            tint = KidDeepBlue
                        )
                    }
                }
            }
        } else {
            // Vocabulary List categorized
            val vocabList = CurriculumRepository.vocabularyList
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(vocabList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.emoji,
                                    fontSize = 36.sp
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = item.englishWord,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidDeepBlue
                                    )
                                    Text(
                                        text = "${item.bengaliWord} (${item.category.split(" ")[0]})",
                                        fontSize = 13.sp,
                                        color = KidDeepBlue.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "“${item.exampleSentence}”",
                                        fontSize = 12.sp,
                                        color = KidSkyBlue
                                    )
                                }
                            }

                            SpeakerButton(
                                onClick = {
                                    viewModel.speak("${item.englishWord}. ${item.bengaliWord}", "en")
                                    viewModel.awardStars(item.id, 1)
                                },
                                isSpeaking = isSpeaking,
                                sizeDp = 44,
                                tint = KidSkyBlue,
                                contentDescription = "Listen to ${item.englishWord}"
                            )
                        }
                    }
                }
            }
        }
    }
}
