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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.GameType
import com.example.ui.MainViewModel
import com.example.ui.ScreenRoute
import com.example.ui.components.KidTopBar
import com.example.ui.theme.KidAmberOrange
import com.example.ui.theme.KidCreamBackground
import com.example.ui.theme.KidDeepBlue
import com.example.ui.theme.KidMintGreen
import com.example.ui.theme.KidSkyBlue

@Composable
fun GamesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val games = GameType.values().toList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "শিক্ষামূলক খেলা (Games)",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() },
            onStarsClick = { viewModel.navigateTo(ScreenRoute.ACHIEVEMENTS) }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            items(games) { game ->
                Card(
                    modifier = Modifier
                        .testTag("game_item_${game.name.lowercase()}")
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { viewModel.openGame(game) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = game.icon,
                                fontSize = 36.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(KidAmberOrange.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = KidAmberOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = game.titleBn,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = game.descBn,
                                fontSize = 11.sp,
                                color = KidDeepBlue.copy(alpha = 0.7f),
                                maxLines = 2,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
