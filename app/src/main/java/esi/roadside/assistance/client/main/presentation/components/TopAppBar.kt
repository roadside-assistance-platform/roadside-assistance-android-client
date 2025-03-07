package esi.roadside.assistance.client.main.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R

@Composable
fun TopAppBar(modifier: Modifier = Modifier,
              @StringRes title : Int,
              @DrawableRes drawableRes: Int,
              height : Float = 0.3f,
              @StringRes des : Int? = null,
              image : ImageVector? = null
              ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(height),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(drawableRes), null, Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                stringResource(title), style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.background
            )
            Text(stringResource(des ?: R.string.empty),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,)
        }
        image?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(image, null, Modifier.size(106.dp))
            }
        }
    }


}