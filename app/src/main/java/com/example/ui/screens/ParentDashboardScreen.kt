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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LearningLevel
import com.example.ui.MainViewModel
import com.example.ui.components.KidTopBar
import com.example.ui.theme.KidAmberOrange
import androidx.compose.foundation.clickable
import com.example.ui.theme.KidCreamBackground
import com.example.ui.theme.KidDeepBlue
import com.example.ui.theme.KidMintGreen
import com.example.ui.theme.KidSkyBlue
import com.example.ui.theme.KidSunnyYellow

@Composable
fun ParentDashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val streakDays by viewModel.progressRepository.streakDays.collectAsState()
    val completedLessons by viewModel.progressRepository.completedLessons.collectAsState()
    val currentLevel by viewModel.progressRepository.learningLevel.collectAsState()
    val soundEnabled by viewModel.progressRepository.soundEnabled.collectAsState()
    val ttsEnabled by viewModel.progressRepository.ttsEnabled.collectAsState()

    // Adult Math Challenge Gate state
    var isUnlocked by remember { mutableStateOf(false) }
    var mathChallengeNum1 by remember { mutableIntStateOf(4) }
    var mathChallengeNum2 by remember { mutableIntStateOf(7) }
    var userAnswerText by remember { mutableStateOf("") }
    var isGateError by remember { mutableStateOf(false) }

    // Reset confirmation dialog
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "অভিভাবক কর্নার (Parent Dashboard)",
            starsCount = starsCount,
            onBackClick = { viewModel.navigateBack() },
            onHomeClick = { viewModel.navigateHome() }
        )

        if (!isUnlocked) {
            // Parent Gate Math Challenge
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(KidSkyBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = KidDeepBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "অভিভাবক নিরাপত্তা প্রবেশদ্বার",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidDeepBlue,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "বাচ্চাদের অনিচ্ছাকৃত প্রবেশ ঠেকাতে নিচের গুণটির উত্তর দিন:",
                            fontSize = 13.sp,
                            color = KidDeepBlue.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "$mathChallengeNum1 × $mathChallengeNum2 = ?",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KidAmberOrange
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = userAnswerText,
                            onValueChange = {
                                userAnswerText = it
                                isGateError = false
                            },
                            label = { Text("উত্তর লিখুন") },
                            isError = isGateError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .testTag("parent_gate_input")
                                .fillMaxWidth(0.6f)
                        )

                        if (isGateError) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "উত্তর সঠিক হয়নি, আবার চেষ্টা করুন।",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val expected = mathChallengeNum1 * mathChallengeNum2
                                if (userAnswerText.trim().toIntOrNull() == expected) {
                                    isUnlocked = true
                                } else {
                                    isGateError = true
                                }
                            },
                            modifier = Modifier
                                .testTag("parent_gate_unlock_button")
                                .fillMaxWidth(0.7f)
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidDeepBlue)
                        ) {
                            Text("প্রবেশ করুন", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        } else {
            // Unlocked Parent Settings & Reports
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Child Learning Level
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "🎯 শিশুর শিক্ষার স্তর নির্বাচন করুন:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "শিশুর বয়স ও পড়ার দক্ষতার ওপর ভিত্তি করে যেকোনো একটি স্তর বেছে নিন:",
                                fontSize = 12.sp,
                                color = KidDeepBlue.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LearningLevel.values().forEach { level ->
                                val isSelected = currentLevel == level
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) KidAmberOrange.copy(alpha = 0.15f) else Color(0xFFF8F9FA))
                                        .border(1.5.dp, if (isSelected) KidAmberOrange else Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                        .clickable { viewModel.setLearningLevel(level) }
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${level.labelBn} (${level.ageRange})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = KidDeepBlue
                                            )
                                            Text(
                                                text = when (level) {
                                                    LearningLevel.BEGINNER -> "বর্ণ চেনা, ছবি মেলানো, সংখ্যা ও সহজ আকার"
                                                    LearningLevel.EARLY_LEARNER -> "সম্পূর্ণ স্বরবর্ণ-ব্যঞ্জনবর্ণ, A-Z, ফোনেটিক্স ও ট্রেসিং"
                                                    LearningLevel.GROWING_READER -> "শব্দ বানান, সচিত্র গল্প পড়া ও বুদ্ধিবৃত্তিক কুইজ"
                                                },
                                                fontSize = 11.sp,
                                                color = KidDeepBlue.copy(alpha = 0.7f)
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = KidAmberOrange
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 2: Audio & Accessibility Controls
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "🔊 শব্দ ও উচ্চারণ নিয়ন্ত্রণ:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("সাউন্ড ইফেক্ট", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = KidDeepBlue)
                                    Text("অ্যাপের বাটন ও ক্লিকের আওয়াজ", fontSize = 11.sp, color = KidDeepBlue.copy(alpha = 0.7f))
                                }
                                Switch(
                                    checked = soundEnabled,
                                    onCheckedChange = { viewModel.setSoundEnabled(it) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = KidMintGreen, checkedTrackColor = KidMintGreen.copy(alpha = 0.3f))
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ভয়েস উচ্চারণ (TTS Voice)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = KidDeepBlue)
                                    Text("বর্ণ, শব্দ ও গল্পের মিষ্টি পাঠ", fontSize = 11.sp, color = KidDeepBlue.copy(alpha = 0.7f))
                                }
                                Switch(
                                    checked = ttsEnabled,
                                    onCheckedChange = { viewModel.setTtsEnabled(it) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = KidSkyBlue, checkedTrackColor = KidSkyBlue.copy(alpha = 0.3f))
                                )
                            }
                        }
                    }
                }

                // Section 3: Progress & Topics Needing Practice
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "📊 অগ্রগতি ও অধ্যয়ন রিপোর্ট:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Text("• মোট অর্জিত স্টার: $starsCount টি", fontSize = 13.sp, color = KidDeepBlue)
                            Text("• নিয়মিত অনুশীলন স্ট্রিক: $streakDays দিন", fontSize = 13.sp, color = KidDeepBlue)
                            Text("• সম্পন্ন করা পাঠ: ${completedLessons.size} টি", fontSize = 13.sp, color = KidDeepBlue)

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(KidSunnyYellow.copy(alpha = 0.2f))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "💡 আরও অনুশীলনের পরামর্শ:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = KidAmberOrange
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (completedLessons.size < 10) {
                                            "শিশুটিকে আরও বেশি বাংলা ব্যঞ্জনবর্ণ এবং হাত ঘুরিয়ে লেখার (Tracing) অনুশীলনে উৎসাহিত করুন।"
                                        } else {
                                            "চমৎকার অগ্রগতি! এবার শিশুকে 'গল্পের কুইজ' এবং 'বানান মেলাও' খেলায় অংশগ্রহণ করান।"
                                        },
                                        fontSize = 12.sp,
                                        color = KidDeepBlue
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 4: Privacy & Google Play Families Compliance
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = KidMintGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "শিশুর প্রাইভেসী ও অফলাইন নিরাপত্তা:",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidDeepBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• শতভাগ অফলাইন ও নিরাপদ: কোনো ইন্টারনেট কানেকশন প্রয়োজন নেই।\n• শিশু থেকে কোনো ব্যক্তিগত তথ্য (নাম, ছবি, বয়স বা লোকেশন) সংগ্রহ করা হয় না।\n• কোনো বিজ্ঞাপন বা থার্ড-পার্টি ট্র্যাকিং নেই।\n• Google Play Families পলিসি ও COPPA নীতিমালার সাথে সম্পূর্ণ সামঞ্জস্যপূর্ণ।",
                                fontSize = 12.sp,
                                color = KidDeepBlue.copy(alpha = 0.8f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Section 5: App Download & Google Play Publication Options
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "📲 অ্যাপ ডাউনলোড ও ব্যবহারের অপশনসমূহ:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidDeepBlue
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "🔹 অপশন ১: সরাসরি APK ডাউনলোড ও ইনস্টল (সবচেয়ে সহজ)\n" +
                                        "• AI Studio-র উপরের ডানপাশের সেটিংস মেনু (Settings / Export) চাপুন।\n" +
                                        "• 'Download APK' অপশন বেছে নিন।\n" +
                                        "• ফাইলটি ফোনে ডাউনলোড হলে সেটিতে ট্যাপ করে সরাসরি 'Install' করুন।\n\n" +
                                        "🔹 অপশন ২: মোবাইল ব্রাউজারে সাথে সাথে অ্যাপ হিসেবে ব্যবহার (PWA)\n" +
                                        "• গুগল ক্রোম ব্রাউজারে অ্যাপটি খোলা অবস্থায় উপরের ডানপাশের থ্রি-ডট (⋮) চাপুন।\n" +
                                        "• 'Add to Home screen' (হোম স্ক্রিনে যোগ করুন) চাপুন। আপনার ফোনের হোমস্ক্রিনে অ্যাপ আইকন চলে আসবে।\n\n" +
                                        "🔹 অপশন ৩: সম্পূর্ণ প্রজেক্ট জিপ (ZIP) ডাউনলোড\n" +
                                        "• AI Studio সেটিংস থেকে 'Export Project (ZIP)' অপশন বেছে নিয়ে পুরো সোর্স কোড ডাউনলোড করে ব্যাকআপ রাখতে পারবেন।\n\n" +
                                        "🔹 অপশন ৪: গুগল প্লে স্টোরে প্রকাশনা\n" +
                                        "• অ্যাপ আইডি: com.aistudio.shikhikids.bgvnkp\n" +
                                        "• Google Play Console-এ Families বিভাগে ৩-৮ বছর বয়স নির্বাচন করে AAB আপলোড করুন।",
                                fontSize = 12.sp,
                                color = KidDeepBlue.copy(alpha = 0.85f),
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                // Section 6: Reset Progress Button
                item {
                    Button(
                        onClick = { showResetDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFECEF))
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "পড়াশোনার অগ্রগতি রিসেট করুন",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("অগ্রগতি রিসেট করবেন?") },
            text = { Text("আপনি কি নিশ্চিত যে সকল স্টার ও সম্পন্ন করা পাঠের তথ্য মুছে দিয়ে নতুন করে শুরু করতে চান?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        viewModel.resetProgress()
                    }
                ) {
                    Text("হ্যাঁ, রিসেট করুন", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
