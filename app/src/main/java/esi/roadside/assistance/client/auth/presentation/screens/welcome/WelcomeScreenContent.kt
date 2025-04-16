package esi.roadside.assistance.client.auth.presentation.screens.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.util.BackgroundBox
import esi.roadside.assistance.client.main.presentation.components.DefaultBackNavButton

@Composable
fun WelcomeScreenTemplate(
    background: Int,
    picture: Int,
    title: Int,
    text: Int,
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    loading: Boolean = false,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    BackgroundBox(
        background,
        modifier,
        navigationButton = {
            if (showNavigationIcon)
                DefaultBackNavButton(Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .offset(12.dp, 12.dp)
                )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)) {
            Column(
                Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                ProvideTextStyle(
                    value = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                ) {
                    Image(
                        painter = painterResource(id = picture),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = stringResource(text),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            AnimatedContent(loading, Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
                if (it)
                    LinearProgressIndicator(Modifier.fillMaxWidth().padding(24.dp))
                else
                    Row(
                        Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onSkip,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Text(stringResource(R.string.skip))
                        }
                        Button(
                            onNext,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            )
                        ) {
                            Text(stringResource(R.string.next))
                        }
                    }
            }
        }
    }
}