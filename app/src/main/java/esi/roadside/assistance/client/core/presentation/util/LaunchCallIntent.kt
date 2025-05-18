package esi.roadside.assistance.client.core.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.launchCallIntent(phoneNumber: String) {
    Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }.let {
        if (it.resolveActivity(this.packageManager) != null) startActivity(it)
    }
}