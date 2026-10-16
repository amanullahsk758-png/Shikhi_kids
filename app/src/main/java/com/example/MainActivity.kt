package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.MainViewModel
import com.example.ui.ScreenRoute
import com.example.ui.components.CelebrationDialog
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.BengaliScreen
import com.example.ui.screens.EnglishScreen
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NumbersShapesScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.StoriesScreen
import com.example.ui.screens.StoryReaderScreen
import com.example.ui.screens.TracingScreen
import com.example.ui.theme.ShikhiKidsTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShikhiKidsTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentRoute by viewModel.currentRoute.collectAsState()
    val celebrationStars by viewModel.celebrationStars.collectAsState()

    // Handle back button smoothly
    BackHandler(enabled = currentRoute != ScreenRoute.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedContent(
                targetState = currentRoute,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { route ->
                when (route) {
                    ScreenRoute.HOME -> HomeScreen(viewModel = viewModel)
                    ScreenRoute.BENGALI_LEARNING -> BengaliScreen(viewModel = viewModel)
                    ScreenRoute.ENGLISH_LEARNING -> EnglishScreen(viewModel = viewModel)
                    ScreenRoute.TRACING -> TracingScreen(viewModel = viewModel)
                    ScreenRoute.STORIES -> StoriesScreen(viewModel = viewModel)
                    ScreenRoute.STORY_READER -> StoryReaderScreen(viewModel = viewModel)
                    ScreenRoute.GAMES -> GamesScreen(viewModel = viewModel)
                    ScreenRoute.GAME_PLAY -> GamePlayScreen(viewModel = viewModel)
                    ScreenRoute.NUMBERS_SHAPES -> NumbersShapesScreen(viewModel = viewModel)
                    ScreenRoute.ACHIEVEMENTS -> AchievementsScreen(viewModel = viewModel)
                    ScreenRoute.PARENT_DASHBOARD -> ParentDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Star celebration popup
    celebrationStars?.let { stars ->
        CelebrationDialog(
            starsAwarded = stars,
            onDismiss = { viewModel.dismissCelebration() }
        )
    }
}
