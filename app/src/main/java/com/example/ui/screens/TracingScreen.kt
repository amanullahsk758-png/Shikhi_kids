package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.theme.KidSunnyYellow

data class TracingStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun TracingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val starsCount by viewModel.progressRepository.stars.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    val selectedItemState by viewModel.selectedTracingItem.collectAsState()

    // Available letters to trace (Bengali vowels, some consonants, and English A-Z)
    val tracingOptions = remember {
        CurriculumRepository.bengaliVowels.take(8) +
                CurriculumRepository.bengaliConsonants.take(8) +
                CurriculumRepository.englishAlphabet.take(8)
    }

    var currentItem by remember {
        mutableStateOf(selectedItemState ?: CurriculumRepository.bengaliVowels.first())
    }

    // Drawing crayon colors
    val crayonColors = listOf(
        KidAmberOrange,
        KidBerryPink,
        KidSkyBlue,
        KidMintGreen,
        Color(0xFFFF3B30),
        Color(0xFF9D4EDD)
    )
    var selectedColor by remember { mutableStateOf(crayonColors.first()) }

    // User strokes
    val strokes = remember { mutableStateListOf<TracingStroke>() }
    var currentPoints = remember { mutableStateListOf<Offset>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidCreamBackground)
    ) {
        KidTopBar(
            title = "হাত ঘুরিয়ে লিখি (Tracing)",
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
            // Horizontal Letter Selector
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tracingOptions) { item ->
                    val isCurrent = item.id == currentItem.id
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isCurrent) KidAmberOrange else Color.White)
                            .border(
                                1.5.dp,
                                if (isCurrent) KidAmberOrange else Color.LightGray.copy(alpha = 0.5f),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                currentItem = item
                                strokes.clear()
                                currentPoints.clear()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.letter,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else KidDeepBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Info Bar with Audio & Instructions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "আঙুল দিয়ে দাগের ওপর হাত ঘোরাও ✏️",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KidDeepBlue
                )

                SpeakerButton(
                    onClick = {
                        val speechLang = if (currentItem.language == "bn") "bn" else "en"
                        viewModel.speak("${currentItem.letter}। ${currentItem.exampleWord}", speechLang)
                    },
                    isSpeaking = isSpeaking,
                    sizeDp = 42,
                    contentDescription = "উচ্চারণ শোনো"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Tracing Canvas Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(26.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Background Guide Letter (Dotted watermark style)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentItem.letter,
                            fontSize = 170.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE2E8F0),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Touch Drawing Surface
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(currentItem.id) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        currentPoints = mutableStateListOf(offset)
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        currentPoints.add(change.position)
                                    },
                                    onDragEnd = {
                                        if (currentPoints.isNotEmpty()) {
                                            strokes.add(
                                                TracingStroke(
                                                    points = currentPoints.toList(),
                                                    color = selectedColor,
                                                    strokeWidth = 24f
                                                )
                                            )
                                            currentPoints.clear()
                                        }
                                    }
                                )
                            }
                    ) {
                        // Draw all finished strokes
                        for (stroke in strokes) {
                            if (stroke.points.size > 1) {
                                val path = Path().apply {
                                    moveTo(stroke.points.first().x, stroke.points.first().y)
                                    for (i in 1 until stroke.points.size) {
                                        lineTo(stroke.points[i].x, stroke.points[i].y)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = stroke.color,
                                    style = Stroke(
                                        width = stroke.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Draw current in-progress stroke
                        if (currentPoints.size > 1) {
                            val activePath = Path().apply {
                                moveTo(currentPoints.first().x, currentPoints.first().y)
                                for (i in 1 until currentPoints.size) {
                                    lineTo(currentPoints[i].x, currentPoints[i].y)
                                }
                            }
                            drawPath(
                                path = activePath,
                                color = selectedColor,
                                style = Stroke(
                                    width = 24f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Crayon Color Palette
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "রং:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = KidDeepBlue,
                    modifier = Modifier.padding(end = 8.dp)
                )

                crayonColors.forEach { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 36.dp else 30.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) KidDeepBlue else Color.White,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Clear, Try Again, and Complete / Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        strokes.clear()
                        currentPoints.clear()
                    },
                    modifier = Modifier
                        .testTag("tracing_clear_button")
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.6f))
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = KidDeepBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("মুছে ফেলো", color = KidDeepBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        // Award star for effort!
                        viewModel.awardStars("trace_${currentItem.id}", 1)
                        // Advance to next letter if available
                        val nextIdx = (tracingOptions.indexOfFirst { it.id == currentItem.id } + 1) % tracingOptions.size
                        currentItem = tracingOptions[nextIdx]
                        strokes.clear()
                        currentPoints.clear()
                    },
                    modifier = Modifier
                        .testTag("tracing_submit_button")
                        .weight(1.4f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KidMintGreen)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("হয়েছে! পরের বর্ণ 🌟", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
