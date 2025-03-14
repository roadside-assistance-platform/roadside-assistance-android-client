package esi.roadside.assistance.client.main.util

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun MarkdownText(
    asset: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val text = remember {
        context.assets.open(asset).bufferedReader().use { it.readText() }
    }
    MarkdownText(
        modifier = modifier,
        markdown = text,
        maxLines = 3,
        style = LocalTextStyle.current.copy(
            textAlign = TextAlign.Justify
        )
    )
}