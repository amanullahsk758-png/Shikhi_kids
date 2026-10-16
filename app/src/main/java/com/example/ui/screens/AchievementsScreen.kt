package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CurriculumRepository
import com.example.ui.MainViewModel
import com.example.ui.components.KidTopBar
import com.example.ui.theme.KidAmberOrange
import com.example.ui.theme.KidCreamBackground
import com.example.ui.theme.KidDeepBlue
import com.example.ui.theme.KidMintGreen
import com.example.ui.theme.KidStarGold
import com.example.ui.theme.KidSunnyYellow

@Composable
fun AchievementsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val streakDays by viewModel.progressRepository.streakDays.collectAsState()
    val completedLessons by viewModel.progressRepository.completedLessons.collectAsState()
    val badges = CurriculumRepository.badgesList

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "আমার অর্জন ও স্টার",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats summary card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🌟 $starsCount", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = KidAmberOrange)
                                Text("অর্জিত স্টার", fontSize = 12.sp, color = KidDeepBlue.copy(alpha = 0.7f))
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color.LightGray.copy(alpha = 0.5f))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔥 $streakDays দিন", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF5964))
                                Text("অনুশীলন স্ট্রিক", fontSize = 12.sp, color = KidDeepBlue.copy(alpha = 0.7f))
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color.LightGray.copy(alpha = 0.5f))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📚 ${completedLessons.size}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = KidMintGreen)
                                Text("সম্পন্ন পাঠ", fontSize = 12.sp, color = KidDeepBlue.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "চকচকে মেডেল ও ব্যাজ সমূহ (Badges):",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = KidDeepBlue,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            items(badges) { badge ->
                val isUnlocked = starsCount >= badge.requiredStars

                Card(
                    modifier = Modifier
                        .testTag("badge_item_${badge.id}")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) Color.White else Color(0xFFF8F9FA)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isUnlocked) 3.dp else 1.dp
                    )
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
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isUnlocked) KidSunnyYellow.copy(alpha = 0.25f) else Color.LightGray.copy(alpha = 0.25f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isUnlocked) badge.iconEmoji else "🔒",
                                    fontSize = 28.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = badge.titleBn,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUnlocked) KidDeepBlue else Color.Gray
                                    )
                                    if (isUnlocked) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "আনলকড",
                                            tint = KidMintGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = badge.descBn,
                                    fontSize = 12.sp,
                                    color = if (isUnlocked) KidDeepBlue.copy(alpha = 0.7f) else Color.Gray
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                val progressFraction = (starsCount.toFloat() / badge.requiredStars.toFloat()).coerceIn(0f, 1f)
                                LinearProgressIndicator(
                                    progress = { progressFraction },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (isUnlocked) KidMintGreen else KidAmberOrange,
                                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isUnlocked) KidMintGreen.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.25f)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${badge.requiredStars} ⭐",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) KidMintGreen else Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
