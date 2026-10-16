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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlphabetItem
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
import com.example.ui.theme.KidStarGold
import com.example.ui.theme.KidSunnyYellow

@Composable
fun BengaliScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    val completedLessons by viewModel.progressRepository.completedLessons.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("স্বরবর্ণ (১১)", "ব্যঞ্জনবর্ণ (৩৯)", "কারচিহ্ন (১০)")

    val currentList: List<AlphabetItem> = when (selectedTabIndex) {
        0 -> CurriculumRepository.bengaliVowels
        1 -> CurriculumRepository.bengaliConsonants
        else -> CurriculumRepository.bengaliKars
    }

    var selectedIndex by remember(selectedTabIndex) { mutableIntStateOf(0) }
    val activeItem = currentList.getOrElse(selectedIndex) { currentList.first() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "বাংলা বর্ণমালা ও শব্দ",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        // Subcategory Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = KidAmberOrange,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = KidAmberOrange,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        selectedIndex = 0
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) KidAmberOrange else KidDeepBlue
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Big Learning Card
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
                    // Header row of Card: Item index & Completed Indicator
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
                                text = "${activeItem.name} (${selectedIndex + 1}/${currentList.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidSkyBlue
                            )
                        }

                        if (completedLessons.contains(activeItem.id)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "শেখা হয়েছে",
                                    tint = KidMintGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "শেখা হয়েছে",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidMintGreen
                                )
                            }
                        }
                    }

                    // Large Letter
                    Text(
                        text = activeItem.letter,
                        fontSize = 82.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = KidAmberOrange,
                        textAlign = TextAlign.Center
                    )

                    // Pronunciation speaker button
                    SpeakerButton(
                        onClick = {
                            viewModel.speak("${activeItem.pronunciation}। ${activeItem.exampleWord}। ${activeItem.exampleSentenceBn}", "bn")
                            viewModel.awardStars(activeItem.id, 1)
                        },
                        isSpeaking = isSpeaking,
                        sizeDp = 56,
                        contentDescription = "উচ্চারণ শোনো"
                    )

                    // Example Word and Picture Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(KidSunnyYellow.copy(alpha = 0.25f), KidMintGreen.copy(alpha = 0.2f))
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
                                fontSize = 42.sp
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
                                    fontSize = 12.sp,
                                    color = KidDeepBlue.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Example Sentence
                    if (activeItem.exampleSentenceBn.isNotEmpty()) {
                        Text(
                            text = "“${activeItem.exampleSentenceBn}”",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = KidDeepBlue,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Action: Trace this letter button
                    Button(
                        onClick = {
                            viewModel.setTracingItem(activeItem)
                            viewModel.navigateTo(ScreenRoute.TRACING)
                        },
                        modifier = Modifier
                            .testTag("bengali_trace_button")
                            .fillMaxWidth(0.75f)
                            .height(46.dp),
                        shape = RoundedCornerShape(23.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KidBerryPink)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Brush,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "'${activeItem.letter}' হাত ঘুরিয়ে লিখি",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Controls: Previous, Grid, Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (selectedIndex > 0) {
                            selectedIndex--
                        } else {
                            selectedIndex = currentList.size - 1
                        }
                    },
                    modifier = Modifier
                        .testTag("bengali_prev_button")
                        .size(48.dp)
                        .background(KidSkyBlue.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "পূর্ববর্তী বর্ণ",
                        tint = KidDeepBlue
                    )
                }

                // Horizontal Letter Selector Quick Strip
                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(currentList) { index, item ->
                        val isSelected = selectedIndex == index
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) KidAmberOrange else Color.White
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) KidAmberOrange else Color.LightGray.copy(alpha = 0.5f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.letter,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else KidDeepBlue
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        if (selectedIndex < currentList.size - 1) {
                            selectedIndex++
                        } else {
                            selectedIndex = 0
                        }
                    },
                    modifier = Modifier
                        .testTag("bengali_next_button")
                        .size(48.dp)
                        .background(KidSkyBlue.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "পরবর্তী বর্ণ",
                        tint = KidDeepBlue
                    )
                }
            }
        }
    }
}
