package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SpeechManager
import com.example.data.model.LearningLevel
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
import com.example.ui.theme.KidSoftPurple
import com.example.ui.theme.KidSunnyYellow

data class HomeModuleItem(
    val route: ScreenRoute,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val iconVector: ImageVector,
    val iconEmoji: String,
    val primaryColor: Color,
    val bgGradient: List<Color>,
    val testTagId: String
)

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val streakDays by viewModel.progressRepository.streakDays.collectAsState()
    val learningLevel by viewModel.progressRepository.learningLevel.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    var showDownloadDialog by remember { mutableStateOf(false) }

    val modules = listOf(
        HomeModuleItem(
            route = ScreenRoute.BENGALI_LEARNING,
            titleBn = "বাংলা শেখা",
            titleEn = "Bengali Learning",
            subtitleBn = "স্বরবর্ণ, ব্যঞ্জনবর্ণ ও শব্দ",
            iconVector = Icons.Default.Translate,
            iconEmoji = "অ",
            primaryColor = Color(0xFFE63946),
            bgGradient = listOf(Color(0xFFFFECEF), Color(0xFFFFD6DC)),
            testTagId = "home_card_bengali"
        ),
        HomeModuleItem(
            route = ScreenRoute.ENGLISH_LEARNING,
            titleBn = "English & Phonics",
            titleEn = "A to Z Learning",
            subtitleBn = "Letters, Sounds & Words",
            iconVector = Icons.Default.Translate,
            iconEmoji = "🔤",
            primaryColor = KidSkyBlue,
            bgGradient = listOf(Color(0xFFEBF7FF), Color(0xFFD4EFFF)),
            testTagId = "home_card_english"
        ),
        HomeModuleItem(
            route = ScreenRoute.TRACING,
            titleBn = "হাত ঘুরিয়ে লিখি",
            titleEn = "Writing Practice",
            subtitleBn = "আঁকা ও বর্ণ ট্রেসিং",
            iconVector = Icons.Default.Brush,
            iconEmoji = "✍️",
            primaryColor = KidBerryPink,
            bgGradient = listOf(Color(0xFFFFEFF5), Color(0xFFFFDDEB)),
            testTagId = "home_card_tracing"
        ),
        HomeModuleItem(
            route = ScreenRoute.STORIES,
            titleBn = "গল্প ও ছড়া",
            titleEn = "Stories & Rhymes",
            subtitleBn = "১০টি শিক্ষণীয় সচিত্র গল্প",
            iconVector = Icons.Default.AutoStories,
            iconEmoji = "📖",
            primaryColor = KidSoftPurple,
            bgGradient = listOf(Color(0xFFF6EEFF), Color(0xFFE9D7FF)),
            testTagId = "home_card_stories"
        ),
        HomeModuleItem(
            route = ScreenRoute.GAMES,
            titleBn = "মজার খেলা",
            titleEn = "Learning Games",
            subtitleBn = "৮টি আকর্ষণীয় কুইজ গেম",
            iconVector = Icons.Default.SportsEsports,
            iconEmoji = "🎮",
            primaryColor = KidAmberOrange,
            bgGradient = listOf(Color(0xFFFFF6E5), Color(0xFFFFE7BA)),
            testTagId = "home_card_games"
        ),
        HomeModuleItem(
            route = ScreenRoute.NUMBERS_SHAPES,
            titleBn = "সংখ্যা, রঙ ও আকার",
            titleEn = "Numbers & Shapes",
            subtitleBn = "১-১০০ গণনা, রঙ ও আকৃতি",
            iconVector = Icons.Default.Category,
            iconEmoji = "🔢",
            primaryColor = KidMintGreen,
            bgGradient = listOf(Color(0xFFE8FBF8), Color(0xFFC7F7EE)),
            testTagId = "home_card_numbers"
        ),
        HomeModuleItem(
            route = ScreenRoute.ACHIEVEMENTS,
            titleBn = "আমার অর্জন",
            titleEn = "Stars & Badges",
            subtitleBn = "চকচকে স্টার ও মেডেল",
            iconVector = Icons.Default.EmojiEvents,
            iconEmoji = "🌟",
            primaryColor = Color(0xFFFFB703),
            bgGradient = listOf(Color(0xFFFFFBEA), Color(0xFFFFF3C4)),
            testTagId = "home_card_achievements"
        ),
        HomeModuleItem(
            route = ScreenRoute.PARENT_DASHBOARD,
            titleBn = "অভিভাবক কর্নার",
            titleEn = "Parent Dashboard",
            subtitleBn = "পড়াশোনার স্তর ও রিপোর্ট",
            iconVector = Icons.Default.Lock,
            iconEmoji = "👨‍👩‍👧",
            primaryColor = KidDeepBlue,
            bgGradient = listOf(Color(0xFFEEF2F6), Color(0xFFDCE4EC)),
            testTagId = "home_card_parent"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "শিখি কিডস (Shikhi Kids)",
            starsCount = starsCount,
            showBackButton = false,
            showHomeButton = false,
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
            // Welcome Greeting & Hero Banner
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Welcome Card with friendly mascot greeting
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "হ্যালো সোনামণি! চলো শিখি! ✨",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = KidDeepBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(KidSunnyYellow.copy(alpha = 0.35f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "${learningLevel.labelBn} (${learningLevel.ageRange})",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = KidDeepBlue
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🔥 $streakDays দিনের অনুশীলন",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidAmberOrange
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Box(
                                    modifier = Modifier
                                        .testTag("home_download_guide_button")
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(KidSkyBlue.copy(alpha = 0.15f))
                                        .clickable { showDownloadDialog = true }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📲 সহজে অ্যাপ ডাউনলোড করার নিয়ম",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidSkyBlue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            SpeakerButton(
                                onClick = {
                                    viewModel.speak("হ্যালো সোনামণি! চলো শিখি! আনন্দ নিয়ে বাংলা ও ইংরেজি পড়ালেখা শুরু করি!", "bn")
                                },
                                isSpeaking = isSpeaking,
                                sizeDp = 48,
                                contentDescription = "শুনে নাও স্বাগতম বার্তা"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hero Banner Illustration
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.shikhi_hero_banner_1792139019424),
                                contentDescription = "শিখি কিডস বন্ধু বাঘছানা ও পাখি",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            )
                            // Subtle overlay at bottom with app tagline
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color(0x99000000))
                                        )
                                    )
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "আনন্দময় খেলা, সুন্দর পড়ালেখা! 🎨📚",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Learning Module Cards (8 Cards)
            items(modules) { module ->
                Card(
                    modifier = Modifier
                        .testTag(module.testTagId)
                        .fillMaxWidth()
                        .height(148.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { viewModel.navigateTo(module.route) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(module.bgGradient)
                            )
                            .border(1.5.dp, module.primaryColor.copy(alpha = 0.25f), RoundedCornerShape(22.dp))
                            .padding(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = module.iconEmoji,
                                    fontSize = 32.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White.copy(alpha = 0.85f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = module.iconVector,
                                        contentDescription = null,
                                        tint = module.primaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = module.titleBn,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidDeepBlue,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = module.subtitleBn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = KidDeepBlue.copy(alpha = 0.75f),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = {
                Text(
                    text = "📲 অ্যাপ ডাউনলোড ও ব্যবহারের সহজ উপায়",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = KidDeepBlue
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "১. সরাসরি APK ডাউনলোড (সবচেয়ে সহজ):\n" +
                                "• AI Studio স্ক্রিনের উপরের ডানপাশের সেটিংস মেনু (Settings / Export) চাপুন।\n" +
                                "• 'Download APK' অপশন বেছে নিন। ফাইল ডাউনলোড হলে ফাইলে ট্যাপ করে 'Install' করুন।",
                        fontSize = 13.sp,
                        color = KidDeepBlue,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "২. মোবাইল ব্রাউজারে সাথে সাথে অ্যাপ হিসেবে ব্যবহার (PWA):\n" +
                                "• আপনার ক্রোম বা মোবাইল ব্রাউজারের থ্রি-ডট মেনু (⋮) চাপুন।\n" +
                                "• 'Add to Home screen' অথবা 'Install app' চাপুন। ফোনে সরাসরি অ্যাপ আইকন চলে আসবে!",
                        fontSize = 13.sp,
                        color = KidDeepBlue,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "৩. সম্পূর্ণ প্রজেক্ট জিপ (ZIP) ডাউনলোড:\n" +
                                "• AI Studio সেটিংস থেকে 'Export Project (ZIP)' অপশন চাপলে পুরো সোর্স কোড ও সব রিসোর্স আপনার ফোনে সেভ হয়ে যাবে।",
                        fontSize = 13.sp,
                        color = KidDeepBlue,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDownloadDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = KidAmberOrange)
                ) {
                    Text("বুঝেছি", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
