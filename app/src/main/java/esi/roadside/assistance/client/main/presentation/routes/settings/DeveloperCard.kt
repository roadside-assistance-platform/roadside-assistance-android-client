package esi.roadside.assistance.client.main.presentation.routes.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.presentation.constants.Settings
import esi.roadside.assistance.client.main.util.launch
import esi.roadside.assistance.client.main.util.openLink

@Composable
fun DeveloperCard(
    developers: Settings.Developers,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(modifier) {
        Column(
            Modifier.padding(vertical = 12.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(developers.type.icon, null, Modifier.padding(6.dp).size(24.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        developers.devName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        developers.type.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                item {
                    MyOutlinedButton(
                        Icons.Outlined.Email,
                        "Email"
                    ) {
                        context.launch(
                            Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(
                                    Intent.EXTRA_EMAIL,
                                    arrayOf(developers.email),
                                )
                            }
                        )
                    }
                }
                developers.github?.let { github ->
                    item {
                        MyOutlinedButton(
                            ImageVector.vectorResource(R.drawable.ic_twitter),
                            "Github"
                        ) {
                            context.openLink(
                                "https://github.com/$github"
                            )
                        }
                    }
                }
                developers.twitter?.let { twitter ->
                    item {
                        MyOutlinedButton(
                            ImageVector.vectorResource(R.drawable.ic_twitter),
                            "Twitter"
                        ) {
                            context.openLink(
                                "twitter://user?screen_name=$twitter",
                                "https://twitter.com/$twitter"
                            )
                        }
                    }
                }
                developers.linkedin?.let { linkedin ->
                    item {
                        MyOutlinedButton(
                            ImageVector.vectorResource(R.drawable.ic_twitter),
                            "LinkedIn"
                        ) {
                            context.openLink(
                                "https://linkedin.com/$linkedin"
                            )
                        }
                    }
                }
                developers.telegram?.let { telegram ->
                    item {
                        MyOutlinedButton(
                            ImageVector.vectorResource(R.drawable.ic_telegram_app),
                            "Telegram"
                        ) {
                            context.openLink("tg://resolve?domain=$telegram")
                        }
                    }
                }
                developers.website?.let { website ->
                    item {
                        MyOutlinedButton(
                            Icons.Default.Language,
                            "Website"
                        ) {
                            context.openLink(website)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyOutlinedButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(onClick, modifier) {
        Icon(icon, null, Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        Text(text)
    }
}