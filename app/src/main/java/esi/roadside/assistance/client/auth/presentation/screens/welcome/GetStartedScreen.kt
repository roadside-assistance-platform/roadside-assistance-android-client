package esi.roadside.assistance.client.auth.presentation.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.util.BackgroundBox
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.OutlinedButton
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun GetStartedScreen(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    BackgroundBox(R.drawable.welcome_background_4, modifier) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp)
                .padding(bottom = 24.dp)) {
            Column(Modifier.fillMaxWidth().align(Alignment.Center)) {
                ProvideTextStyle(
                    value = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.welcome_4),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.join_us),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.join_us_text),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(32.dp))
                Button(stringResource(R.string.log_in), Modifier.fillMaxWidth()) {
                    onAction(Action.GoToLogin)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(stringResource(R.string.sign_up), Modifier.fillMaxWidth()) {
                    onAction(Action.GoToSignup)
                }
            }
//            Column(Modifier.align(Alignment.BottomCenter)) {
//                Box(Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center) {
//                    HorizontalDivider(Modifier.fillMaxWidth())
//                    Text(
//                        "Or",
//                        Modifier
//                            .background(MaterialTheme.colorScheme.background)
//                            .padding(16.dp)
//                    )
//                }
//                Button(
//                    stringResource(R.string.log_in_with_google),
//                    Modifier.fillMaxWidth(),
//                    icon = ImageVector.vectorResource(R.drawable.google),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        contentColor = MaterialTheme.colorScheme.tertiary
//                    )
//                ) {
//                    onAction(Action.GoToGoogleLogin)
//                }
//            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GetStartedScreenPreview() {
    PreviewAppTheme {
        GetStartedScreen({})
    }
}