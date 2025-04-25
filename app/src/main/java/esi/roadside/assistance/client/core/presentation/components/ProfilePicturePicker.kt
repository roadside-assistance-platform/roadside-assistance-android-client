package esi.roadside.assistance.client.core.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ProfilePicturePicker(
    image: Any?,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.AddAPhoto,
    enabled: Boolean = true,
    onImageChange: (Uri?) -> Unit
) {
    val photoPicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                it?.let { onImageChange(it) }
            },
        )
    Box(
        modifier
            .shadow(4.dp, MaterialTheme.shapes.extraLarge)
            .size(140.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(enabled = enabled) {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(Modifier.fillMaxSize(.5f))
                }
            },
            error = {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (enabled) Icons.Default.AddAPhoto else icon,
                        null,
                        Modifier.size(48.dp),
                        MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        )
        if (enabled && image != null)
            SmallFloatingActionButton(
                { onImageChange(null) },
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset((-8).dp, (-8).dp)
            ) {
                Icon(Icons.Default.Delete, null)
            }
    }
}