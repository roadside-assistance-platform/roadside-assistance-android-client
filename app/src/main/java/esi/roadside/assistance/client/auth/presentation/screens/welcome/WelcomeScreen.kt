package esi.roadside.assistance.client.auth.presentation.screens.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action

@Composable
fun WelcomeScreen(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    val onNext = { if (step < 3) step++ }
    val onSkip = { step = 3 }
    AnimatedContent(
        step,
        Modifier.fillMaxSize(),
        {
            if (initialState > targetState)
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            else
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        }
    ) {
        when(it) {
            0 -> WelcomeScreenTemplate(
                background = R.drawable.welcome_background_1,
                picture = R.drawable.welcome_1,
                title = R.string.welcome_title_1,
                text = R.string.welcome_text_1,
                modifier = modifier,
                onSkip = onSkip,
                onNext = onNext
            )
            1 -> WelcomeScreenTemplate(
                background = R.drawable.welcome_background_2,
                picture = R.drawable.welcome_2,
                title = R.string.welcome_title_2,
                text = R.string.welcome_text_2,
                modifier = modifier,
                onSkip = onSkip,
                onNext = onNext
            )
            2 -> WelcomeScreenTemplate(
                background = R.drawable.welcome_background_3,
                picture = R.drawable.welcome_3,
                title = R.string.welcome_title_3,
                text = R.string.welcome_text_3,
                modifier = modifier,
                onSkip = onSkip,
                onNext = onNext
            )
            3 -> GetStartedScreen(onAction)
        }
    }
    BackHandler((step < 3) and (step > 0)) {
        step--
    }
}