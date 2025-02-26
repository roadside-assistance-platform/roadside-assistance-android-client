package esi.roadside.assistance.client.main.presentation.screens

import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.AppTheme

@Composable
fun WelcomeScreen(navController: NavController) {

    var welcomeScreen by rememberSaveable {
        mutableStateOf(1)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxHeight(0.55f)
                .fillMaxWidth(),
            factory = { context ->
                ImageView(context).apply {
                    setImageResource(
                        when (welcomeScreen) {
                            1 -> R.drawable.welcome_background_1
                            2 -> R.drawable.welcome_background_2
                            else -> R.drawable.welcome_background_3
                        }
                    )
                    scaleType = ImageView.ScaleType.FIT_XY
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        setImageResource(
                            when (welcomeScreen) {
                                1 -> R.drawable.welcome_1
                                2 -> R.drawable.welcome_2
                                else -> R.drawable.welcome_3
                            }
                        )
                        scaleType = ImageView.ScaleType.FIT_XY
                    }
                }
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = when(welcomeScreen){
                    1 -> stringResource(R.string.welcome_1_title)
                    2 -> stringResource(R.string.welcome_2_title)
                    else -> stringResource(R.string.welcome_3_title)
                },
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )

            Text(
                text = when(welcomeScreen){
                    1 -> stringResource(R.string.welcome_1_des)
                    2 -> stringResource(R.string.welcome_2_des)
                    else -> stringResource(R.string.welcome_3_des)
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 20.dp)
            )

            Spacer(Modifier.fillMaxHeight(0.4f))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                if (welcomeScreen >= 3) Spacer(Modifier.width(1.dp)) else {
                    Button(
                        onClick = {
                            navController.navigate("Auth")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Skip")
                    }
                }

                Button(
                    onClick = {
                        if (welcomeScreen != 3) welcomeScreen++
                        else navController.navigate("Auth") // Ki ndir graph nbadalah
                    },
                ) {
                    Text(if (welcomeScreen <3)"Next" else "Get started")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        WelcomeScreen(
            rememberNavController()
        )
    }
}
