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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
fun NumbersShapesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("গণনা ও সংখ্যা (১-১০০)", "রং ও আকৃতি", "সহজ যোগ-বিয়োগ")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "সংখ্যা, রঙ ও আকার",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = KidMintGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = KidMintGreen,
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
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) KidMintGreen else KidDeepBlue
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                // Numbers 1-20 detailed counting and 1-100 chart
                val numbers = CurriculumRepository.numberList
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    items(numbers) { num ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${num.digitBn} / ${num.digitEn}",
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = KidMintGreen
                                    )

                                    SpeakerButton(
                                        onClick = {
                                            viewModel.speak("${num.wordBn}। ${num.wordEn}", "bn")
                                            viewModel.awardStars("num_${num.number}", 1)
                                        },
                                        isSpeaking = isSpeaking,
                                        sizeDp = 38,
                                        tint = KidMintGreen,
                                        contentDescription = "উচ্চারণ শোনো"
                                    )
                                }

                                Text(
                                    text = num.countEmoji,
                                    fontSize = 18.sp,
                                    maxLines = 1
                                )

                                Text(
                                    text = "${num.wordBn} (${num.wordEn})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidDeepBlue
                                )
                            }
                        }
                    }
                }
            }
            1 -> {
                // Colors and Shapes explorer
                val colors = CurriculumRepository.colorList
                val shapes = CurriculumRepository.shapeList

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "🎨 সুন্দর রঙ চিনে নাও (Colors):",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidDeepBlue
                        )
                    }

                    items(colors) { col ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color(col.hexColor))
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text(
                                            text = "${col.nameBn} (${col.nameEn})",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = KidDeepBlue
                                        )
                                        Text(
                                            text = "উদাহরণ: ${col.emoji}",
                                            fontSize = 13.sp,
                                            color = KidDeepBlue.copy(alpha = 0.7f)
                                        )
                                    }
                                }

                                SpeakerButton(
                                    onClick = {
                                        viewModel.speak("${col.nameBn}। ${col.nameEn}", "bn")
                                    },
                                    isSpeaking = isSpeaking,
                                    sizeDp = 40,
                                    tint = KidAmberOrange
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "📐 জ্যামিতিক আকৃতি (Shapes):",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidDeepBlue
                        )
                    }

                    items(shapes) { shp ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = shp.emoji, fontSize = 34.sp)
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text(
                                            text = "${shp.nameBn} (${shp.nameEn})",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = KidDeepBlue
                                        )
                                        Text(
                                            text = shp.descriptionBn,
                                            fontSize = 12.sp,
                                            color = KidDeepBlue.copy(alpha = 0.7f)
                                        )
                                    }
                                }

                                SpeakerButton(
                                    onClick = {
                                        viewModel.speak("${shp.nameBn}। ${shp.nameEn}। ${shp.descriptionBn}", "bn")
                                    },
                                    isSpeaking = isSpeaking,
                                    sizeDp = 40,
                                    tint = KidSkyBlue
                                )
                            }
                        }
                    }
                }
            }
            2 -> {
                // Interactive Math addition & subtraction with visual fruits
                var num1 by remember { mutableIntStateOf(2) }
                var num2 by remember { mutableIntStateOf(3) }
                var isAddition by remember { mutableStateOf(true) }
                var selectedAnswer by remember { mutableStateOf<Int?>(null) }
                var feedbackCorrect by remember { mutableStateOf<Boolean?>(null) }

                val correctAnswer = if (isAddition) num1 + num2 else num1 - num2
                val answerOptions = remember(num1, num2, isAddition) {
                    val correct = if (isAddition) num1 + num2 else num1 - num2
                    val options = mutableListOf(correct)
                    while (options.size < 3) {
                        val wrong = (correct + (-2..2).random()).coerceAtLeast(0)
                        if (!options.contains(wrong)) options.add(wrong)
                    }
                    options.shuffled()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
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
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "মজার গণনা ও ফল মেলাও 🍎",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidMintGreen
                            )

                            // Visual Fruit Math
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "🍎".repeat(num1), fontSize = 24.sp)
                                    Text(text = "$num1", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = KidDeepBlue)
                                }

                                Text(
                                    text = if (isAddition) " + " else " − ",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = KidAmberOrange,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "🍎".repeat(num2), fontSize = 24.sp)
                                    Text(text = "$num2", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = KidDeepBlue)
                                }

                                Text(
                                    text = " = ?",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = KidSkyBlue,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }

                            // 3 Answer Choices
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                answerOptions.forEach { opt ->
                                    val isChosen = selectedAnswer == opt
                                    val isThisRight = opt == correctAnswer

                                    val btnBg = when {
                                        isChosen && feedbackCorrect == true -> KidMintGreen
                                        isChosen && feedbackCorrect == false -> Color.Red
                                        else -> KidSunnyYellow
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(CircleShape)
                                            .background(btnBg)
                                            .clickable {
                                                selectedAnswer = opt
                                                val correct = isThisRight
                                                feedbackCorrect = correct
                                                if (correct) {
                                                    viewModel.speak("সাবাশ! উত্তর $opt!", "bn")
                                                    viewModel.awardStars("math_$num1$num2", 1)
                                                } else {
                                                    viewModel.speak("আবার চেষ্টা করো!", "bn")
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$opt",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // New Math Problem Generator
                            Button(
                                onClick = {
                                    num1 = (2..5).random()
                                    num2 = (1..4).random()
                                    isAddition = listOf(true, false).random()
                                    if (!isAddition && num1 < num2) {
                                        val temp = num1; num1 = num2; num2 = temp
                                    }
                                    selectedAnswer = null
                                    feedbackCorrect = null
                                },
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidAmberOrange)
                            ) {
                                Text("নতুন অংক 🔄", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
