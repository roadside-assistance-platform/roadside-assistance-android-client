package esi.roadside.assistance.client.main.presentation.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import esi.roadside.assistance.client.R
import kotlin.math.floor


@Composable
fun RatingBar(
    rating: Double,
    onRatingChange: ((Double) -> Unit)? = null,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {
    val filledStars = floor(rating).toInt()
    Row(modifier = modifier.pointerInput(onRatingChange) {
            if (onRatingChange != null) {
                detectTapGestures(
                    onTap = { offset ->
                        val starWidth = size.width / stars
                        val newRating = ((offset.x / starWidth) + 0.5).toInt()
                        onRatingChange(newRating.toDouble())
                    }
                )
            }
        }
    ) {
        repeat(stars) {
            Box(Modifier.weight(1f).aspectRatio(1f)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.star_1_svgrepo_com),
                    contentDescription = null,
                    tint = starsColor,
                )
                androidx.compose.animation.AnimatedVisibility(
                    it < filledStars,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.star_svgrepo_com_filled),
                        contentDescription = null,
                        tint = starsColor
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun RatingPreview() {
    RatingBar(rating = 2.5)
}

@Preview
@Composable
fun TenStarsRatingPreview() {
    RatingBar(stars = 10, rating = 8.5)
}

@Preview
@Composable
fun RatingPreviewFull() {
    RatingBar(rating = 5.0)
}

@Preview
@Composable
fun RatingPreviewWorst() {
    RatingBar(rating = 1.0)
}

@Preview
@Composable
fun RatingPreviewDisabled() {
    RatingBar(rating = 0.0, starsColor = Color.Gray)
}